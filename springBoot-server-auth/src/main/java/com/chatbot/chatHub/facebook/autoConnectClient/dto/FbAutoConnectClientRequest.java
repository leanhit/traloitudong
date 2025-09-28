// src/main/java/com/chatbot/chatHub/facebook/autoConnectClient/dto/FbAutoConnectRequest.java

package com.chatbot.chatHub.facebook.autoConnectClient.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class FbAutoConnectClientRequest {
    @NotBlank(message = "botId cannot be blank")
    private String botId;
    @NotBlank(message = "botName cannot be blank")
    private String botName;
    @NotBlank(message = "pageId cannot be blank")
    private String pageId;
    private String fanpageUrl;
    @NotBlank(message = "pageAccessToken cannot be blank")
    private String pageAccessToken;
    private boolean isEnabled;
}