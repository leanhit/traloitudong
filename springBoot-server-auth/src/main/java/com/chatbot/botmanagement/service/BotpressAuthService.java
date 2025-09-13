// /src/main/java/com/chatbot/botpress/service/BotpressAuthService.java
package com.chatbot.botmanagement.service;

import com.chatbot.botmanagement.dto.BotpressLoginRequest;
import com.chatbot.botmanagement.dto.BotpressLoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BotpressAuthService {

    @Value("${botpress.api.url}")
    private String botpressApiUrl;

    @Value("${botpress.api.username}")
    private String botpressApiUsername;

    @Value("${botpress.api.password}")
    private String botpressApiPassword;

    private final RestTemplate restTemplate;

    public BotpressAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getNewAdminToken() {
        // This is the correct endpoint for Botpress v13 admin login
        String url = String.format("%s/api/v1/admin/auth/login/basic/default", botpressApiUrl);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BotpressLoginRequest loginRequest = new BotpressLoginRequest(botpressApiUsername, botpressApiPassword);
        HttpEntity<BotpressLoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        try {
            ResponseEntity<BotpressLoginResponse> response = restTemplate.postForEntity(url, entity, BotpressLoginResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getToken();
            }
        } catch (Exception e) {
            System.err.println("Failed to get Botpress admin token: " + e.getMessage());
        }
        return null;
    }
}