package com.chatbot.botpress.service;

import com.chatbot.botpress.dto.BotpressCreateBotRequest;
import com.chatbot.botpress.dto.BotpressUpdateBotRequest;
import com.chatbot.botpress.dto.BotpressResponse;
import com.chatbot.botpress.dto.BotpressTemplateRequest;
import com.chatbot.botpress.dto.BotpressResponseInfo;
import com.chatbot.botmanagement.dto.WorkspaceResponse;
import com.chatbot.botmanagement.dto.BotInfoResponse  ;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BotpressApiService {

    @Value("${botpress.api.url}")
    private String botpressApiUrl;

    @Value("${botpress.api.admin-token}")
    private String botpressAdminToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String WORKSPACE_ID = "default";

    public BotpressApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(botpressAdminToken);
        headers.add("X-BP-Workspace", WORKSPACE_ID);
        return headers;
    }

    /** DTO nhỏ gọn để trả về danh sách bot */
    public static class BotInfo {
        private String id;
        private String name;

        public BotInfo(String id, String name) {
            this.id = id;
            this.name = name;
        }
        public String getId() { return id; }
        public String getName() { return name; }
    }

    // ======================== BOT CRUD ========================

    public BotpressResponseInfo createBot(String botId, String botName) {
        String url = String.format("%s/api/v1/admin/bots", botpressApiUrl);
        HttpHeaders headers = createHeaders();

        BotpressTemplateRequest template = new BotpressTemplateRequest("builtin", "welcome-bot");
        BotpressCreateBotRequest request = new BotpressCreateBotRequest(botId, botName, template);
        HttpEntity<BotpressCreateBotRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return new BotpressResponseInfo(response.getStatusCode(), response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Botpress API error (create bot): {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return new BotpressResponseInfo(e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unexpected error (create bot): {}", e.getMessage(), e);
            return new BotpressResponseInfo(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage());
        }
    }

    public boolean updateBotName(String botId, String newBotName) {
        String url = String.format("%s/api/v1/admin/bots/%s", botpressApiUrl, botId);
        HttpHeaders headers = createHeaders();

        BotpressUpdateBotRequest request = new BotpressUpdateBotRequest(newBotName);
        HttpEntity<BotpressUpdateBotRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<BotpressResponse> response =
                    restTemplate.exchange(url, HttpMethod.PUT, entity, BotpressResponse.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException e) {
            log.error("Botpress API error (update bot): {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unexpected error (update bot): {}", e.getMessage(), e);
        }
        return false;
    }

    public boolean deleteBot(String botId) {
        String url = String.format("%s/api/v1/admin/bots/%s", botpressApiUrl, botId);
        System.out.println("🗑️ [DEBUG] Delete bot API invoked: " + url);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    new HttpEntity<>(createHeaders()),
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ [DEBUG] Bot " + botId + " deleted successfully.");
                return true;
            } else {
                System.out.println("⚠️ [DEBUG] Failed to delete bot. Status: " + response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ======================== WORKSPACES ========================
    public List<WorkspaceResponse> getWorkspaces() {
        String url = String.format("%s/api/v1/admin/workspaces", botpressApiUrl);
        System.out.println("➡️ [DEBUG] API get all bot invoked by user: " + url);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(createHeaders()),
                String.class
        );

        System.out.println("📥 [DEBUG] Raw response body: " + response.getBody());

        try {
            WorkspaceResponse[] workspaces = mapper.readValue(response.getBody(), WorkspaceResponse[].class);
            System.out.println("✅ [DEBUG] Parsed workspaces: " + Arrays.toString(workspaces));
            return Arrays.asList(workspaces);
        } catch (Exception e) {
            System.out.println("📦 [DEBUG] Error: " + e);
            return Collections.emptyList();
        }
    }

    // Lấy tất cả bots
    public List<BotInfoResponse> getAllBots() {
        String url = String.format("%s/api/v1/admin/bots", botpressApiUrl);
        System.out.println("📡 [DEBUG] Get all bots API invoked: " + url);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders()),
                    String.class
            );

            System.out.println("📥 [DEBUG] Raw bots response: " + response.getBody());

            BotInfoResponse[] bots = mapper.readValue(response.getBody(), BotInfoResponse[].class);
            return Arrays.asList(bots);

        } catch (Exception e) {
            System.out.println("📦 [DEBUG] Error: " + e);
            return List.of();
        }
    }

    // Lấy thông tin chi tiết một bot
    public BotInfoResponse getBotInfo(String botId) {
        String url = String.format("%s/api/v1/admin/bots/%s", botpressApiUrl, botId);
        System.out.println("ℹ️ [DEBUG] Get bot info API invoked: " + url);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders()),
                    String.class
            );

            return mapper.readValue(response.getBody(), BotInfoResponse.class);

        } catch (Exception e) {
            System.out.println("📦 [DEBUG] Error: " + e);
            return null;
        }
    }

    // Archive bot
    public boolean archiveBot(String botId) {
        String url = String.format("%s/api/v1/admin/bots/%s/archive", botpressApiUrl, botId);
        System.out.println("📦 [DEBUG] Archive bot API invoked: " + url);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(createHeaders()),
                    Void.class
            );
            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            System.out.println("📦 [DEBUG] Error: " + e);
            return false;
        }
    }

    // Unarchive bot
    public boolean unarchiveBot(String botId) {
        String url = String.format("%s/api/v1/admin/bots/%s/unarchive", botpressApiUrl, botId);
        System.out.println("📦 [DEBUG] Unarchive bot API invoked: " + url);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(createHeaders()),
                    Void.class
            );
            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            System.out.println("📦 [DEBUG] Error: " + e);
            return false;
        }
    }
}
