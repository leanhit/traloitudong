// src/main/java/com/chatbot/connection/dto/CreateFacebookConnectionRequest.java

package com.chatbot.connection.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class CreateFacebookConnectionRequest {
    private String botId;
    private String botName;
    private String pageId;
    private String fanpageUrl;
    private String appSecret;
    private String pageAccessToken;
    private String verifyToken;
    private String urlCallback;
    private boolean isEnabled; // Thêm trường này để tạo kết nối ban đầu
}