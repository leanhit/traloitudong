package com.chatbot.botpress.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_id_mapping")
@Data
public class UserIdMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internalId;

    @Column(name = "user_id", unique = true, nullable = false, length = 255)
    private String userId;
}