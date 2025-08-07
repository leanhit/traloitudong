package com.chatbot.botpress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BotpressMessage {
    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private String text;

    // Có thể thêm các trường khác như "url" cho hình ảnh, "actions", v.v.
}