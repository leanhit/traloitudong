package com.chatbot.botmanagement.repository;

import com.chatbot.botmanagement.model.Bot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface BotRepository extends JpaRepository<Bot, Long> {
    Optional<Bot> findByBotId(String botId);
    List<Bot> findByOwnerId(String ownerId);
    Optional<Bot> findByBotIdAndOwnerId(String botId, String ownerId);
    boolean existsByBotIdAndOwnerId(String botId, String ownerId);
    Page<Bot> findByOwnerId(String ownerId, Pageable pageable);
    
    @Transactional
    void deleteByBotId(String botId);
}