// src/main/java/com/chatbot/chatHub/facebook/webhook/controller/BotpressWebhookController.java
package com.chatbot.chatHub.facebook.webhook.controller;

import com.chatbot.chatHub.facebook.webhook.dto.BotpressReplyRequest;
import com.chatbot.chatHub.facebook.webhook.service.FacebookMessengerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller nhận các yêu cầu từ Botpress.
 */
@RestController
@RequestMapping("/webhooks/botpress/facebook")
public class BotpressWebhookController {
    
    private final FacebookMessengerService facebookMessengerService;

    public BotpressWebhookController(FacebookMessengerService facebookMessengerService) {
        this.facebookMessengerService = facebookMessengerService;
    }

    @PostMapping("/{pageId}")
    public ResponseEntity<Void> handleBotpressReply(@PathVariable String pageId, @RequestBody BotpressReplyRequest request) {
        System.out.println("Received reply from Botpress for page " + pageId + " to recipient " + request.getRecipientId());
        
        facebookMessengerService.sendMessageToUser(
            pageId, 
            request.getRecipientId(), 
            request.getPayload().getText()
        );
        
        return ResponseEntity.ok().build();
    }
}
