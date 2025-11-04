package com.soosmart.facts.dto.file;

import java.time.Instant;

public record FileMetaDataDto(
        String filename,
        String uri,
        String contentType,
        Long size,
        String uploadBy,
        String storageProvider,
        Instant update_at
) {
}
