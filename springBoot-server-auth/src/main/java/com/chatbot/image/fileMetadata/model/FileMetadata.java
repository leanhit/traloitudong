package com.chatbot.image.fileMetadata.model;

import com.chatbot.image.category.model.Category;
import com.chatbot.auth.model.Auth;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "file_metadata")
@Data
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")   // ép Hibernate tạo cột UUID
    private UUID id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileUrl;

    private long fileSize;
    private String contentType;

    private LocalDateTime uploadTime;

    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ElementCollection
    @CollectionTable(
        name = "file_tags",
        joinColumns = @JoinColumn(
            name = "file_id", 
            referencedColumnName = "id", 
            columnDefinition = "UUID"   // ⚡ ép cột FK là UUID luôn
        )
    )
    @Column(name = "tag")
    private List<String> tags;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Auth user;

    @Column(length = 100)   // để filter sau này
    private String code;
}
