// src/main/java/com/chatbot/chatHub/facebook/autoConnectClient/dto/CreateFbAutoConnectClientRequest.java

package com.chatbot.chatHub.facebook.autoConnectClient.dto;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class CreateFbAutoConnectClientRequest {
    @NotNull(message = "Connection list cannot be null")
    @Size(min = 1, message = "Connection list cannot be empty")
    @Valid
    private List<FbAutoConnectClientRequest> connections;
}