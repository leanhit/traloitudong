package com.chatbot.botpress.service;

import com.chatbot.botpress.dto.BotpressCreateBotRequest;
import com.chatbot.botpress.dto.BotpressUpdateBotRequest;
import com.chatbot.botpress.dto.BotpressResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class BotpressApiService {

    @Value("${botpress.api.url}")
    private String botpressApiUrl;

    @Value("${botpress.api.admin-token}")
    private String botpressAdminToken;

    private final RestTemplate restTemplate;

    public BotpressApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(botpressAdminToken);
        return headers;
    }

    public String createBot(String botId, String botName) {
        String url = String.format("%s/api/v1/admin/bots", botpressApiUrl);
        HttpHeaders headers = createHeaders();
        
        BotpressCreateBotRequest request = new BotpressCreateBotRequest(botId, botName);
        HttpEntity<BotpressCreateBotRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<BotpressResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, BotpressResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                System.out.println("Bot created on Botpress successfully.");
                return botId;
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Lỗi HTTP khi tạo bot trên Botpress: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi tạo bot trên Botpress: " + e.getMessage());
        }
        return null;
    }

    public boolean updateBotName(String botId, String newBotName) {
        String url = String.format("%s/api/v1/admin/bots/%s", botpressApiUrl, botId);
        HttpHeaders headers = createHeaders();
        
        BotpressUpdateBotRequest request = new BotpressUpdateBotRequest(newBotName);
        HttpEntity<BotpressUpdateBotRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<BotpressResponse> response = restTemplate.exchange(url, HttpMethod.PUT, entity, BotpressResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Bot name updated on Botpress successfully.");
                return true;
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Lỗi HTTP khi cập nhật tên bot trên Botpress: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi cập nhật tên bot trên Botpress: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteBot(String botId) {
        String url = String.format("%s/api/v1/admin/bots/%s", botpressApiUrl, botId);
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
            System.out.println("Bot deleted on Botpress successfully.");
            return true;
        } catch (HttpClientErrorException e) {
            System.err.println("Lỗi HTTP khi xóa bot trên Botpress: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi xóa bot trên Botpress: " + e.getMessage());
        }
        return false;
    }
}