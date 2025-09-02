package com.soosmart.facts.Implement.file;

import com.soosmart.facts.dto.file.FileMetaDataDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.file.FileMetadata;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.file.FileMetadataRepository;
import com.soosmart.facts.security.user.UtilisateurConnecteServie;
import com.soosmart.facts.service.file.FileMetadataService;
import com.soosmart.facts.service.file.FileStorageService;
import com.soosmart.facts.utils.pagination.PageMapperUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.soosmart.facts.utils.FileUtlis.generateUniqueFileName;
import static com.soosmart.facts.utils.FileUtlis.getFileExtension;

@Service
public class FileMetadataImpl implements FileMetadataService {

    private final FileMetadataRepository fileMetadataRepository;
    private final FileStorageService fileStorageService;
    private final UtilisateurConnecteServie utilisateurConnecteServie;
    private final ResponseMapper responseMapper;
    @Value("${file.storage.provider}")
    private String provider;

    public FileMetadataImpl(FileMetadataRepository fileMetadataRepository, FileStorageService fileStorageService, UtilisateurConnecteServie utilisateurConnecteServie, ResponseMapper responseMapper) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.fileStorageService = fileStorageService;
        this.utilisateurConnecteServie = utilisateurConnecteServie;
        this.responseMapper = responseMapper;
    }


    public FileMetadata save(MultipartFile file) {
        String uniqueFileName = generateUniqueFileName(getFileExtension(file.getOriginalFilename()));

        String username = utilisateurConnecteServie
                .getUtilisateurConnecte().getUsername();
        return fileMetadataRepository.save(FileMetadata.builder()
                .storageUrl(this.fileStorageService.uploadFile(file))
                .storageProvider(provider)
                .fileSize(file.getSize())
                .originalFileName(file.getOriginalFilename())
                .fileName(uniqueFileName)
                .contentType(file.getContentType())
//                .uploadedBy("N/A")
                .uploadedBy(username)
                .build());
    }

    public FileMetadata findById(UUID id) {
        return fileMetadataRepository.findById(id).orElse(null);
    }

    public List<FileMetadata> findByUploadedBy(String uploadedBy) {
        return fileMetadataRepository.findByUploadedBy(uploadedBy);
    }

    public Boolean delete(UUID id) {
        FileMetadata fileMetadata = findById(id);
        if (fileMetadata != null) {
            fileMetadata.setSupprimer(true);
            this.fileMetadataRepository.save(fileMetadata);
        }
        return false;
    }

    public CustomPageResponse<FileMetaDataDto> findAll(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(this.fileMetadataRepository.findAllBySupprimerIsFalse(PageMapperUtils.createPageableWithoutSearch(paginatedRequest)), this.responseMapper::responseFileMetadate);
    }
}
