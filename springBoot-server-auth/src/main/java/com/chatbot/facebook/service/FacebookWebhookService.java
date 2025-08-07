// src/main/java/com/chatbot/facebook/service/FacebookWebhookService.java

package com.chatbot.facebook.service;

import com.chatbot.connection.model.FacebookConnection;
import com.chatbot.connection.repository.FacebookConnectionRepository;
import com.chatbot.facebook.dto.WebhookRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FacebookWebhookService {

    private final FacebookConnectionRepository connectionRepository;

    public FacebookWebhookService(FacebookConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    // Logic xác thực webhook
    public boolean verifyWebhook(String mode, String challenge, String verifyToken) {
        if ("subscribe".equals(mode)) {
            return connectionRepository.findByVerifyToken(verifyToken).isPresent();
        }
        return false;
    }

    // Logic xử lý sự kiện webhook và chuyển tiếp tin nhắn
    public void handleWebhookEvent(WebhookRequest request) {
        if ("page".equals(request.getObject())) {
            for (WebhookRequest.Entry entry : request.getEntry()) {
                if (entry.getMessaging() != null) {
                    for (WebhookRequest.Messaging messaging : entry.getMessaging()) {
                        String pageId = messaging.getRecipient().getId();
                        String senderId = messaging.getSender().getId();
                        
                        // Tìm kết nối trong database bằng pageId
                        Optional<FacebookConnection> connectionOpt = connectionRepository.findByPageId(pageId);
                        
                        connectionOpt.ifPresent(connection -> {
                            // Chỉ xử lý tin nhắn nếu kết nối được bật
                            if (connection.isEnabled() && messaging.getMessage() != null) {
                                String messageText = messaging.getMessage().getText();
                                
                                if (messageText != null) {
                                    System.out.println("Received message for bot " + connection.getBotId() + " from sender " + senderId + ": " + messageText);
                                    // TODO: Thêm logic để gửi tin nhắn đến Botpress
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}