package com.chatbot.image.fileMetadata.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDTO {
    private String fileName;
    private String fileUrl;
    private long fileSize;
    private String contentType;
    private LocalDateTime uploadTime;
}