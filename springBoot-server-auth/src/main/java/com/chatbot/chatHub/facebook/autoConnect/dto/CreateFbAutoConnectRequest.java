// src/main/java/com/chatbot/webHub/facebook/autoConnect/dto/CreateFbAutoConnectRequest.java

package com.chatbot.webHub.facebook.autoConnect.dto;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class CreateFbAutoConnectRequest {
    @NotNull(message = "Connection list cannot be null")
    @Size(min = 1, message = "Connection list cannot be empty")
    @Valid
    private List<FbAutoConnectRequest> connections;
}