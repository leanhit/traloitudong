package com.chatbot.chatHub.facebook.webhook.service;

import com.chatbot.chatHub.facebook.connection.model.FacebookConnection;
import com.chatbot.chatHub.facebook.connection.repository.FacebookConnectionRepository;
import com.chatbot.chatHub.facebook.webhook.dto.WebhookRequest;
import com.chatbot.chatHub.facebook.webhook.model.FacebookMessageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service xử lý các sự kiện webhook từ Facebook.
 * Phiên bản cải tiến: tránh gửi trùng tin nhắn dựa trên messageId (mid).
 */
@Service
public class FacebookWebhookService {

    private final FacebookConnectionRepository connectionRepository;
    private final BotpressServiceFb botpressService;
    private final FacebookMessengerService facebookMessengerService;

    @Value("${facebook.autoConnect.verifyToken}")
    private String configuredVerifyToken;

    // Lưu các mid đã xử lý, tránh gửi trùng
    private final Set<String> processedMessageIds = ConcurrentHashMap.newKeySet();

    public FacebookWebhookService(FacebookConnectionRepository connectionRepository,
                                  BotpressServiceFb botpressService,
                                  FacebookMessengerService facebookMessengerService) {
        this.connectionRepository = connectionRepository;
        this.botpressService = botpressService;
        this.facebookMessengerService = facebookMessengerService;
    }

    public boolean verifyWebhook(String mode, String challenge, String verifyToken) {
        return "subscribe".equals(mode) && configuredVerifyToken.equals(verifyToken);
    }

    private FacebookMessageType classifyMessage(WebhookRequest.Messaging messaging) {
        if (messaging.getMessage() != null) {
            if (Boolean.TRUE.equals(messaging.getMessage().getIsEcho())) return FacebookMessageType.ECHO;
            if (messaging.getMessage().getQuickReply() != null) return FacebookMessageType.QUICK_REPLY;
            if (messaging.getMessage().getText() != null) return FacebookMessageType.TEXT;
            if (messaging.getMessage().getAttachments() != null && !messaging.getMessage().getAttachments().isEmpty()) {
                String type = messaging.getMessage().getAttachments().get(0).getType();
                switch (type) {
                    case "image": return FacebookMessageType.IMAGE;
                    case "video": return FacebookMessageType.VIDEO;
                    case "audio": return FacebookMessageType.AUDIO;
                    case "file":  return FacebookMessageType.FILE;
                    default:      return FacebookMessageType.ATTACHMENT;
                }
            }

        } else if (messaging.getPostback() != null) return FacebookMessageType.POSTBACK;
        else if (messaging.getReaction() != null) return FacebookMessageType.REACTION;
        else if (messaging.getRead() != null) return FacebookMessageType.READ;
        else if (messaging.getDelivery() != null) return FacebookMessageType.DELIVERY;

        return FacebookMessageType.UNKNOWN;
    }

    @SuppressWarnings("unchecked")
    public void handleWebhookEvent(WebhookRequest request) {
        if (!"page".equals(request.getObject())) return;

        for (WebhookRequest.Entry entry : request.getEntry()) {
            if (entry.getMessaging() == null) continue;

            for (WebhookRequest.Messaging messaging : entry.getMessaging()) {
                // Xác định pageId & senderId
                String pageId = (messaging.getMessage() != null && Boolean.TRUE.equals(messaging.getMessage().getIsEcho()))
                        ? messaging.getSender().getId()
                        : messaging.getRecipient().getId();
                String senderId = (messaging.getMessage() != null && Boolean.TRUE.equals(messaging.getMessage().getIsEcho()))
                        ? messaging.getRecipient().getId()
                        : messaging.getSender().getId();

                FacebookMessageType type = classifyMessage(messaging);

                // Bỏ qua tin nhắn ECHO
                if (type == FacebookMessageType.ECHO) {
                    System.out.println("🔄 Bỏ qua tin nhắn ECHO: " + messaging.getMessage().getText());
                    continue;
                }

                Optional<FacebookConnection> connectionOpt = connectionRepository.findByPageId(pageId);
                if (connectionOpt.isEmpty() || !connectionOpt.get().isEnabled()) continue;
                FacebookConnection connection = connectionOpt.get();

                // Xử lý dựa trên loại message
                switch (type) {
                    case TEXT:
                        handleTextMessage(connection, senderId, messaging.getMessage());
                        break;

                    case IMAGE:
                    case VIDEO:
                    case AUDIO:
                    case FILE:
                    case ATTACHMENT: // fallback nếu không rõ loại
                        handleAttachmentMessage(connection, senderId, messaging);
                        break;

                    case QUICK_REPLY:
                        handleQuickReply(connection, senderId, messaging);
                        break;

                    case POSTBACK:
                        handlePostback(connection, senderId, messaging);
                        break;

                    case REACTION:
                        handleReaction(connection, senderId, messaging);
                        break;

                    case READ:
                        handleRead(messaging);
                        break;

                    case DELIVERY:
                        handleDelivery(messaging);
                        break;

                    default:
                        System.out.println("⚠️ Loại message không xác định, bỏ qua.");
                }

            }
        }
    }

    // ========== HANDLERS ==========

    private void handleTextMessage(FacebookConnection connection, String senderId, WebhookRequest.Message message) {
        String mid = message.getMid();
        String text = message.getText();
        if (text == null || text.isEmpty() || mid == null) return;

        // Nếu message đã xử lý, bỏ qua
        if (!processedMessageIds.add(mid)) {
            System.out.println("⚠️ Bỏ qua message trùng mid=" + mid);
            return;
        }

        System.out.println("✉️ Xử lý TEXT: " + text);

        Map<String, Object> botpressResponse = botpressService.sendMessageToBotpress(
                connection.getBotId(), senderId, text
        );

        facebookMessengerService.sendBotpressRepliesToUser(connection.getPageId(), senderId, botpressResponse);
    }
    
    private void handleAttachmentMessage(FacebookConnection connection, String senderId, WebhookRequest.Messaging messaging) {
        messaging.getMessage().getAttachments().forEach(attachment -> {
        String type = attachment.getType(); 
        String url = attachment.getPayload() != null ? attachment.getPayload().getUrl() : null;
        String mid = messaging.getMessage().getMid();

        if (mid == null || !processedMessageIds.add(mid)) {
            System.out.println("⚠️ Bỏ qua attachment trùng mid=" + mid);
            return;
        }

        if (url != null) {
            System.out.println("🖼 ATTACHMENT: type=" + type + ", url=" + url);

            Map<String, Object> eventPayload = new HashMap<>();
            eventPayload.put("type", type);
            eventPayload.put("url", url);

            Map<String, Object> botpressResponse = botpressService.sendEventToBotpress(
            connection.getBotId(),
            senderId,
            "facebook.attachment",
            eventPayload
            );

            facebookMessengerService.sendBotpressRepliesToUser(connection.getPageId(), senderId, botpressResponse);
        }
        });
    }

    private void handleQuickReply(FacebookConnection connection, String senderId, WebhookRequest.Messaging messaging) {
        String payload = messaging.getMessage().getQuickReply().getPayload();
        botpressService.sendMessageToBotpress(connection.getBotId(), senderId, "[QuickReply] " + payload);
    }

    private void handlePostback(FacebookConnection connection, String senderId, WebhookRequest.Messaging messaging) {
        String payload = messaging.getPostback().getPayload();
        botpressService.sendMessageToBotpress(connection.getBotId(), senderId, "[Postback] " + payload);
    }

    /**
     * Handles 'reaction' events from Facebook Messenger.
     * Forwards the reaction to Botpress as a custom event.
     */
    private void handleReaction(FacebookConnection connection, String senderId, WebhookRequest.Messaging messaging) {
        if (messaging.getReaction() == null || messaging.getReaction().getEmoji() == null) {
            System.out.println("⚠️ Reaction without emoji, skipping.");
            return;
        }

        String action = messaging.getReaction().getAction(); // "react" or "unreact"
        String emoji = messaging.getReaction().getEmoji();
        String mid = messaging.getReaction().getMid();

        // Check for message ID to prevent duplicates, though less critical for reactions
        if (mid == null || !processedMessageIds.add(mid)) {
            System.out.println("⚠️ Skipping duplicate reaction mid=" + mid);
            return;
        }

        System.out.println("❤️ REACTION: action=" + action + ", emoji=" + emoji);

        // Create a custom event payload for Botpress
        Map<String, Object> eventPayload = new HashMap<>();
        eventPayload.put("action", action);
        eventPayload.put("emoji", emoji);
        eventPayload.put("mid", mid);

        try {
            // Send the custom event to Botpress
            Map<String, Object> botpressResponse = botpressService.sendEventToBotpress(
                connection.getBotId(),
                senderId,
                "facebook.reaction", // The custom event name for Botpress
                eventPayload
            );
            facebookMessengerService.sendBotpressRepliesToUser(connection.getPageId(), senderId, botpressResponse);
        } catch (Exception e) {
            System.err.println("❌ Error sending reaction event to Botpress: " + e.getMessage());
            // Clean up processed ID in case of an error
            processedMessageIds.remove(mid);
        }
    }    

    private void handleRead(WebhookRequest.Messaging messaging) {
        System.out.println("👀 READ: watermark=" + messaging.getRead().getWatermark());
    }

    private void handleDelivery(WebhookRequest.Messaging messaging) {
        System.out.println("📬 DELIVERY: mids=" + messaging.getDelivery().getMids());
    }
}
