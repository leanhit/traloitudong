// src/main/java/com/chatbot/chatHub/facebook/webhook/service/FacebookWebhookService.java
package com.chatbot.chatHub.facebook.webhook.service;

import com.chatbot.webHub.facebook.connection.model.FacebookConnection;
import com.chatbot.webHub.facebook.connection.repository.FacebookConnectionRepository;
import com.chatbot.chatHub.facebook.webhook.dto.WebhookRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Service xử lý các sự kiện webhook từ Facebook.
 * Đã cập nhật để log chi tiết quá trình truy vấn database
 * và forward phản hồi từ Botpress về lại Facebook Messenger.
 */
@Service
public class FacebookWebhookService {

    private final FacebookConnectionRepository connectionRepository;
    private final BotpressServiceFb botpressService;
    private final FacebookMessengerService facebookMessengerService;

    public FacebookWebhookService(FacebookConnectionRepository connectionRepository,
                                  BotpressServiceFb botpressService,
                                  FacebookMessengerService facebookMessengerService) {
        this.connectionRepository = connectionRepository;
        this.botpressService = botpressService;
        this.facebookMessengerService = facebookMessengerService;
    }

    public boolean verifyWebhook(String mode, String challenge, String verifyToken) {
        if ("subscribe".equals(mode)) {
            return connectionRepository.findByVerifyToken(verifyToken).isPresent();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void handleWebhookEvent(WebhookRequest request) {
        if ("page".equals(request.getObject())) {
            for (WebhookRequest.Entry entry : request.getEntry()) {
                if (entry.getMessaging() != null) {
                    for (WebhookRequest.Messaging messaging : entry.getMessaging()) {
                        String pageId = messaging.getRecipient().getId();
                        String senderId = messaging.getSender().getId();

                        System.out.println("--------------------------------------------------");
                        System.out.println("✅ Nhận được tin nhắn từ Facebook Webhook.");
                        System.out.println("  - Page ID từ tin nhắn: " + pageId);
                        System.out.println("  - Sender ID: " + senderId);

                        Optional<FacebookConnection> connectionOpt = connectionRepository.findByPageId(pageId);

                        if (connectionOpt.isPresent()) {
                            FacebookConnection connection = connectionOpt.get();
                            System.out.println("  -> Đã tìm thấy kết nối trong DB với Page ID: " + pageId);

                            if (connection.isEnabled()
                                    && messaging.getMessage() != null
                                    && messaging.getMessage().getText() != null) {

                                String messageText = messaging.getMessage().getText();

                                System.out.println("  -> Kết nối đang bật, chuẩn bị gửi tới Botpress...");
                                System.out.println("  - Bot ID: " + connection.getBotId());
                                System.out.println("  - Sender ID: " + senderId);
                                System.out.println("  - Nội dung tin nhắn: " + messageText);

                                // Log URL gửi tin nhắn tới Botpress
                                String botpressUrl = String.format(
                                        "http://localhost:3001/api/v1/bots/%s/converse/%s",
                                        connection.getBotId(),
                                        senderId
                                );
                                System.out.println("🚀 URL gửi tới Botpress: " + botpressUrl);
                                System.out.println("--------------------------------------------------");

                                // Gửi tin nhắn tới Botpress
                                Map<String, Object> botpressResponse = botpressService.sendMessageToBotpress(
                                        connection.getBotId(),
                                        senderId,
                                        messageText
                                );

                                // Forward phản hồi từ Botpress về Facebook Messenger
                                facebookMessengerService.sendBotpressRepliesToUser(
                                        pageId,
                                        senderId,
                                        botpressResponse
                                );

                            } else {
                                System.out.println("  -> Kết nối tắt hoặc tin nhắn không phải dạng text, bỏ qua.");
                                System.out.println("--------------------------------------------------");
                            }
                        } else {
                            System.err.println("❌ Không tìm thấy kết nối nào trong DB với Page ID: " + pageId);
                            System.out.println("--------------------------------------------------");
                        }
                    }
                }
            }
        }
    }
}
