// src/main/java/com/chatbot/connections/models/FacebookConnection.java

package com.chatbot.chatHub.facebook.connection.model;

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
    private String pageAccessToken;
    private String fbUserId;
    private boolean isEnabled; // Trường mới
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt; // Trường mới
}