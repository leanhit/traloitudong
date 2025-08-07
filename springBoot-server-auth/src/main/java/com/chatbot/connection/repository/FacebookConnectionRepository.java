// src/main/java/com/chatbot/connections/repositories/FacebookConnectionRepository.java

package com.chatbot.connection.repository;

import com.chatbot.connection.model.FacebookConnection;
import org.springframework.data.domain.Page; // Thêm dòng này
import org.springframework.data.domain.Pageable; // Thêm dòng này
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; 
import java.util.Optional;
import java.util.UUID;

public interface FacebookConnectionRepository extends JpaRepository<FacebookConnection, UUID> {
    Optional<FacebookConnection> findByBotId(String botId);
    Optional<FacebookConnection> findByPageId(String pageId);
    Optional<FacebookConnection> findByVerifyToken(String verifyToken);
    List<FacebookConnection> findByOwnerId(String ownerId); // Thêm dòng này
    Page<FacebookConnection> findByOwnerId(String ownerId, Pageable pageable); 
}