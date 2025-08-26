// src/main/java/com/chatbot/chatHub/facebook/webhook/dto/SimpleBotpressMessage.java
package com.chatbot.chatHub.facebook.webhook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO để gửi tin nhắn văn bản đơn giản đến Botpress.
 */
@Data
public class SimpleBotpressMessage {

    @JsonProperty("type")
    private final String type = "text";

    @JsonProperty("text")
    private String text;

    public SimpleBotpressMessage(String text) {
        this.text = text;
    }
}
