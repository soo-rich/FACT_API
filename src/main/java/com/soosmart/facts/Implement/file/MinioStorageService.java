package com.soosmart.facts.Implement.file;

import com.soosmart.facts.service.file.FileStorageService;
import io.minio.*;
import io.minio.http.Method;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

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
    public String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        try {
            InputStream inputStream = file.getInputStream();

            return minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            ).toString();


        } catch (Exception e) {
            logger.error("Error uploading file to MinIO: {}", fileName, e);
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }

    @Override
    public byte[] downloadFile(String fileName) {
        try {

            return IOUtils.toByteArray(minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
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
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );

            logger.info("File deleted successfully from MinIO: {}", fileName);
            return true;

        } catch (Exception e) {
            logger.error("Error deleting file from MinIO: {}", fileName, e);
            return false;
        }
    }

    @Override
    public String generateSignedUrl(String fileName, int expirationTimeInMinutes) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
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
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            return true;

        } catch (Exception e) {
            logger.debug("File does not exist in MinIO: {}", fileName);
            return false;
        }
    }
}