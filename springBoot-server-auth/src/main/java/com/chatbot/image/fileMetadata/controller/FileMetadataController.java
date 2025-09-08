package com.chatbot.image.fileMetadata.controller;

import com.chatbot.image.fileMetadata.dto.FileResponseDTO;
import com.chatbot.image.fileMetadata.service.FileMetadataService;
import com.chatbot.image.fileMetadata.dto.FileRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.PageRequest;  

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class FileMetadataController {

    private final FileMetadataService fileMetadataService;

    public FileMetadataController(FileMetadataService fileMetadataService) {
        this.fileMetadataService = fileMetadataService;
    }

    /**
     * Upload một hoặc nhiều file hoặc URL.
     * - Lấy thông tin user từ token (email)
     * - Validate: phải có ít nhất 1 file hoặc 1 URL
     * - Trả về danh sách các FileResponseDTO đã lưu
     */
    @PostMapping
    public ResponseEntity<List<FileResponseDTO>> handleUpload(
            @Validated @ModelAttribute FileRequestDTO requestDTO,
            Authentication authentication) throws Exception {

        String email = authentication != null ? authentication.getName() : null;
        if (email == null) {
            throw new IllegalArgumentException("Không tìm thấy thông tin người dùng từ token");
        }

        if ((requestDTO.getFiles() == null || requestDTO.getFiles().isEmpty())
                && (requestDTO.getUrls() == null || requestDTO.getUrls().isEmpty())) {
            throw new IllegalArgumentException("Phải có ít nhất một file hoặc một URL.");
        }

        return ResponseEntity.ok(fileMetadataService.processUploadRequest(requestDTO, email));
    }

    /**
     * Lấy danh sách file (có phân trang)
     * @param page: số trang (bắt đầu từ 0)
     * @param size: số phần tử/trang
     * @return Page<FileResponseDTO>
     */
    @GetMapping
    public ResponseEntity<?> getAllFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(fileMetadataService.getAllFiles(PageRequest.of(page, size)));
    }

    /**
     * Xóa file theo ID
     * @param id: ID file
     * @return 200 nếu xóa thành công, 404 nếu không tìm thấy
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id) {
        boolean deleted = fileMetadataService.deleteFileById(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cập nhật metadata file (name, description, tags, category…)
     * @param id: ID file
     * @param requestDTO: dữ liệu metadata mới
     * @return FileResponseDTO mới nếu cập nhật thành công, 404 nếu không tìm thấy
     */
    @PutMapping("/{id}")
    public ResponseEntity<FileResponseDTO> updateFile(
            @PathVariable String id,
            @Validated @RequestBody FileRequestDTO requestDTO) {
        
        FileResponseDTO updatedFile = fileMetadataService.updateFile(id, requestDTO);
        if (updatedFile != null) {
            return ResponseEntity.ok(updatedFile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cập nhật file thực tế (upload lại) kèm metadata nếu có
     * @param id: ID file
     * @param requestDTO: dữ liệu file mới (multipart/form-data)
     * @return FileResponseDTO sau khi cập nhật
     */
    @PutMapping("/{id}/file")
    public ResponseEntity<FileResponseDTO> updateFileContent(
            @PathVariable String id,
            @ModelAttribute FileRequestDTO requestDTO) throws Exception {

        FileResponseDTO updatedFile = fileMetadataService.updateFileContent(id, requestDTO);
        return ResponseEntity.ok(updatedFile);
    }

}
