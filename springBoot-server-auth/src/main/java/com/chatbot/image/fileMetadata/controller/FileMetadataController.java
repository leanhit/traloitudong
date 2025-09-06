package com.chatbot.image.fileMetadata.controller;

import com.chatbot.image.fileMetadata.dto.FileResponseDTO;
import com.chatbot.image.fileMetadata.service.FileMetadataService;
import com.chatbot.image.fileMetadata.dto.FileRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/images") 
public class FileMetadataController {

    @Autowired
    private FileMetadataService fileMetadataService;

    @PostMapping
    public ResponseEntity<List<FileResponseDTO>> handleUpload(@Valid @ModelAttribute FileRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String webUserId = authentication.getName();

        try {
            List<FileResponseDTO> responses = fileMetadataService.processUploadRequest(request, webUserId);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint mới để lấy ảnh theo phân trang
    @GetMapping
    public ResponseEntity<Page<FileResponseDTO>> getAllFiles(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

            // Thêm câu lệnh println để in ra giá trị của page và size
        System.out.println("Received request for page: " + page + ", size: " + size);
        
        
        PageRequest pageable = PageRequest.of(page, size);
        Page<FileResponseDTO> filePage = fileMetadataService.getAllFiles(pageable);
        return ResponseEntity.ok(filePage);
    }
}