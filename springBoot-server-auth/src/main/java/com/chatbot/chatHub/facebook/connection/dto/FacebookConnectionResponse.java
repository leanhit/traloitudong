// src/main/java/com/chatbot/connection/dto/FacebookConnectionResponse.java

package com.chatbot.webHub.facebook.connection.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FacebookConnectionResponse {
    private UUID id;
    private String botId;
    private String botName;
    private String pageId;
    private String fanpageUrl;
    private String appSecret;
    private String pageAccessToken;
    private String urlCallback;
    private String verifyToken;
    private boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
}