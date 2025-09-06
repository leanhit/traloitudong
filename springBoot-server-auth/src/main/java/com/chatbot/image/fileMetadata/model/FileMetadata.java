package com.chatbot.image.fileMetadata.model;

import com.chatbot.image.category.model.Category;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "file_metadata")
@Data
@NoArgsConstructor
public class FileMetadata {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private String id;

    private String fileName;
    private String fileUrl;
    private long fileSize;
    private String contentType;
    private LocalDateTime uploadTime;
    private String title;
    private String description;

    // Thiết lập mối quan hệ ManyToOne với bảng Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Trường tags sẽ được lưu dưới dạng một danh sách chuỗi
    @ElementCollection
    @CollectionTable(name = "file_tags", joinColumns = @JoinColumn(name = "file_metadata_id"))
    @Column(name = "tag")
    private List<String> tags;
    
    // Thêm trường webUserId
    private String webUserId;

    public FileMetadata(String fileName, String fileUrl, long fileSize, String contentType, LocalDateTime uploadTime) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.uploadTime = uploadTime;
    }
}