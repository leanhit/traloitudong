// src/main/java/com/chatbot/connections/repositories/FacebookConnectionRepository.java

package com.chatbot.chatHub.facebook.connection.repository;

import com.chatbot.chatHub.facebook.connection.model.FacebookConnection;
import org.springframework.data.domain.Page; // Thêm dòng này
import org.springframework.data.domain.Pageable; // Thêm dòng này
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; 
import java.util.Optional;
import java.util.UUID;

public interface FacebookConnectionRepository extends JpaRepository<FacebookConnection, UUID> {
    Optional<FacebookConnection> findByBotId(String botId);
    Optional<FacebookConnection> findByPageId(String pageId);
    List<FacebookConnection> findByOwnerId(String ownerId); // Thêm dòng này
    Page<FacebookConnection> findByOwnerId(String ownerId, Pageable pageable); 

     // 👇 Thêm method có filter active
    List<FacebookConnection> findByOwnerIdAndIsActiveTrue(String ownerId);
    Page<FacebookConnection> findByOwnerIdAndIsActiveTrue(String ownerId, Pageable pageable);

}