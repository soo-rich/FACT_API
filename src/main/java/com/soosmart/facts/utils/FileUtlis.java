package com.soosmart.facts.utils;

import java.util.UUID;

public class FileUtlis {
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }

    public static String generateUniqueFileName(String extension) {
        String uuid = UUID.randomUUID().toString();
        String timestamp = String.valueOf(System.currentTimeMillis());
        return uuid + "_" + timestamp + (extension.isEmpty() ? "" : "." + extension);
    }
}
