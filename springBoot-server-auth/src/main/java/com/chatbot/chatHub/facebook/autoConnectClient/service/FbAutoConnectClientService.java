// src/main/java/com/chatbot/chatHub/facebook/autoConnectClient/service/FbAutoConnectClientService.java

package com.chatbot.chatHub.facebook.autoConnectClient.service;

import com.chatbot.chatHub.facebook.autoConnectClient.dto.CreateFbAutoConnectClientRequest;
import com.chatbot.chatHub.facebook.autoConnectClient.dto.FbAutoConnectClientRequest;
import com.chatbot.chatHub.facebook.connection.model.FacebookConnection;
import com.chatbot.chatHub.facebook.connection.repository.FacebookConnectionRepository;
import com.chatbot.chatHub.facebook.webhook.service.FacebookApiGraphService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FbAutoConnectClientService {

    private final FacebookConnectionRepository connectionRepository;
    private final FacebookApiGraphService facebookApiGraphService;

    public FbAutoConnectClientService(FacebookConnectionRepository connectionRepository,
                                      FacebookApiGraphService facebookApiGraphService) {
        this.connectionRepository = connectionRepository;
        this.facebookApiGraphService = facebookApiGraphService;
    }

    /**
     * Tạo hoặc cập nhật connection cho các fanpage
     *
     * @param ownerId webUserId (từ token)
     * @param request danh sách connection cần auto connect
     * @return danh sách id của connection đã xử lý
     */
    public List<String> createConnections(String ownerId, CreateFbAutoConnectClientRequest request) {
        System.out.println("🔹 Bắt đầu xử lý auto connect cho ownerId=" + ownerId);

        List<FacebookConnection> existingConnections = connectionRepository.findByOwnerId(ownerId);
        Map<String, FacebookConnection> pageIdToConnection = existingConnections.stream()
                .collect(Collectors.toMap(FacebookConnection::getPageId, c -> c));

        List<FacebookConnection> connectionsToSave = new ArrayList<>();
        List<String> resultIds = new ArrayList<>();

        for (FbAutoConnectClientRequest connectionRequest : request.getConnections()) {
            String pageId = connectionRequest.getPageId();
            String botName = connectionRequest.getBotName();
            String pageToken = connectionRequest.getPageAccessToken();

            if (pageIdToConnection.containsKey(pageId)) {
                FacebookConnection conn = pageIdToConnection.get(pageId);

                // luôn cập nhật token + botName mới nhất
                conn.setPageAccessToken(pageToken);
                conn.setBotName(botName);

                if (!conn.isActive()) {
                    conn.setActive(true);
                    conn.setEnabled(true);
                    System.out.println("♻️ Kích hoạt lại connection cho pageId=" + pageId);
                } else {
                    System.out.println("➡️ Connection đã tồn tại, cập nhật lại: pageId=" + pageId);
                }

                conn.setLastUpdatedAt(LocalDateTime.now());
                connectionsToSave.add(conn);
                resultIds.add(conn.getId().toString());
            } else {
                // tạo mới
                FacebookConnection conn = new FacebookConnection();
                conn.setId(UUID.randomUUID());
                conn.setBotId(connectionRequest.getBotId());
                conn.setBotName(botName);
                conn.setPageId(pageId);
                conn.setFanpageUrl(connectionRequest.getFanpageUrl());
                conn.setPageAccessToken(pageToken);
                conn.setOwnerId(ownerId);
                conn.setActive(true);
                conn.setEnabled(connectionRequest.isEnabled());
                conn.setCreatedAt(LocalDateTime.now());
                conn.setLastUpdatedAt(LocalDateTime.now());

                connectionsToSave.add(conn);
                resultIds.add(conn.getId().toString());
                System.out.println("➡️ Tạo mới connection cho pageId=" + pageId);
            }
        }

        if (!connectionsToSave.isEmpty()) {
            connectionRepository.saveAll(connectionsToSave);
            System.out.println("✅ Đã lưu " + connectionsToSave.size() + " connections");
        }

        // đăng ký webhook
        for (FacebookConnection conn : connectionsToSave) {
            try {
                if (conn.isActive()) {
                    facebookApiGraphService.subscribePageToWebhook(conn.getPageId(), conn.getPageAccessToken());
                    System.out.println("✅ Subscribed webhook cho pageId=" + conn.getPageId());
                }
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi đăng ký webhook cho pageId=" + conn.getPageId() + ": " + e.getMessage());
            }
        }

        return resultIds;
    }
}
