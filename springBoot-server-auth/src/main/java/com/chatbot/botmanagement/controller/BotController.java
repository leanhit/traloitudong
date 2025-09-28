package com.chatbot.botmanagement.controller;

import com.chatbot.botmanagement.dto.BotNameUpdateRequest;
import com.chatbot.botmanagement.dto.CreateBotRequest;
import com.chatbot.botmanagement.dto.BotResponse;
import com.chatbot.botmanagement.model.Bot;
import com.chatbot.botmanagement.service.BotService;
import com.chatbot.botpress.service.BotpressApiService;
import com.chatbot.botmanagement.dto.WorkspaceResponse;
import com.chatbot.botmanagement.dto.BotInfoResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bots")
public class BotController {

    private final BotService botService;
    private final BotpressApiService botpressApiService;

    public BotController(BotService botService, BotpressApiService botpressApiService) {
        this.botService = botService;
        this.botpressApiService = botpressApiService;
    }

    // ================== CRUD DB + Botpress ==================

    @PostMapping
    public ResponseEntity<BotResponse> createBot(
            @Valid @RequestBody CreateBotRequest request,
            Principal principal) {

        String ownerId = principal.getName();
        String botId = botService.createBot(ownerId, request.getBotName(), request.getBotDescription());

        if (botId != null) {
            return ResponseEntity.ok(new BotResponse(
                    botId,
                    request.getBotName(),
                    ownerId,
                    "Bot created successfully.",
                    request.getBotDescription()
            ));
        } else {
            return ResponseEntity.badRequest()
                    .body(new BotResponse(null, null, null, "Failed to create bot.", null));
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

        System.out.println("➡️ [DEBUG] API get all bot invoked by user: " + principal.getName());

        String ownerId = principal.getName();
        Page<Bot> botPage = botService.getBotsByOwnerId(ownerId, pageable);

        Page<BotResponse> botResponsePage = botPage.map(bot ->
                new BotResponse(bot.getBotId(), bot.getBotName(), bot.getOwnerId(), "Success", bot.getBotDescription())
        );

        return ResponseEntity.ok(botResponsePage);
    }

    // ================== Botpress API ==================

    @GetMapping("/botpress/workspaces")
    public ResponseEntity<List<WorkspaceResponse>> getWorkspaces() {
        System.out.println("➡️ [DEBUG] API get all bot invoked by user: ");
        return ResponseEntity.ok(botpressApiService.getWorkspaces());
    }
     // Lấy danh sách tất cả bots trong workspace
    @GetMapping("/botpress")
    public ResponseEntity<List<BotInfoResponse>> getAllBots() {
        List<BotInfoResponse> bots = botpressApiService.getAllBots();        
        System.out.println("➡️ [DEBUG] API get all bot invoked by user: "+ bots);
        return ResponseEntity.ok(bots);
    }

    // Lấy thông tin chi tiết 1 bot theo botId
    @GetMapping("/botpress/{botId}")
    public ResponseEntity<BotInfoResponse> getBotInfo(@PathVariable String botId) {        
        System.out.println("➡️ [DEBUG] API get bot info of: " + botId);
        BotInfoResponse botInfo = botpressApiService.getBotInfo(botId);
        if (botInfo != null) {
            return ResponseEntity.ok(botInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Archive bot
    @PostMapping("/botpress/{botId}/archive")
    public ResponseEntity<Void> archiveBot(@PathVariable String botId) {
        System.out.println("➡️ [DEBUG] API archive: " + botId);
        boolean success = botpressApiService.archiveBot(botId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    // Unarchive bot
    @PostMapping("/botpress/{botId}/unarchive")
    public ResponseEntity<Void> unarchiveBot(@PathVariable String botId) {
        System.out.println("➡️ [DEBUG] API unarchive: "+ botId);
        boolean success = botpressApiService.unarchiveBot(botId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
