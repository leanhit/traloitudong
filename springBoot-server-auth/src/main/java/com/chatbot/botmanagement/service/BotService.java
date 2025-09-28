package com.chatbot.botmanagement.service;

import com.chatbot.botmanagement.model.Bot;
import com.chatbot.botmanagement.repository.BotRepository;
import com.chatbot.botpress.dto.BotpressResponseInfo;
import com.chatbot.botpress.service.BotpressApiService;
import com.chatbot.botpress.service.UserIdMappingService;
import com.chatbot.utils.StringUtils;
import com.chatbot.botmanagement.enums.BotEnums;
import com.chatbot.botmanagement.dto.WorkspaceResponse;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BotService {

    private static final Logger logger = LoggerFactory.getLogger(BotService.class);

    private final BotRepository botRepository;
    private final BotpressApiService botpressApiService;
    private final UserIdMappingService userIdMappingService;

    public BotService(BotRepository botRepository, BotpressApiService botpressApiService, UserIdMappingService userIdMappingService) {
        this.botRepository = botRepository;
        this.botpressApiService = botpressApiService;
        this.userIdMappingService = userIdMappingService;
    }

    // public String createBot1(String ownerId, String botName, String botDescription) {
    //     Long internalId = userIdMappingService.getOrCreateInternalId(ownerId);

    //     // Chuẩn hóa tên bot trước khi tạo botId
    //     String cleanBotName = StringUtils.slugify(botName);

    //     String botId = String.format("bot-%d-%s", internalId, cleanBotName);

    //     try {
    //         String createdBotId = botpressApiService.createBot(botId, botName);
    //         if (createdBotId != null) {
    //             Bot newBot = new Bot();
    //             newBot.setBotId(createdBotId);
    //             newBot.setBotName(botName);
    //             newBot.setOwnerId(ownerId);
    //             newBot.setBotDescription(botDescription);
    //             botRepository.save(newBot);
    //             logger.info("Bot and database entry created successfully for owner: {}", ownerId);
    //         }
    //         return createdBotId;
    //     } catch (Exception e) {
    //         logger.error("Failed to create bot for owner {}: {}", ownerId, e.getMessage(), e);
    //         return null; // Hoặc ném ra ngoại lệ
    //     }
    // }

    public String createBot(String ownerId, String botName, String botDescription) {
        Long internalId = userIdMappingService.getOrCreateInternalId(ownerId);
        String cleanBotName = StringUtils.slugify(botName);
        String botId = String.format("bot-%d-%s", internalId, cleanBotName);

        logger.info("Đang tạo bot với ID: {}", botId);

        BotpressResponseInfo responseInfo = botpressApiService.createBot(botId, botName);

        logger.info("Phản hồi từ Botpress - HTTP Status: {}", responseInfo.getHttpStatus());
        logger.info("Phản hồi từ Botpress - Body: {}", responseInfo.getResponseBody());

        if (responseInfo.getHttpStatus().is2xxSuccessful()) {
            try {
                // Phân tích cú pháp JSON để kiểm tra lỗi nghiệp vụ
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(responseInfo.getResponseBody());
                String status = rootNode.path("status").asText();

                if ("ok".equals(status) || "success".equals(status)) {
                    Bot newBot = new Bot();
                    newBot.setBotId(botId);
                    newBot.setBotName(botName);
                    newBot.setOwnerId(ownerId);
                    newBot.setBotDescription(botDescription);
                    botRepository.save(newBot);
                    logger.info("Bot và database đã được tạo thành công.");
                    return botId;
                } else {
                    String errorMessage = rootNode.path("message").asText();
                    logger.error("Botpress API báo lỗi: {}", errorMessage);
                    return null;
                }
            } catch (Exception e) {
                logger.error("Không thể xử lý phản hồi JSON từ Botpress: {}", e.getMessage(), e);
                return null;
            }
        } else {
            logger.error("Lỗi HTTP khi tạo bot trên Botpress: {}", responseInfo.getHttpStatus());
            return null;
        }
    }    
    
    @Transactional // Đảm bảo tính toàn vẹn dữ liệu
    public boolean updateBotName(String botId, String newBotName, String ownerId) {
        Bot existingBot = botRepository.findByBotIdAndOwnerId(botId, ownerId).orElse(null);
        if (existingBot == null) {
            logger.warn("Attempted to update non-existent bot {} for owner {}", botId, ownerId);
            return false;
        }
        
        try {
            boolean isSuccess = botpressApiService.updateBotName(botId, newBotName);
            if (isSuccess) {
                existingBot.setBotName(newBotName);
                botRepository.save(existingBot);
                logger.info("Successfully updated bot name for botId: {}", botId);
            }
            return isSuccess;
        } catch (Exception e) {
            logger.error("Failed to update bot name for botId {}: {}", botId, e.getMessage(), e);
            return false; // Transaction sẽ tự rollback nếu có lỗi
        }
    }

    @Transactional // Đảm bảo tính toàn vẹn dữ liệu
    public boolean deleteBot(String botId, String ownerId) {
        if (!botRepository.existsByBotIdAndOwnerId(botId, ownerId)) {
            logger.warn("Attempted to delete non-existent bot {} for owner {}", botId, ownerId);
            return false;
        }
        
        try {
            boolean isSuccess = botpressApiService.deleteBot(botId);
            if (isSuccess) {
                botRepository.deleteByBotId(botId);
                logger.info("Successfully deleted bot from both systems with ID: {}", botId);
            }
            return isSuccess;
        } catch (Exception e) {
            logger.error("Failed to delete bot {} from Botpress: {}", botId, e.getMessage(), e);
            return false; // Transaction sẽ tự rollback nếu có lỗi
        }
    }

    public List<Bot> getBotsByOwnerId(String ownerId) {
        return botRepository.findByOwnerId(ownerId);
    }

    public Page<Bot> getBotsByOwnerId(String ownerId, Pageable pageable) {
        return botRepository.findByOwnerId(ownerId, pageable);
    }   

    // Lấy toàn bộ workspace từ Botpress
    public List<WorkspaceResponse> getWorkspacesFromBotpress() {
        return botpressApiService.getWorkspaces();
    }

    // Lấy bots trong workspace "default"
    public List<String> getBotsInDefaultWorkspace() {
        return botpressApiService.getWorkspaces().stream()
                .filter(ws -> "default".equals(ws.getId()))
                .findFirst()
                .map(WorkspaceResponse::getBots) // lấy trực tiếp list<String> bots
                .orElse(Collections.emptyList());
    }

    // Alias: lấy bots (thực chất từ workspace default)
    public List<String> getBotsFromBotpress() {
        return getBotsInDefaultWorkspace();
    }
}