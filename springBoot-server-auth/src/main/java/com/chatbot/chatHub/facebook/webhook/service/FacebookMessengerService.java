// src/main/java/com/chatbot/chatHub/facebook/webhook/service/FacebookMessengerService.java
package com.chatbot.chatHub.facebook.webhook.service;

import com.chatbot.chatHub.facebook.connection.model.FacebookConnection;
import com.chatbot.chatHub.facebook.connection.repository.FacebookConnectionRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service gửi tin nhắn phản hồi đến người dùng Facebook.
 */
@Service
public class FacebookMessengerService {

    private final FacebookConnectionRepository connectionRepository;
    private final WebClient webClient;

    public FacebookMessengerService(FacebookConnectionRepository connectionRepository, WebClient.Builder webClientBuilder) {
        this.connectionRepository = connectionRepository;
        this.webClient = webClientBuilder.build();
    }

    /**
     * Gửi một tin nhắn text tới người dùng Facebook.
     */
    public void sendMessageToUser(String pageId, String recipientId, String messageText) {
        Optional<FacebookConnection> connectionOpt = connectionRepository.findByPageId(pageId);

        connectionOpt.ifPresentOrElse(connection -> {
            String accessToken = connection.getPageAccessToken();
            String url = "https://graph.facebook.com/v18.0/me/messages?access_token=" + accessToken;

            // Payload đúng format Facebook Messenger API
            Map<String, Object> payload = Map.of(
                "recipient", Map.of("id", recipientId),
                "message", Map.of("text", messageText)
            );

            System.out.println("--------------------------------------------------");
            System.out.println("Gửi tin nhắn tới Facebook:");
            System.out.println("  - URL: " + url);
            System.out.println("  - Recipient ID: " + recipientId);
            System.out.println("  - Payload: " + payload);
            System.out.println("--------------------------------------------------");

            for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

                System.out.println("✅ Gửi tin nhắn tới " + recipientId + " thành công (attempt " + attempt + ")");
                return; // thành công thì thoát luôn
            } catch (Exception e) {
                System.err.println("⚠️ Lỗi gửi tin nhắn (attempt " + attempt + "): " + e.getMessage());

                if (attempt < 3) {
                    try {
                        Thread.sleep(1000L * attempt); // delay 1s, 2s...
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        }, () -> {
            System.err.println("❌ Không tìm thấy cấu hình cho page_id: " + pageId);
        });
    }

    /**
     * Xử lý toàn bộ phản hồi từ Botpress và gửi về Facebook Messenger.
     */
    @SuppressWarnings("unchecked")
    public void sendBotpressRepliesToUser(String pageId, String senderId, Map<String, Object> botpressResponse) {
        List<Map<String, Object>> replies = (List<Map<String, Object>>) botpressResponse.get("responses");

        if (replies != null) {
            Set<String> sentMessages = new HashSet<>();

            for (Map<String, Object> reply : replies) {
                String type = (String) reply.get("type");
                if ("text".equals(type)) {
                    String text = (String) reply.get("text");

                    // Nếu chưa gửi thì mới gửi
                    if (text != null && sentMessages.add(text)) {
                        sendMessageToUser(pageId, senderId, text);
                    } else {
                        System.out.println("⚠️ Bỏ qua message trùng: " + text);
                    }
                }
                // TODO: xử lý thêm image, quick_replies, card...
            }
        } else {
            System.out.println("⚠️ Botpress không trả về 'responses' hoặc rỗng.");
        }
    }

}
