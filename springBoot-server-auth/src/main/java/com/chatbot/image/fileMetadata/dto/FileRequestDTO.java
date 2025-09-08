package com.chatbot.image.fileMetadata.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Data
public class FileRequestDTO {
    private List<MultipartFile> files = new ArrayList<>();
    private List<String> urls = new ArrayList<>();

    private String title;
    private String description;

    private UUID categoryId;
    private List<String> tags = new ArrayList<>();

    private String code;  // <--- thêm mới
}
