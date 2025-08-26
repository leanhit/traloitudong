// src/main/java/com/chatbot/chatHub/facebook/webhook/dto/WebhookRequest.java
package com.chatbot.chatHub.facebook.webhook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * DTO để ánh xạ payload từ Facebook Webhook.
 */
@Data
public class WebhookRequest {
    
    @JsonProperty("object")
    private String object;
    
    @JsonProperty("entry")
    private List<Entry> entry;

    @Data
    public static class Entry {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("messaging")
        private List<Messaging> messaging;
    }

    @Data
    public static class Messaging {
        @JsonProperty("sender")
        private Sender sender;
        
        @JsonProperty("recipient")
        private Recipient recipient;
        
        @JsonProperty("message")
        private Message message;
    }

    @Data
    public static class Sender {
        @JsonProperty("id")
        private String id;
    }

    @Data
    public static class Recipient {
        @JsonProperty("id")
        private String id;
    }

    @Data
    public static class Message {
        @JsonProperty("mid")
        private String mid;
        
        @JsonProperty("text")
        private String text;
    }
}
