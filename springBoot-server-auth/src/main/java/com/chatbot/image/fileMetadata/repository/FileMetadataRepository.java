package com.chatbot.image.fileMetadata.repository;

import com.chatbot.image.fileMetadata.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, String> {
}