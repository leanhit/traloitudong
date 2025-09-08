package com.chatbot.image.fileMetadata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDTO {
    private UUID id;
    private String fileName;
    private String fileUrl;
    private long fileSize;
    private String contentType;
    private LocalDateTime uploadTime;

    private String title;
    private String description;
    private String categoryId;
    private List<String> tags;
    private String code;   // <--- thêm mới
}
