package com.chatbot.botmanagement.controller;

import com.chatbot.botmanagement.dto.BotNameUpdateRequest;
import com.chatbot.botmanagement.dto.CreateBotRequest;
import com.chatbot.botmanagement.dto.BotResponse;
import com.chatbot.botmanagement.model.Bot;
import com.chatbot.botmanagement.service.BotService;
import com.chatbot.botmanagement.enums.BotEnums;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bots")
public class BotController {

    private final BotService botService;

    public BotController(BotService botService) {
        this.botService = botService;
    }

    @PostMapping
    public ResponseEntity<BotResponse> createBot(
            @Valid @RequestBody CreateBotRequest request,
            Principal principal) {
        
        String ownerId = principal.getName();
        String botId = botService.createBot(ownerId, request.getBotName(), request.getBotDescription());
        
        if (botId != null) {
            return ResponseEntity.ok(new BotResponse(botId, request.getBotName(), ownerId, "Bot created successfully.", request.getBotDescription()));
        } else {
            return ResponseEntity.badRequest().body(new BotResponse(null, null, null, "Failed to create bot.", null));
        }
    }

    @PutMapping("/{botId}")
    public ResponseEntity<String> updateBotName(
            @PathVariable String botId,
            @Valid @RequestBody BotNameUpdateRequest request,
            Principal principal) {
        
        String ownerId = principal.getName();
        boolean isSuccess = botService.updateBotName(botId, request.getNewBotName(), ownerId);
        
        if (isSuccess) {
            return ResponseEntity.ok("Bot name updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update bot name. Check bot ownership.");
        }
    }
    
    @DeleteMapping("/{botId}")
    public ResponseEntity<String> deleteBot(
            @PathVariable String botId,
            Principal principal) {

        String ownerId = principal.getName();
        boolean isSuccess = botService.deleteBot(botId, ownerId);
        
        if (isSuccess) {
            return ResponseEntity.ok("Bot '" + botId + "' deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete bot. Check bot ownership.");
        }
    }

    @GetMapping
    public ResponseEntity<Page<BotResponse>> getBots(
            Principal principal,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        
        String ownerId = principal.getName();
        
        // Gọi service với đối tượng pageable
        Page<Bot> botPage = botService.getBotsByOwnerId(ownerId, pageable);
        
        // Chuyển đổi Page<Bot> thành Page<BotResponse>
        Page<BotResponse> botResponsePage = botPage.map(bot -> 
            new BotResponse(bot.getBotId(), bot.getBotName(), bot.getOwnerId(), "Success", bot.getBotDescription()));
        
        return ResponseEntity.ok(botResponsePage);
    }

}
