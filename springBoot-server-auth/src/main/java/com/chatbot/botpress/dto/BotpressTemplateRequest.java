package com.chatbot.botpress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotpressTemplateRequest {
    @JsonProperty("moduleId")
    private String moduleId;

    @JsonProperty("templateId")
    private String templateId;
}