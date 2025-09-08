package com.chatbot.image.fileMetadata.service;

import com.chatbot.auth.model.Auth;
import com.chatbot.auth.repository.AuthRepository;
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
import io.minio.RemoveObjectArgs;

@Service
public class FileMetadataService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AuthRepository authRepository;

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.bucketName}")
    private String bucketName;

    private final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private final String[] ALLOWED_CONTENT_TYPES = {"image/jpeg", "image/png", "image/webp", "image/gif"};

    @Transactional
    public List<FileResponseDTO> processUploadRequest(FileRequestDTO request, String email) throws Exception {
        if ((request.getFiles() == null || request.getFiles().isEmpty())
                && (request.getUrls() == null || request.getUrls().isEmpty())) {
            throw new IllegalArgumentException("Phải gửi ít nhất một file hoặc một URL");
        }

        // lấy user từ email
        Auth user = authRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user với email: " + email));

        // lấy category
        Category category = categoryService.getCategoryById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Category với ID: " + request.getCategoryId()));

        List<FileResponseDTO> responses = new ArrayList<>();

        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            for (MultipartFile file : request.getFiles()) {
                responses.add(uploadFile(file, request, category, user));
            }
        }

        if (request.getUrls() != null && !request.getUrls().isEmpty()) {
            for (String url : request.getUrls()) {
                responses.add(uploadFromUrl(url, request, category, user));
            }
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public Page<FileResponseDTO> getAllFiles(Pageable pageable) {
        Page<FileMetadata> fileMetadataPage = fileMetadataRepository.findAll(pageable); 
        return fileMetadataPage.map(this::convertToDto);
    }

    private FileResponseDTO convertToDto(FileMetadata file) {
        return new FileResponseDTO(
            file.getId(),
            file.getFileName(),
            file.getFileUrl(),
            file.getFileSize(),
            file.getContentType(),
            file.getUploadTime(),
            file.getTitle(),
            file.getDescription(),
            file.getCategory() != null ? file.getCategory().getId().toString() : null,
            file.getTags() != null ? new ArrayList<>(file.getTags()) : List.of(), // <-- sửa ở đây
            file.getCode()
        );
    }


    private FileResponseDTO uploadFile(MultipartFile file, FileRequestDTO request,
                                       Category category, Auth user)
            throws IOException, MinioException, InvalidKeyException, NoSuchAlgorithmException {

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
        metadata.setTitle(request.getTitle());
        metadata.setDescription(request.getDescription());
        metadata.setCategory(category);
        metadata.setTags(request.getTags());
        metadata.setUser(user);
        metadata.setCode(request.getCode());

        fileMetadataRepository.save(metadata);

        return convertToDto(metadata);
    }

    private FileResponseDTO uploadFromUrl(String url, FileRequestDTO request,
                                          Category category, Auth user)
            throws IOException, MinioException, InvalidKeyException, NoSuchAlgorithmException {

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
            metadata.setTitle(request.getTitle());
            metadata.setDescription(request.getDescription());
            metadata.setCategory(category);
            metadata.setTags(request.getTags());
            metadata.setUser(user);
            metadata.setCode(request.getCode());

            fileMetadataRepository.save(metadata);

            return convertToDto(metadata);
        }
    }

    @Transactional
    public boolean deleteFileById(String id) {
        UUID uuid = UUID.fromString(id);  // convert String -> UUID
        return fileMetadataRepository.findById(uuid).map(file -> {
            try {
                // Xóa file trên MinIO trước
                minioClient.removeObject(
                    RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(file.getFileName())
                        .build()
                );
                // Xóa metadata trong DB
                fileMetadataRepository.delete(file);
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Không thể xóa file: " + e.getMessage(), e);
            }
        }).orElse(false);
    }

    @Transactional
    public FileResponseDTO updateFile(String id, FileRequestDTO requestDTO) {
        UUID uuid = UUID.fromString(id);  // convert String -> UUID
        return fileMetadataRepository.findById(uuid).map(file -> {
            file.setTitle(requestDTO.getTitle());
            file.setDescription(requestDTO.getDescription());
            if (requestDTO.getCategoryId() != null) {
                Category category = categoryService.getCategoryById(requestDTO.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Category với ID: " + requestDTO.getCategoryId()));
                file.setCategory(category);
            }
            file.setTags(requestDTO.getTags());
            fileMetadataRepository.save(file);
            return convertToDto(file);
        }).orElse(null);
    }

    @Transactional
    public FileResponseDTO updateFileContent(String id, FileRequestDTO requestDTO) throws Exception {
        UUID uuid = UUID.fromString(id);
        return fileMetadataRepository.findById(uuid).map(file -> {
            try {
                // Nếu gửi file mới
                if (requestDTO.getFiles() != null && !requestDTO.getFiles().isEmpty()) {
                    MultipartFile newFile = requestDTO.getFiles().get(0); // chỉ lấy file đầu tiên
                    // Xóa file cũ trên MinIO
                    minioClient.removeObject(
                        RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(file.getFileName())
                            .build()
                    );
                    // Upload file mới
                    String originalFilename = StringUtils.cleanPath(newFile.getOriginalFilename());
                    String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9.\\-]", "_");
                    String uniqueFileName = LocalDateTime.now().toString().replace(":", "-") + "_" + UUID.randomUUID() + "_" + sanitizedFilename;

                    try (InputStream inputStream = newFile.getInputStream()) {
                        minioClient.putObject(
                            PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(uniqueFileName)
                                .stream(inputStream, newFile.getSize(), -1)
                                .contentType(newFile.getContentType())
                                .build()
                        );
                    }

                    String fileUrl = String.format("%s/%s/%s", minioUrl, bucketName, uniqueFileName);

                    file.setFileName(uniqueFileName);
                    file.setFileUrl(fileUrl);
                    file.setFileSize(newFile.getSize());
                    file.setContentType(newFile.getContentType());
                }

                // Cập nhật metadata
                file.setTitle(requestDTO.getTitle());
                file.setDescription(requestDTO.getDescription());
                if (requestDTO.getCategoryId() != null) {
                    Category category = categoryService.getCategoryById(requestDTO.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Category với ID: " + requestDTO.getCategoryId()));
                    file.setCategory(category);
                }
                file.setTags(requestDTO.getTags());
                fileMetadataRepository.save(file);

                return convertToDto(file);
            } catch (Exception e) {
                throw new RuntimeException("Không thể cập nhật file: " + e.getMessage(), e);
            }
        }).orElse(null);
    }

}
