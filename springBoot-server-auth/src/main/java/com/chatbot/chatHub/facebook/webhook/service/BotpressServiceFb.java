// src/main/java/com/chatbot/chatHub/facebook/webhook/service/BotpressServiceFb.java
package com.chatbot.chatHub.facebook.webhook.service;

import com.chatbot.chatHub.facebook.webhook.dto.SimpleBotpressMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * Service gửi tin nhắn văn bản đến Botpress và nhận phản hồi.
 */
@Service
public class BotpressServiceFb {

    @Value("${botpress.api.url}")
    private String botpressUrl;

    private final WebClient webClient;

    public BotpressServiceFb(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> sendMessageToBotpress(String botId, String senderId, String messageText) {
        System.out.println("===>" + botId + "===" + senderId + "===" + messageText);
        try {
            String url = String.format("%s/api/v1/bots/%s/converse/%s", botpressUrl, botId, senderId);
            SimpleBotpressMessage botpressMessage = new SimpleBotpressMessage(messageText);

            // In log chi tiết về tin nhắn đang được gửi tới Botpress
            System.out.println("--------------------------------------------------");
            System.out.println("🚀 Gửi tin nhắn tới Botpress:");
            System.out.println("  - URL: " + url);
            System.out.println("  - Sender ID: " + senderId);
            System.out.println("  - Nội dung tin nhắn: " + messageText);
            System.out.println("--------------------------------------------------");

            // Gọi API Botpress và parse kết quả JSON về Map<String,Object>
            Map<String, Object> response = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(botpressMessage)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            System.out.println("✅ Message successfully forwarded to Botpress for bot " + botId);
            return response;

        } catch (Exception e) {
            System.err.println("❌ Failed to send message to Botpress: " + e.getMessage());
            return null;
        }
    }
}
