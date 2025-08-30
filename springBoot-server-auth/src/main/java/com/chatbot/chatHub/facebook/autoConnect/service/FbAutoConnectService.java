// src/main/java/com/chatbot/webHub/facebook/autoConnect/service/FbAutoConnectService.java

package com.chatbot.webHub.facebook.autoConnect.service;

import com.chatbot.webHub.facebook.autoConnect.dto.CreateFbAutoConnectRequest;
import com.chatbot.webHub.facebook.autoConnect.dto.FbAutoConnectRequest;
import com.chatbot.webHub.facebook.connection.repository.FacebookConnectionRepository;
import com.chatbot.webHub.facebook.connection.model.FacebookConnection;
import com.chatbot.chatHub.facebook.webhook.service.FacebookApiGraphService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FbAutoConnectService {

    private final FacebookConnectionRepository connectionRepository;
    private final FacebookApiGraphService facebookApiGraphService;
        
    @Value("${facebook.autoConnect.appSecret}")
    private String appSecret;

    public FbAutoConnectService(FacebookConnectionRepository connectionRepository, FacebookApiGraphService facebookApiGraphService) {
        this.connectionRepository = connectionRepository;
        this.facebookApiGraphService = facebookApiGraphService;
    }
    
    public List<String> createConnections(String ownerId, CreateFbAutoConnectRequest request) {
        // Lọc ra các kết nối mới, loại bỏ các kết nối đã tồn tại
        List<FacebookConnection> newConnections = request.getConnections().stream()
            .filter(connectionRequest -> connectionRepository.findByPageId(connectionRequest.getPageId()).isEmpty())
            .map(connectionRequest -> {
                FacebookConnection newConnection = new FacebookConnection();
                newConnection.setId(UUID.randomUUID());
                newConnection.setBotId(connectionRequest.getBotId());
                newConnection.setBotName(connectionRequest.getBotName());
                newConnection.setPageId(connectionRequest.getPageId());
                newConnection.setFanpageUrl(connectionRequest.getFanpageUrl());
                newConnection.setPageAccessToken(connectionRequest.getPageAccessToken());
                
                newConnection.setOwnerId(ownerId);
                newConnection.setCreatedAt(LocalDateTime.now());
                newConnection.setLastUpdatedAt(LocalDateTime.now());
                newConnection.setEnabled(connectionRequest.isEnabled());
                return newConnection;
            })
            .collect(Collectors.toList());

        // Nếu không có kết nối mới nào, trả về danh sách rỗng
        if (newConnections.isEmpty()) {
            return List.of();
        }

        // Lưu các kết nối mới vào database
        List<FacebookConnection> savedConnections = connectionRepository.saveAll(newConnections);

        // Gọi API của Facebook để đăng ký webhook cho mỗi kết nối mới
        for (FacebookConnection connection : savedConnections) {
            facebookApiGraphService.subscribePageToWebhook(connection.getPageId(), connection.getPageAccessToken());
        }

        return savedConnections.stream()
            .map(connection -> connection.getId().toString())
            .collect(Collectors.toList());
    }
}