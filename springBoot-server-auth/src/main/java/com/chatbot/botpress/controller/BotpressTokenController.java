package com.chatbot.botpress.controller;

import com.chatbot.botmanagement.service.BotpressAuthService;
import com.chatbot.botpress.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class BotpressTokenController {

    private final BotpressAuthService authService;

    public BotpressTokenController(BotpressAuthService authService) {
        this.authService = authService;
    }

    /**
     * Triggers a new Botpress admin token to be fetched and returns it.
     * This endpoint should be protected to be used only by administrators.
     * @return ResponseEntity containing the new token.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshBotpressToken() {
        String newToken = authService.getNewAdminToken();

        if (newToken != null) {
            return ResponseEntity.ok(new TokenResponse(newToken, "Botpress token refreshed successfully."));
        } else {
            return ResponseEntity.internalServerError().body(new TokenResponse(null, "Failed to refresh Botpress token. Check server logs for details."));
        }
    }
}