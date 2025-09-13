// /src/main/java/com/chatbot/botpress/dto/BotpressLoginRequest.java
package com.chatbot.botmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotpressLoginRequest {
    private String email;
    private String password;
}