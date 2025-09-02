package com.soosmart.facts.repository.file;

import com.soosmart.facts.entity.file.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, UUID> {
    List<FileMetadata> findByUploadedBy(String uploadedBy);

    Page findAllBySupprimerIsFalse(Pageable pageable);
}
