package com.soosmart.facts.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class FileValidationService {

    private static final List<String> DANGEROUS_EXTENSIONS = Arrays.asList(
            "exe", "bat", "cmd", "com", "pif", "scr", "vbs", "js", "jar", "sh"
    );
    @Value("${file.storage.max-size:10485760}") // 10MB par défaut
    private long maxFileSize;
    @Value("${file.storage.allowed-types:jpg,jpeg,png,pdf,doc,docx}")
    private String allowedTypes;

    public ValidationResult validateFile(MultipartFile file) {
        // Vérifier si le fichier est vide
        if (file.isEmpty()) {
            return ValidationResult.error("Le fichier est vide");
        }

        // Vérifier la taille du fichier
        if (file.getSize() > maxFileSize) {
            return ValidationResult.error(
                    String.format("Le fichier est trop volumineux. Taille maximale autorisée : %d bytes", maxFileSize)
            );
        }

        // Vérifier l'extension du fichier
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            return ValidationResult.error("Nom de fichier invalide");
        }

        String extension = FileUtlis.getFileExtension(fileName);
        if (!isAllowedFileType(extension)) {
            return ValidationResult.error(
                    String.format("Type de fichier non autorisé. Types autorisés : %s", allowedTypes)
            );
        }

        // Vérifier les extensions dangereuses
        if (isDangerousFile(extension)) {
            return ValidationResult.error("Type de fichier potentiellement dangereux non autorisé");
        }

        // Vérifier le type MIME
        String contentType = file.getContentType();
        if (contentType == null || !isValidMimeType(contentType, extension)) {
            return ValidationResult.error("Type MIME du fichier invalide ou ne correspond pas à l'extension");
        }

        // Vérifications de sécurité supplémentaires
        if (!isSecureFileName(fileName)) {
            return ValidationResult.error("Nom de fichier contient des caractères non autorisés");
        }

        return ValidationResult.success();
    }

    private boolean isAllowedFileType(String extension) {
        List<String> allowedExtensions = Arrays.asList(allowedTypes.toLowerCase().split(","));
        return allowedExtensions.contains(extension.toLowerCase().trim());
    }

    private boolean isDangerousFile(String extension) {
        return DANGEROUS_EXTENSIONS.contains(extension.toLowerCase());
    }

    private boolean isValidMimeType(String mimeType, String extension) {
        // Mapping basique des types MIME selon l'extension
        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg" -> mimeType.equals("image/jpeg");
            case "png" -> mimeType.equals("image/png");
            case "pdf" -> mimeType.equals("application/pdf");
            case "txt" -> mimeType.equals("text/plain");
            case "doc" -> mimeType.equals("application/msword");
            case "docx" -> mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            default -> true; // Permettre d'autres types par défaut
        };
    }

    private boolean isSecureFileName(String fileName) {
        // Vérifier les caractères dangereux dans le nom de fichier
        String[] dangerousPatterns = {"../", "..\\", "/", "\\", ":", "*", "?", "\"", "<", ">", "|"};

        for (String pattern : dangerousPatterns) {
            if (fileName.contains(pattern)) {
                return false;
            }
        }

        return true;
    }

    // Classe pour le résultat de validation
    public record ValidationResult(boolean valid, String errorMessage) {

        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult error(String message) {
            return new ValidationResult(false, message);
        }

    }
}
