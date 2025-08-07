package com.chatbot.botpress.service;

import com.chatbot.botpress.model.UserIdMapping;
import com.chatbot.botpress.repository.UserIdMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserIdMappingService {
    private final UserIdMappingRepository userIdMappingRepository;

    public UserIdMappingService(UserIdMappingRepository userIdMappingRepository) {
        this.userIdMappingRepository = userIdMappingRepository;
    }

    @Transactional
    public Long getOrCreateInternalId(String userId) {
        return userIdMappingRepository.findByUserId(userId)
                .map(UserIdMapping::getInternalId)
                .orElseGet(() -> {
                    UserIdMapping newMapping = new UserIdMapping();
                    newMapping.setUserId(userId);
                    return userIdMappingRepository.save(newMapping).getInternalId();
                });
    }
}