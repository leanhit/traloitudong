// /src/main/java/com/chatbot/botpress/dto/TokenResponse.java
package com.chatbot.botpress.dto;

import lombok.AllArgsConstructor; // Thêm dòng này
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // Thêm annotation này
public class TokenResponse {
    private String token;
    private String message;
}