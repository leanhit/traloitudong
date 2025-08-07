// src/main/java/com/chatbot/facebook/dto/WebhookRequest.java

package com.chatbot.facebook.dto;

import java.util.List;
import lombok.Data;

@Data
public class WebhookRequest {
    private String object;
    private List<Entry> entry;

    @Data
    public static class Entry {
        private String id;
        private Long time;
        private List<Messaging> messaging;
    }

    @Data
    public static class Messaging {
        private Sender sender;
        private Recipient recipient;
        private Long timestamp;
        private Message message;
        
        @Data
        public static class Sender {
            private String id;
        }

        @Data
        public static class Recipient {
            private String id;
        }

        @Data
        public static class Message {
            private String mid;
            private String text;
        }
    }
}