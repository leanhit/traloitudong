// src/main/java/com/chatbot/chatHub/facebook/autoConnect/service/FbAutoConnectService.java

package com.chatbot.chatHub.facebook.autoConnect.service;

import com.chatbot.chatHub.facebook.connection.model.FacebookConnection;
import com.chatbot.chatHub.facebook.connection.repository.FacebookConnectionRepository;
import com.chatbot.chatHub.facebook.webhook.service.FacebookApiGraphService;
import com.chatbot.chatHub.facebook.autoConnect.dto.AutoConnectResponse; 
import com.chatbot.chatHub.facebook.autoConnect.dto.ConnectionError; 
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FbAutoConnectService {

    private final FacebookConnectionRepository connectionRepository;
    private final FacebookApiGraphService facebookApiGraphService;

    public FbAutoConnectService(FacebookConnectionRepository connectionRepository,
                                FacebookApiGraphService facebookApiGraphService) {
        this.connectionRepository = connectionRepository;
        this.facebookApiGraphService = facebookApiGraphService;
    }

    /**
     * Auto connect tất cả fanpage của user
     *
     * @param webUserId       web userId từ auth token
     * @param botId           botId
     * @param userAccessToken Facebook user access token
     * @return AutoConnectResponse đối tượng chứa kết quả chi tiết
     */
    public AutoConnectResponse autoConnect(String webUserId, String botId, String userAccessToken) {
        System.out.println("🔹 Bắt đầu auto connect fanpage cho webUserId=" + webUserId);

        List<String> connectedPages = new ArrayList<>();
        List<String> reactivatedPages = new ArrayList<>();
        List<String> inactivePages = new ArrayList<>();
        List<ConnectionError> errors = new ArrayList<>();

        String fbUserId = facebookApiGraphService.getUserIdFromToken(userAccessToken);

        // 1️⃣ Lấy danh sách page từ FB (pageId, pageAccessToken, fbUserId)
        List<Map<String, Object>> fbPages = facebookApiGraphService.getUserPages(userAccessToken);
        if (fbPages.isEmpty()) {
            System.out.println("⚠️ User không có fanpage hoặc lấy page thất bại");
            return new AutoConnectResponse(
                false, 
                "User không có fanpage nào.", 
                Collections.emptyList(), 
                Collections.emptyList(), 
                Collections.emptyList(), 
                Collections.emptyList()
            );
        }
        Set<String> fbPageIds = fbPages.stream()
            .map(p -> (String) p.get("id"))
            .collect(Collectors.toSet());

        // 2️⃣ Lấy connection hiện tại của web user
        List<FacebookConnection> existingConnections = connectionRepository.findByOwnerId(webUserId);
        Map<String, FacebookConnection> pageIdToConnection = existingConnections.stream()
            .collect(Collectors.toMap(FacebookConnection::getPageId, c -> c));

        List<FacebookConnection> connectionsToSave = new ArrayList<>();

        // 3️⃣ Xử lý FB pages: kết nối mới hoặc kích hoạt lại
        for (Map<String, Object> page : fbPages) {
            String pageId = (String) page.get("id");
            String pageName = (String) page.get("name");
            String pageToken = (String) page.get("access_token");

            if (pageIdToConnection.containsKey(pageId)) {
                FacebookConnection conn = pageIdToConnection.get(pageId);
                
                // Luôn cập nhật access token và bot name mới nhất
                conn.setPageAccessToken(pageToken);
                conn.setBotName(pageName);
                
                // Nếu kết nối đang inactive, kích hoạt lại
                if (!conn.isActive()) {
                    conn.setActive(true); 
                    conn.setEnabled(true); 
                    conn.setLastUpdatedAt(LocalDateTime.now());
                    connectionsToSave.add(conn);
                    reactivatedPages.add(pageName); 
                    System.out.println("♻️ Kích hoạt lại trang: " + pageId + " (" + pageName + ")");
                } else {
                    // Nếu đã active, cập nhật và thêm vào danh sách để lưu
                    conn.setLastUpdatedAt(LocalDateTime.now());
                    connectionsToSave.add(conn);
                    connectedPages.add(pageName);
                    System.out.println("➡️ Trang đã có kết nối và active, đang cập nhật: " + pageId + " (" + pageName + ")");
                }
            } else {
                // Trang mới → tạo kết nối mới
                FacebookConnection conn = new FacebookConnection();
                conn.setId(UUID.randomUUID());
                conn.setBotId(botId);
                conn.setBotName(pageName);
                conn.setOwnerId(webUserId);
                conn.setFbUserId(fbUserId);
                conn.setPageId(pageId);
                conn.setFanpageUrl("https://www.facebook.com/" + pageId);
                conn.setPageAccessToken(pageToken);
                conn.setEnabled(true);
                conn.setActive(true);
                conn.setCreatedAt(LocalDateTime.now());
                conn.setLastUpdatedAt(LocalDateTime.now());
                connectionsToSave.add(conn);
                connectedPages.add(pageName); 
                System.out.println("➡️ Tạo kết nối mới cho trang: " + pageId + " (" + pageName + ")");
            }
        }

        // 4️⃣ Xử lý trang có kết nối nhưng không còn trong FB
        // ✅ ĐÃ SỬA: lọc các kết nối theo fbUserId hiện tại
        List<FacebookConnection> currentFbUserConnections = existingConnections.stream()
                .filter(conn -> conn.getFbUserId() != null && conn.getFbUserId().equals(fbUserId))
                .collect(Collectors.toList());

        for (FacebookConnection conn : currentFbUserConnections) {
            if (!fbPageIds.contains(conn.getPageId()) && conn.isActive()) {
                try {
                    facebookApiGraphService.unsubscribePageFromWebhook(conn.getPageId(), conn.getPageAccessToken());
                    inactivePages.add(conn.getBotName());
                    conn.setActive(false);
                    conn.setLastUpdatedAt(LocalDateTime.now());
                    connectionsToSave.add(conn);
                    System.out.println("❌ Đánh dấu không hoạt động trang " + conn.getPageId() + " cho fbUserId=" + fbUserId);
                } catch (Exception e) {
                    errors.add(new ConnectionError(conn.getBotName(), "Lỗi khi hủy đăng ký webhook: " + e.getMessage()));
                    inactivePages.add(conn.getBotName());
                    conn.setActive(false);
                    conn.setLastUpdatedAt(LocalDateTime.now());
                    connectionsToSave.add(conn);
                    System.err.println("❌ Lỗi khi hủy đăng ký webhook cho trang " + conn.getPageId() + ": " + e.getMessage());
                }
            }
        }

        // 5️⃣ Lưu các kết nối mới hoặc kích hoạt lại
        if (!connectionsToSave.isEmpty()) {
            connectionRepository.saveAll(connectionsToSave);
            System.out.println("✅ Lưu/kích hoạt " + connectionsToSave.size() + " kết nối thành công");
        }

        // 6️⃣ Đăng ký webhook cho các kết nối đang hoạt động
        for (FacebookConnection conn : connectionsToSave) {
            try {
                if(conn.isActive()){
                    facebookApiGraphService.subscribePageToWebhook(conn.getPageId(), conn.getPageAccessToken());
                }
            } catch (Exception e) {
                errors.add(new ConnectionError(conn.getBotName(), "Lỗi khi đăng ký webhook: " + e.getMessage()));
                System.err.println("❌ Lỗi khi đăng ký webhook cho trang " + conn.getPageId() + ": " + e.getMessage());
            }
        }

        System.out.println("🔹 Tự động kết nối hoàn tất cho webUserId=" + webUserId);
        
        boolean isSuccess = errors.isEmpty();
        String message = isSuccess ? "Tất cả kết nối đã được xử lý thành công!" : "Đã xử lý xong, nhưng có lỗi xảy ra.";
        
        return new AutoConnectResponse(
            isSuccess, 
            message, 
            connectedPages, 
            reactivatedPages, 
            inactivePages, 
            errors
        );
    }
}