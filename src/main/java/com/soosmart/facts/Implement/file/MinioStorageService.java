package com.soosmart.facts.Implement.file;

import com.soosmart.facts.service.file.FileStorageService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static com.soosmart.facts.utils.FileUtlis.generateUniqueFileName;
import static com.soosmart.facts.utils.FileUtlis.getFileExtension;

@Service
@ConditionalOnProperty(name = "file.storage.provider", havingValue = "minio")
public class MinioStorageService implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(MinioStorageService.class);
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;


    public MinioStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            // Normaliser le nom du fichier pour MinIO (remplacer les backslashes par des slashes)
            String normalizedFileName = fileName.replace("\\", "/");

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(normalizedFileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            logger.info("File uploaded successfully to MinIO: {}", normalizedFileName);
            return String.format("%s/%s", bucketName, normalizedFileName);

        } catch (Exception e) {
            logger.error("Error uploading file to MinIO: {}", fileName, e);
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String uniqueFileName = generateUniqueFileName(fileExtension);

        try {
            InputStream inputStream = file.getInputStream();

            ObjectWriteResponse response = minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(uniqueFileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            logger.info("File uploaded successfully to MinIO: {}", uniqueFileName);
            return String.format("%s/%s", bucketName, uniqueFileName);

        } catch (Exception e) {
            logger.error("Error uploading file to MinIO: {}", uniqueFileName, e);
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }

    /**
     * Upload un fichier dans un sous-dossier spécifique
     *
     * @param file      Le fichier à uploader
     * @param subFolder Le sous-dossier (ex: "documents/invoices")
     * @return L'URL d'accès au fichier
     */
    public String uploadFileToSubFolder(MultipartFile file, String subFolder) {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String uniqueFileName = generateUniqueFileName(fileExtension);

        // Construire le chemin avec sous-dossier
        String fullPath = subFolder.endsWith("/") ? subFolder + uniqueFileName : subFolder + "/" + uniqueFileName;

        return uploadFile(file, fullPath);
    }

    @Override
    public byte[] downloadFile(String fileName) {
        try {
            String normalizedFileName = fileName.replace("\\", "/");

            return IOUtils.toByteArray(minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(normalizedFileName)
                            .build()
            ));

        } catch (Exception e) {
            logger.error("Error downloading file from MinIO: {}", fileName, e);
            throw new RuntimeException("Failed to download file from MinIO", e);
        }
    }

    @Override
    public boolean deleteFile(String fileName) {
        try {
            String normalizedFileName = fileName.replace("\\", "/");

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(normalizedFileName)
                            .build()
            );

            logger.info("File deleted successfully from MinIO: {}", normalizedFileName);
            return true;

        } catch (Exception e) {
            logger.error("Error deleting file from MinIO: {}", fileName, e);
            return false;
        }
    }

    @Override
    public String generateSignedUrl(String fileName, int expirationTimeInMinutes) {
        try {
            String normalizedFileName = fileName.replace("\\", "/");

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(normalizedFileName)
                            .expiry(expirationTimeInMinutes, TimeUnit.MINUTES)
                            .build()
            );

        } catch (Exception e) {
            logger.error("Error generating signed URL for MinIO file: {}", fileName, e);
            throw new RuntimeException("Failed to generate signed URL", e);
        }
    }

    @Override
    public boolean fileExists(String fileName) {
        try {
            String normalizedFileName = fileName.replace("\\", "/");

            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(normalizedFileName)
                            .build()
            );
            return true;

        } catch (Exception e) {
            logger.debug("File does not exist in MinIO: {}", fileName);
            return false;
        }
    }

    /**
     * Vérifie que le bucket existe et le crée si nécessaire
     */
    private void ensureBucketExists() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
                logger.info("Bucket created successfully: {}", bucketName);
            }

        } catch (Exception e) {
            logger.error("Error ensuring bucket exists: {}", bucketName, e);
            throw new RuntimeException("Failed to ensure bucket exists", e);
        }
    }

    /**
     * Liste tous les fichiers dans un sous-dossier
     *
     * @param prefix Le préfixe/sous-dossier
     * @return Iterable des objets
     */
    public Iterable<Result<Item>> listFilesInFolder(String prefix) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .build()
        );
    }
}