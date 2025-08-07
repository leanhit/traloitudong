package com.chatbot.botmanagement.service;

import com.chatbot.botmanagement.model.Bot;
import com.chatbot.botmanagement.repository.BotRepository;
import com.chatbot.botpress.service.BotpressApiService;
import com.chatbot.botpress.service.UserIdMappingService;
import org.springframework.stereotype.Service;
import com.chatbot.botmanagement.enums.BotEnums;
import java.util.List;

@Service
public class BotService {

    private final BotRepository botRepository;
    private final BotpressApiService botpressApiService;
    private final UserIdMappingService userIdMappingService; // Thêm service mới

    public BotService(BotRepository botRepository, BotpressApiService botpressApiService, UserIdMappingService userIdMappingService) {
        this.botRepository = botRepository;
        this.botpressApiService = botpressApiService;
        this.userIdMappingService = userIdMappingService;
    }

    public String createBot(String ownerId, String botName) {
        Long internalId = userIdMappingService.getOrCreateInternalId(ownerId); // Lấy ID số nội bộ
        String botId = String.format("bot_%d_%s", internalId, botName.replace(" ", "_").toLowerCase()); // Tạo botId
        
        String createdBotId = botpressApiService.createBot(botId, botName);
        if (createdBotId != null) {
            Bot newBot = new Bot();
            newBot.setBotId(createdBotId);
            newBot.setBotName(botName);
            newBot.setOwnerId(ownerId);
            botRepository.save(newBot);
        }
        return createdBotId;
    }
    
    public boolean updateBotName(String botId, String newBotName, String ownerId) {
        Bot existingBot = botRepository.findByBotIdAndOwnerId(botId, ownerId).orElse(null);
        if (existingBot == null) {
            return false;
        }
        boolean isSuccess = botpressApiService.updateBotName(botId, newBotName);
        if (isSuccess) {
            existingBot.setBotName(newBotName);
            botRepository.save(existingBot);
        }
        return isSuccess;
    }

    // --- SỬA LỖI 2: Thêm ownerId vào chữ ký phương thức ---
    public boolean deleteBot(String botId, String ownerId) {
        if (!botRepository.existsByBotIdAndOwnerId(botId, ownerId)) {
            return false;
        }
        boolean isSuccess = botpressApiService.deleteBot(botId);
        if (isSuccess) {
            botRepository.deleteByBotId(botId);
        }
        return isSuccess;
    }

    public List<Bot> getBotsByOwnerId(String ownerId) {
        return botRepository.findByOwnerId(ownerId);
    }

    public List<BotEnums> getAllBots() {
        return BotEnums.getAllBots();
    }
}