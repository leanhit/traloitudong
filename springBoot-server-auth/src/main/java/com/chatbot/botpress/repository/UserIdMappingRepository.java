package com.chatbot.botpress.repository;

import com.chatbot.botpress.model.UserIdMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserIdMappingRepository extends JpaRepository<UserIdMapping, Long> {
    Optional<UserIdMapping> findByUserId(String userId);
}