package com.chatbot.botmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BotNameUpdateRequest {
    @NotBlank(message = "Tên bot mới không được để trống")
    @Size(min = 3, max = 50, message = "Tên bot phải từ 3 đến 50 ký tự")
    private String newBotName;
}