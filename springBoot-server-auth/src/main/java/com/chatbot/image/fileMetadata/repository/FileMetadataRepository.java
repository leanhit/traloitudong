package com.chatbot.image.fileMetadata.repository;

import com.chatbot.image.fileMetadata.model.FileMetadata;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, UUID> {

    // Lấy 1 file kèm category + tags
    @EntityGraph(attributePaths = {"category", "tags"})
    Optional<FileMetadata> findById(UUID id);

    // Lấy tất cả file kèm category + tags
    @Override
    @EntityGraph(attributePaths = {"category", "tags"})
    List<FileMetadata> findAll();
}
