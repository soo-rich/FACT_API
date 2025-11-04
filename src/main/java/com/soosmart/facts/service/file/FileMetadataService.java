package com.soosmart.facts.service.file;

import com.soosmart.facts.dto.file.FileMetaDataDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.file.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileMetadataService {
    FileMetadata save(MultipartFile file);

    FileMetadata save(MultipartFile file, String subDir);

    FileMetadata save(MultipartFile file, String filename, String subDir);

    FileMetadata findById(UUID id);

    List<FileMetadata> findByUploadedBy(String uploadedBy);

    Boolean delete(UUID id);

    CustomPageResponse<FileMetaDataDto> findAll(PaginatedRequest paginatedRequest);
}
