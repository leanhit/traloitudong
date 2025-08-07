package com.chatbot.botmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bots")
@Data
public class Bot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bot_id", unique = true, nullable = false, length = 255)
    private String botId;

    @Column(name = "bot_name", nullable = false)
    private String botName;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;
}