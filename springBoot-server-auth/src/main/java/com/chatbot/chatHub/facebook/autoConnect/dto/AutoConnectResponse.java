// src/main/java/com/chatbot/chatHub/facebook/autoConnect/dto/AutoConnectResponse.java

package com.chatbot.chatHub.facebook.autoConnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoConnectResponse {
    private boolean isSuccess;
    private String message;
    private List<String> connectedPages; // ✅ Đã sửa tên biến
    private List<String> reactivatedPages;
    private List<String> inactivePages;
    private List<ConnectionError> errors;
}