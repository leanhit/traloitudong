// src/main/java/com/chatbot/chatHub/facebook/webhook/service/FacebookApiGraphService.java
package com.chatbot.chatHub.facebook.webhook.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FacebookApiGraphService {

    private final WebClient webClient;

    public FacebookApiGraphService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://graph.facebook.com/v18.0").build();
    }

    /**
     * Gửi yêu cầu POST đến Facebook để đăng ký webhook cho một trang.
     * @param pageId ID của trang
     * @param pageAccessToken Access token của trang
     */
    public void subscribePageToWebhook(String pageId, String pageAccessToken) {
        try {
            webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/{pageId}/subscribed_apps")
                    .queryParam("access_token", pageAccessToken) // Thêm access_token vào query
                    .build(pageId))
                .body(Mono.just("subscribed_fields=messages,messaging_postbacks"), String.class)
                .headers(headers -> headers.set("Content-Type", "application/x-www-form-urlencoded"))
                .retrieve()
                .bodyToMono(Void.class) // Expecting an empty body response
                .block();

            System.out.println("✅ Đã đăng ký webhook thành công cho trang: " + pageId);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đăng ký webhook cho trang " + pageId + ": " + e.getMessage());
        }
    }

    /**
     * Gửi yêu cầu DELETE đến Facebook để hủy đăng ký webhook cho một trang.
     * @param pageId ID của trang
     * @param pageAccessToken Access token của trang
     */
    public void unsubscribePageFromWebhook(String pageId, String pageAccessToken) {
        try {
            webClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/{pageId}/subscribed_apps")
                    .queryParam("access_token", pageAccessToken)
                    .build(pageId))
                .retrieve()
                .bodyToMono(Void.class)
                .block();

            System.out.println("✅ Đã hủy đăng ký webhook thành công cho trang: " + pageId);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi hủy đăng ký webhook cho trang " + pageId + ": " + e.getMessage());
        }
    }
}