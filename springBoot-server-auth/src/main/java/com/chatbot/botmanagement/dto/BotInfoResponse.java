package com.chatbot.botmanagement.dto;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class BotInfoResponse {
    private String id;
    private String name;
    private String description;
    private String defaultLanguage;
    private List<String> languages;
    private boolean disabled;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}
