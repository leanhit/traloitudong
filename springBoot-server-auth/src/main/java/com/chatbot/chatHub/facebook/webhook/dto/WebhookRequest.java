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

        @JsonProperty("timestamp")
        private Long timestamp;

        @JsonProperty("message")
        private Message message;

        @JsonProperty("postback")
        private Postback postback;

        @JsonProperty("reaction")
        private Reaction reaction;

        @JsonProperty("read")
        private Read read;

        @JsonProperty("delivery")
        private Delivery delivery;
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

        @JsonProperty("attachments")
        private List<Attachment> attachments;

        @JsonProperty("quick_reply")
        private QuickReply quickReply;

        @JsonProperty("is_echo")
        private Boolean isEcho;   // 👈 thêm để nhận biết tin nhắn echo
    }

    @Data
    public static class QuickReply {
        @JsonProperty("payload")
        private String payload;
    }

    @Data
    public static class Attachment {
        @JsonProperty("type")
        private String type;

        @JsonProperty("payload")
        private Payload payload;
    }

    @Data
    public static class Payload {
        @JsonProperty("url")
        private String url;
    }

    @Data
    public static class Postback {
        @JsonProperty("title")
        private String title;

        @JsonProperty("payload")
        private String payload;
    }

    @Data
    public static class Reaction {
        @JsonProperty("action")
        private String action; // "react" hoặc "unreact"

        @JsonProperty("emoji")
        private String emoji;  // ví dụ 👍

        @JsonProperty("mid")
        private String mid;    // ID tin nhắn bị reaction
    }

    @Data
    public static class Read {
        @JsonProperty("watermark")
        private long watermark;
    }

    @Data
    public static class Delivery {
        @JsonProperty("mids")
        private List<String> mids;

        @JsonProperty("watermark")
        private long watermark;
    }
}
