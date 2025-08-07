package com.chatbot.botmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotResponse {
    private String botId;
    private String botName;
    private String ownerId;
    private String message;
}