// src/main/java/com/chatbot/webHub/facebook/autoConnect/dto/FbAutoConnectRequest.java

package com.chatbot.chatHub.facebook.autoConnect.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CreateFbAutoConnectRequest {
    @NotBlank(message = "botId cannot be blank")
    private String botId;
    @NotBlank(message = "userAccessToken cannot be blank")
    private String userAccessToken;
}