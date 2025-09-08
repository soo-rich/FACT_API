package com.soosmart.facts.Implement.file;

import com.soosmart.facts.exceptions.file.FileStorageException;
import com.soosmart.facts.service.file.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@ConditionalOnProperty(name = "file.storage.provider", havingValue = "local")
public class LocalStorageService implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(LocalStorageService.class);

    @Value("${file.storage.local.base-path}")
    private String basePath;

    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            // Créer les sous-dossiers si nécessaire
            Path fullPath = createDirectoriesIfNeeded(fileName);

            // Copier le fichier
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);
            }

            logger.info("File uploaded successfully to local storage: {}", fullPath);

            // Retourner l'URL d'accès local
            return fullPath.toString();

        } catch (IOException e) {
            logger.error("Error uploading file to local storage: {}", fileName, e);
            throw new FileStorageException("Failed to upload file to local storage", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return uploadFile(file, file.getOriginalFilename());
    }

    @Override
    public String uploadFileToSubFolder(MultipartFile file, String subFolder) {
        return uploadFile(file, subFolder);
    }

    @Override
    public byte[] downloadFile(String fileName) {
        try {
            Path filePath = Paths.get(basePath, fileName);

            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found: " + fileName);
            }

            return Files.readAllBytes(filePath);

        } catch (IOException e) {
            logger.error("Error downloading file from local storage: {}", fileName, e);
            throw new RuntimeException("Failed to download file from local storage", e);
        }
    }

    @Override
    public boolean deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(basePath, fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("File deleted successfully from local storage: {}", fileName);
                return true;
            }

            logger.warn("File does not exist for deletion: {}", fileName);
            return false;

        } catch (IOException e) {
            logger.error("Error deleting file from local storage: {}", fileName, e);
            return false;
        }
    }

    @Override
    public String generateSignedUrl(String fileName, int expirationTimeInMinutes) {
       /* try {
            // Pour le stockage local, on génère une URL avec un token temporaire
            // Ici on utilise un simple encodage Base64 avec timestamp (à améliorer pour la production)
            long expirationTime = System.currentTimeMillis() + (expirationTimeInMinutes * 60 * 1000L);
            String token = Base64.getEncoder().encodeToString(
                    (fileName + ":" + expirationTime).getBytes()
            );

            return generateFileUrl(fileName) + "?token=" + token;

        } catch (Exception e) {
            logger.error("Error generating signed URL for local file: {}", fileName, e);
            throw new RuntimeException("Failed to generate signed URL", e);
        }*/
        return "";
    }

    @Override
    public boolean fileExists(String fileName) {
        Path filePath = Paths.get(basePath, fileName);
        return Files.exists(filePath);
    }

    /**
     * Crée les dossiers nécessaires pour le fichier s'ils n'existent pas
     *
     * @param fileName Le nom du fichier avec son chemin
     * @return Le chemin complet du fichier
     * @throws IOException Si erreur lors de la création des dossiers
     */
    private Path createDirectoriesIfNeeded(String fileName) throws IOException {
        Path fullPath = Paths.get(basePath, fileName);
        Path parentDir = fullPath.getParent();

        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
            logger.info("Created directories: {}", parentDir);
        }

        return fullPath;
    }

    /**
     * Génère l'URL d'accès au fichier
     *
     * @param fileName Le nom du fichier
     * @return L'URL d'accès
     */
   /* private String generateFileUrl(String fileName) {
        String baseUrl = "http://localhost:" + serverPort;
        if (contextPath != null && !contextPath.isEmpty()) {
            baseUrl += contextPath;
        }
        return baseUrl + "/api/files/" + fileName;
    }*/
}