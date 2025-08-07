// src/main/java/com/chatbot/connections/models/FacebookConnection.java

package com.chatbot.connection.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "facebook_connection")
public class FacebookConnection {
    
    @Id
    private UUID id;
    private String botId;
    private String botName;
    private String ownerId;
    private String pageId;
    private String fanpageUrl;
    private String appSecret;
    private String pageAccessToken;
    private String urlCallback;
    private String verifyToken;
    private boolean isEnabled; // Trường mới
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt; // Trường mới
}