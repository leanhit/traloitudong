// src/main/java/com/chatbot/connection/dto/UpdateFacebookConnectionRequest.java

package com.chatbot.connection.dto;

import lombok.Data;

@Data
public class UpdateFacebookConnectionRequest {
    private String botName;
    private String pageId;
    private String fanpageUrl;
    private String appSecret;
    private String pageAccessToken;
    private String verifyToken;
    private Boolean isEnabled;
}