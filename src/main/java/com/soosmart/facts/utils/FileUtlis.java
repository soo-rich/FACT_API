package com.soosmart.facts.utils;

public class FileUtlis {
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }

    public static String generateUniqueFileName(String filename, String extension) {
        if (filename != null) {
            return filename + (extension.isEmpty() ? "" : "." + extension);
        } else {

            String timestamp = String.valueOf(System.currentTimeMillis());
            return timestamp + (extension.isEmpty() ? "" : "." + extension);
        }
    }
}
