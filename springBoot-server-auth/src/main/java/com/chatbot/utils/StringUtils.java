// /src/main/java/com/chatbot/utils/StringUtils.java
package com.chatbot.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {
    public static String slugify(String text) {
        // Loại bỏ dấu tiếng Việt và các ký tự đặc biệt không mong muốn
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalizedText).replaceAll("");
        
        // Thay thế các ký tự không phải chữ, số, gạch dưới, gạch ngang bằng gạch dưới và chuyển sang chữ thường
        return result.replaceAll("[^a-zA-Z0-9_ -]", "").replace(" ", "_").toLowerCase();
    }
}