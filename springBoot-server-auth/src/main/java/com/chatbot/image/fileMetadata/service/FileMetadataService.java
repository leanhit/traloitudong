package com.chatbot.image.fileMetadata.service;

import com.chatbot.image.category.model.Category;
import com.chatbot.image.category.service.CategoryService;
import com.chatbot.image.fileMetadata.dto.FileRequestDTO;
import com.chatbot.image.fileMetadata.dto.FileResponseDTO;
import com.chatbot.image.fileMetadata.model.FileMetadata;
import com.chatbot.image.fileMetadata.repository.FileMetadataRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileMetadataService {

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private FileMetadataRepository fileMetadataRepository;
    @Autowired
    private CategoryService categoryService;

    @Value("${minio.url}")
    private String minioUrl;
    @Value("${minio.bucketName}")
    private String bucketName;

    private final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private final String[] ALLOWED_CONTENT_TYPES = {"image/jpeg", "image/png", "image/webp", "image/gif"};

    @Transactional
    public List<FileResponseDTO> processUploadRequest(FileRequestDTO request, String webUserId) throws Exception {
        if ((request.getFiles() == null || request.getFiles().isEmpty()) && (request.getUrls() == null || request.getUrls().isEmpty())) {
            throw new IllegalArgumentException("Phải gửi ít nhất một file hoặc một URL");
        }

        // Lấy Category Entity thay vì DTO
        Category category = categoryService.getCategoryById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Category với ID: " + request.getCategoryId()));

        List<FileResponseDTO> responses = new ArrayList<>();
        
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            for (MultipartFile file : request.getFiles()) {
                responses.add(uploadFile(file, request.getTitle(), request.getDescription(), category, request.getTags(), webUserId));
            }
        }

        if (request.getUrls() != null && !request.getUrls().isEmpty()) {
            for (String url : request.getUrls()) {
                responses.add(uploadFromUrl(url, request.getTitle(), request.getDescription(), category, request.getTags(), webUserId));
            }
        }
        return responses;
    }

    // Phương thức mới để lấy tất cả ảnh với phân trang
    @Transactional(readOnly = true)
    public Page<FileResponseDTO> getAllFiles(Pageable pageable) {
        Page<FileMetadata> fileMetadataPage = fileMetadataRepository.findAll(pageable);
        return fileMetadataPage.map(this::convertToDto);
    }
    
    // Phương thức chuyển đổi Entity sang DTO
    private FileResponseDTO convertToDto(FileMetadata metadata) {
        return new FileResponseDTO(
            metadata.getFileName(),
            metadata.getFileUrl(),
            metadata.getFileSize(),
            metadata.getContentType(),
            metadata.getUploadTime()
        );
    }

    private FileResponseDTO uploadFile(MultipartFile file, String title, String description, Category category, List<String> tags, String webUserId) throws IOException, MinioException, InvalidKeyException, NoSuchAlgorithmException {
        // (Giữ nguyên code của phương thức này)
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File không được rỗng");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Kích thước file vượt quá 10MB");
        }
        String contentType = file.getContentType();
        boolean isAllowedType = false;
        if (contentType != null) {
            for (String allowedType : ALLOWED_CONTENT_TYPES) {
                if (allowedType.equalsIgnoreCase(contentType)) {
                    isAllowedType = true;
                    break;
                }
            }
        }
        if (!isAllowedType) {
            throw new IllegalArgumentException("Định dạng file không được hỗ trợ: " + contentType);
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9.\\-]", "_");
        String uniqueFileName = LocalDateTime.now().toString().replace(":", "-") + "_" + UUID.randomUUID() + "_" + sanitizedFilename;

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(uniqueFileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        }

        String fileUrl = String.format("%s/%s/%s", minioUrl, bucketName, uniqueFileName);

        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(uniqueFileName);
        metadata.setFileUrl(fileUrl);
        metadata.setFileSize(file.getSize());
        metadata.setContentType(file.getContentType());
        metadata.setUploadTime(LocalDateTime.now());
        metadata.setTitle(title);
        metadata.setDescription(description);
        metadata.setCategory(category);
        metadata.setTags(tags);
        metadata.setWebUserId(webUserId);
        
        fileMetadataRepository.save(metadata);

        return new FileResponseDTO(
            metadata.getFileName(),
            metadata.getFileUrl(),
            metadata.getFileSize(),
            metadata.getContentType(),
            metadata.getUploadTime()
        );
    }

    private FileResponseDTO uploadFromUrl(String url, String title, String description, Category category, List<String> tags, String webUserId) throws IOException, MinioException, InvalidKeyException, NoSuchAlgorithmException {
        // (Giữ nguyên code của phương thức này)
        String originalFilename = StringUtils.getFilename(url);
        String uniqueFileName = LocalDateTime.now().toString().replace(":", "-") + "_" + UUID.randomUUID() + "_" + originalFilename;

        try (InputStream inputStream = new URL(url).openStream()) {
            URL urlObject = new URL(url);
            long fileSize = urlObject.openConnection().getContentLengthLong();
            String contentType = urlObject.openConnection().getContentType();

            if (fileSize > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("Kích thước file từ URL vượt quá 10MB");
            }
            boolean isAllowedType = false;
            if (contentType != null) {
                for (String allowedType : ALLOWED_CONTENT_TYPES) {
                    if (allowedType.equalsIgnoreCase(contentType)) {
                        isAllowedType = true;
                        break;
                    }
                }
            }
            if (!isAllowedType) {
                throw new IllegalArgumentException("Định dạng file từ URL không được hỗ trợ: " + contentType);
            }

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(uniqueFileName)
                    .stream(inputStream, fileSize, -1)
                    .contentType(contentType)
                    .build());

            String finalFileUrl = String.format("%s/%s/%s", minioUrl, bucketName, uniqueFileName);

            FileMetadata metadata = new FileMetadata();
            metadata.setFileName(uniqueFileName);
            metadata.setFileUrl(finalFileUrl);
            metadata.setFileSize(fileSize);
            metadata.setContentType(contentType);
            metadata.setUploadTime(LocalDateTime.now());
            metadata.setTitle(title);
            metadata.setDescription(description);
            metadata.setCategory(category);
            metadata.setTags(tags);
            metadata.setWebUserId(webUserId);
            fileMetadataRepository.save(metadata);

            return new FileResponseDTO(
                metadata.getFileName(),
                metadata.getFileUrl(),
                metadata.getFileSize(),
                metadata.getContentType(),
                metadata.getUploadTime()
            );
        }
    }
}