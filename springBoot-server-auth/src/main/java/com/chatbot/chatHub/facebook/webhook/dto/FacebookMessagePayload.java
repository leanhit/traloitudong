// src/main/java/com/chatbot/chatHub/facebook/webhook/dto/FacebookMessagePayload.java
package com.chatbot.chatHub.facebook.webhook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO để gửi tin nhắn văn bản đến Facebook Messenger API.
 */
@Data
public class FacebookMessagePayload {
    
    @JsonProperty("recipient")
    private Recipient recipient;
    
    @JsonProperty("message")
    private Message message;

    public FacebookMessagePayload(String recipientId, String messageText) {
        this.recipient = new Recipient(recipientId);
        this.message = new Message(messageText);
    }
    
    @Data
    public static class Recipient {
        @JsonProperty("id")
        private String id;

        public Recipient(String id) {
            this.id = id;
        }
    }

    @Data
    public static class Message {
        @JsonProperty("text")
        private String text;

        public Message(String text) {
            this.text = text;
        }
    }
}
