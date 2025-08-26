// src/main/java/com/chatbot/connection/service/FacebookConnectionService.java

package com.chatbot.webHub.facebook.connection.service;

import com.chatbot.webHub.facebook.connection.dto.CreateFacebookConnectionRequest;
import com.chatbot.webHub.facebook.connection.dto.FacebookConnectionResponse;
import com.chatbot.webHub.facebook.connection.dto.UpdateFacebookConnectionRequest;
import com.chatbot.webHub.facebook.connection.model.FacebookConnection;
import com.chatbot.webHub.facebook.connection.repository.FacebookConnectionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FacebookConnectionService {

    private final FacebookConnectionRepository connectionRepository;

    public FacebookConnectionService(FacebookConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public String createConnection(String ownerId, CreateFacebookConnectionRequest request) {
        // TODO: Thêm logic kiểm tra xem botId có thuộc về ownerId không.
        FacebookConnection newConnection = new FacebookConnection();
        newConnection.setId(UUID.randomUUID());
        newConnection.setBotId(request.getBotId());
        newConnection.setBotName(request.getBotName());
        newConnection.setPageId(request.getPageId());
        newConnection.setFanpageUrl(request.getFanpageUrl());
        newConnection.setAppSecret(request.getAppSecret());
        newConnection.setPageAccessToken(request.getPageAccessToken());
        newConnection.setUrlCallback(request.getUrlCallback());
        newConnection.setVerifyToken(request.getVerifyToken());
        newConnection.setOwnerId(ownerId);
        newConnection.setCreatedAt(LocalDateTime.now());
        newConnection.setLastUpdatedAt(LocalDateTime.now());
        newConnection.setEnabled(request.isEnabled());
        connectionRepository.save(newConnection);
        return newConnection.getId().toString();
    }

    public List<FacebookConnectionResponse> getConnectionsByOwnerId(String ownerId) {
        List<FacebookConnection> connections = connectionRepository.findByOwnerId(ownerId);
        return connections.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<FacebookConnectionResponse> getConnectionsByOwnerId(String ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FacebookConnection> connectionsPage = connectionRepository.findByOwnerId(ownerId, pageable);
        List<FacebookConnectionResponse> dtoList = connectionsPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, connectionsPage.getTotalElements());
    }

    private FacebookConnectionResponse convertToDto(FacebookConnection connection) {
        FacebookConnectionResponse dto = new FacebookConnectionResponse();
        dto.setId(connection.getId());
        dto.setBotId(connection.getBotId());
        dto.setBotName(connection.getBotName());
        dto.setPageId(connection.getPageId());
        dto.setAppSecret(connection.getAppSecret());
        dto.setPageAccessToken(connection.getPageAccessToken());
        dto.setVerifyToken(connection.getVerifyToken());
        dto.setFanpageUrl(connection.getFanpageUrl());
        dto.setUrlCallback(connection.getUrlCallback());
        dto.setEnabled(connection.isEnabled());
        dto.setCreatedAt(connection.getCreatedAt());
        dto.setLastUpdatedAt(connection.getLastUpdatedAt());
        return dto;
    }

    public void updateConnection(UUID connectionId, String ownerId, UpdateFacebookConnectionRequest request) {
        FacebookConnection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Connection not found."));
        if (!connection.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Access denied.");
        }
        if (request.getBotName() != null) {
            connection.setBotName(request.getBotName());
        }
        if (request.getBotId() != null) {
            connection.setBotId(request.getBotId());
        }
        if (request.getPageAccessToken() != null) {
            connection.setPageAccessToken(request.getPageAccessToken());
        }
        if (request.getFanpageUrl() != null) {
            connection.setFanpageUrl(request.getFanpageUrl());
        }
        if (request.getVerifyToken() != null) {
            connection.setVerifyToken(request.getVerifyToken());
        }
        if (request.getAppSecret() != null) {
            connection.setAppSecret(request.getAppSecret());
        }
        if (request.getPageId() != null) {
            connection.setPageId(request.getPageId());
        }
        if (request.getIsEnabled() != null) {
            connection.setEnabled(request.getIsEnabled());
        }
        connection.setLastUpdatedAt(LocalDateTime.now());
        connectionRepository.save(connection);
    }

    public void deleteConnection(String id) {
        UUID connectionId = UUID.fromString(id);
        connectionRepository.deleteById(connectionId);
    }
}
