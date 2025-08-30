// src/main/java/com/chatbot/connection/dto/UpdateFacebookConnectionRequest.java

package com.chatbot.webHub.facebook.connection.dto;

import lombok.Data;

@Data
public class UpdateFacebookConnectionRequest {
    private String botName;
    private String botId;
    private String pageId;
    private String fanpageUrl;
    private String pageAccessToken;
    private Boolean isEnabled;
}