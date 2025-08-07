// src/main/java/com/chatbot/facebook/controller/FacebookWebhookController.java

package com.chatbot.facebook.controller;

import com.chatbot.facebook.dto.WebhookRequest;
import com.chatbot.facebook.service.FacebookWebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook/facebook/botpress")
public class FacebookWebhookController {

    private final FacebookWebhookService facebookWebhookService;

    public FacebookWebhookController(FacebookWebhookService facebookWebhookService) {
        this.facebookWebhookService = facebookWebhookService;
    }

    // Endpoint cho xác thực webhook của Facebook
    @GetMapping
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode,
                                                @RequestParam("hub.challenge") String challenge,
                                                @RequestParam("hub.verify_token") String verifyToken) {
        // Logic xác thực token sẽ ở trong service
        if (facebookWebhookService.verifyWebhook(mode, challenge, verifyToken)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.badRequest().body("Verification failed.");
        }
    }

    // Endpoint để nhận các sự kiện tin nhắn từ Facebook
    @PostMapping
    public ResponseEntity<Void> handleWebhookEvent(@RequestBody WebhookRequest request) {
        facebookWebhookService.handleWebhookEvent(request);
        return ResponseEntity.ok().build();
    }
}