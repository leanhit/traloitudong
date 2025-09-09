package com.chatbot.chatHub.facebook.webhook.service;

import com.chatbot.chatHub.facebook.webhook.dto.SimpleBotpressMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.HashMap;

/**
 * Service gửi tin nhắn văn bản đến Botpress và nhận phản hồi.
 */
@Service
public class BotpressServiceFb {

    @Value("${botpress.api.url}")
    private String botpressUrl;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public BotpressServiceFb(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> sendMessageToBotpress(String botId, String senderId, String messageText) {
        System.out.println("===>" + botId + "===" + senderId + "===" + messageText);
        try {
            String url = String.format("%s/api/v1/bots/%s/converse/%s", botpressUrl, botId, senderId);
            SimpleBotpressMessage botpressMessage = new SimpleBotpressMessage(messageText);

            System.out.println("--------------------------------------------------");
            System.out.println("🚀 Gửi tin nhắn tới Botpress:");
            System.out.println("   - URL: " + url);
            System.out.println("   - Sender ID: " + senderId);
            System.out.println("   - Nội dung tin nhắn: " + messageText);
            System.out.println("--------------------------------------------------");

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

    public Map<String, Object> sendEventToBotpress(String botId, String senderId, String eventName, Map<String, Object> customPayload) {
        String url = String.format("%s/api/v1/bots/%s/converse/%s", botpressUrl, botId, senderId);
        
        try {
            String jsonPayload = objectMapper.writeValueAsString(customPayload);
            String finalPayloadText;
            
            // Botpress có giới hạn 360 ký tự cho tin nhắn văn bản
            if (jsonPayload.length() > 360) {
                finalPayloadText = "Tệp đính kèm quá lớn để xử lý.";
            } else {
                finalPayloadText = jsonPayload;
            }

            Map<String, String> simplePayload = new HashMap<>();
            simplePayload.put("text", finalPayloadText);

            System.out.println("🚀 Gửi event tới Botpress:");
            System.out.println(" - URL: " + url);
            System.out.println(" - User: " + senderId);
            System.out.println(" - Simple Payload: " + simplePayload);

            return webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(simplePayload)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            System.err.println("❌ LỖI: " + e.getRawStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi xử lý JSON: " + e.getMessage());
            return null;
        }
    }

}
