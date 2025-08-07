package com.chatbot.botpress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BotpressResponse {
    @JsonProperty("botId")
    private String botId;
    
    @JsonProperty("message")
    private String message;
}