// src/main/java/com/chatbot/chatHub/facebook/webhook/dto/BotpressReplyRequest.java
package com.chatbot.chatHub.facebook.webhook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO để ánh xạ payload từ Botpress khi nhận tin nhắn phản hồi.
 */
@Data
public class BotpressReplyRequest {

    @JsonProperty("recipientId")
    private String recipientId;

    @JsonProperty("payload")
    private ReplyPayload payload;
    
    @Data
    public static class ReplyPayload {
        @JsonProperty("text")
        private String text;
    }
}
