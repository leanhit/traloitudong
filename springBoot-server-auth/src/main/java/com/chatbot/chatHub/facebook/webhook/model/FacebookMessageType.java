package com.chatbot.chatHub.facebook.webhook.model;

public enum FacebookMessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO,
    FILE,
    ATTACHMENT,   // fallback chung nếu không rõ loại
    QUICK_REPLY,
    POSTBACK,
    REACTION,
    READ,
    DELIVERY,
    ECHO,
    UNKNOWN
}
