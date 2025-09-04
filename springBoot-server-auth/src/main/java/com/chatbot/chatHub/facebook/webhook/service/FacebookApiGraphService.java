// src/main/java/com/chatbot/chatHub/facebook/webhook/service/FacebookApiGraphService.java
package com.chatbot.chatHub.facebook.webhook.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Service
public class FacebookApiGraphService {

    private final WebClient webClient;

    @Value("${facebook.autoConnect.appId}")
    private String appId;

    @Value("${facebook.autoConnect.appSecret}")
    private String appSecret;

    public FacebookApiGraphService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://graph.facebook.com/v18.0").build();
    }

    // ✅ Lấy danh sách fanpage user quản lý
    public List<Map<String, Object>> getUserPages(String userAccessToken) {
        try {
            String longLivedToken = getLongLivedUserToken(userAccessToken);
            if (longLivedToken == null) {
                System.err.println("❌ Không thể convert user token dài hạn");
                return List.of();
            }

            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/me/accounts")
                            .queryParam("access_token", longLivedToken)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (response != null && response.get("data") instanceof List) {
                System.out.println("✅ Lấy danh sách fanpage thành công");
                return (List<Map<String, Object>>) response.get("data");
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy fanpage từ Facebook: " + e.getMessage());
        }
        return List.of();
    }

    // ✅ Đăng ký webhook
    public void subscribePageToWebhook(String pageId, String pageAccessToken) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("subscribed_fields", "messages,messaging_postbacks");

            webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/{pageId}/subscribed_apps")
                            .queryParam("access_token", pageAccessToken)
                            .build(pageId))
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            System.out.println("✅ Đã đăng ký webhook cho page: " + pageId);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đăng ký webhook cho page " + pageId + ": " + e.getMessage());
        }
    }

    // ✅ Hủy đăng ký webhook
    public void unsubscribePageFromWebhook(String pageId, String pageAccessToken) {
        try {
            webClient.delete()
                    .uri(uriBuilder -> uriBuilder.path("/{pageId}/subscribed_apps")
                            .queryParam("access_token", pageAccessToken)
                            .build(pageId))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            System.out.println("✅ Đã hủy webhook cho page: " + pageId);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi hủy webhook cho page " + pageId + ": " + e.getMessage());
        }
    }

    // ✅ Convert user token ngắn hạn thành dài hạn
    private String getLongLivedUserToken(String shortLivedToken) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/oauth/access_token")
                            .queryParam("grant_type", "fb_exchange_token")
                            .queryParam("client_id", appId)
                            .queryParam("client_secret", appSecret)
                            .queryParam("fb_exchange_token", shortLivedToken)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (response != null && response.get("access_token") instanceof String) {
                System.out.println("✅ Convert user token dài hạn thành công");
                return (String) response.get("access_token");
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi convert user token dài hạn: " + e.getMessage());
        }
        return null;
    }

    // ✅ Lấy fbUserId từ token
    public String getUserIdFromToken(String userAccessToken) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/me")
                            .queryParam("fields", "id")
                            .queryParam("access_token", userAccessToken)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (response != null && response.containsKey("id")) {
                return (String) response.get("id");
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy fbUserId từ token: " + e.getMessage());
        }
        return null;
    }
}
