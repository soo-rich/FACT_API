package com.soosmart.facts.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /*  *
     * Upload un fichier vers le stockage
     * @param file Le fichier à uploader
     * @param fileName Le nom du fichier
     * @return L'URL d'accès au fichier
     */
    String uploadFile(MultipartFile file, String fileName);

    /**
     * Upload un fichier vers le stockage
     *
     * @param file Le fichier à uploader
     * @return L'URL d'accès au fichier
     */
    String uploadFile(MultipartFile file);

    /**
     * Télécharge un fichier depuis le stockage
     *
     * @param fileName Le nom du fichier
     * @return byte[] du fichier
     */
    byte[] downloadFile(String fileName);

    /**
     * Supprime un fichier du stockage
     *
     * @param fileName Le nom du fichier
     * @return true si supprimé avec succès
     */
    boolean deleteFile(String fileName);

    /**
     * Génère une URL signée pour accéder temporairement au fichier
     *
     * @param fileName                Le nom du fichier
     * @param expirationTimeInMinutes Durée d'expiration en minutes
     * @return URL signée
     */
    String generateSignedUrl(String fileName, int expirationTimeInMinutes);

    /**
     * Vérifie si un fichier existe
     *
     * @param fileName Le nom du fichier
     * @return true si le fichier existe
     */
    boolean fileExists(String fileName);
}