package com.chatbot.botpress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotpressCreateBotRequest {
    @JsonProperty("id")
    private String botId;
    
    @JsonProperty("name")
    private String botName;
}