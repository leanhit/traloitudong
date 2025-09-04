package com.chatbot.chatHub.facebook.autoConnect.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionError {
    private String pageName;
    private String errorMessage;
}