package com.chatbot.image.fileMetadata.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class FileRequestDTO {
    @NotNull(message = "Danh sách files không được null")
    @Size(min = 0, message = "Danh sách files không được null")
    private List<MultipartFile> files;

    @NotNull(message = "Danh sách URLs không được null")
    @Size(min = 0, message = "Danh sách URLs không được null")
    private List<String> urls;

    @NotNull(message = "Danh sách URLs không được null")
    private String categoryId;

    private String title;
    private String description;
    private String category;
    private List<String> tags;
}