// File: src/main/java/com/chatbot/botmanagement/enums/BotEnums.java
package com.chatbot.botmanagement.enums;

import java.util.List; // Thêm dòng này
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BotEnums {
    // Tên enum nên là một định danh duy nhất (ví dụ: tên viết hoa)
    EASY_CHATBOT("bot_1_easychatbot", "Easy Chatbot"),
    HR_BOT("bot_2_hrbot", "HR Bot"),
    SALES_BOT("bot_3_salesbot", "Sales Bot");

    private final String botId;
    private final String botName;

    BotEnums(String botId, String botName) {
        this.botId = botId;
        this.botName = botName;
    }

    public String getBotId() {
        return botId;
    }

    public String getBotName() {
        return botName;
    }

    // Phương thức tiện ích để tìm botId từ tên bot (không phân biệt chữ hoa, chữ thường)
    public static String getBotIdByName(String name) {
        for (BotEnums bot : BotEnums.values()) {
            if (bot.getBotName().equalsIgnoreCase(name)) {
                return bot.getBotId();
            }
        }
        return null;
    } 

    public static List<BotEnums> getAllBots() {
        return List.of(BotEnums.values());
    }
}