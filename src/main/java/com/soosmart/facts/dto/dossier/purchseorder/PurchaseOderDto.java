package com.soosmart.facts.dto.dossier.purchseorder;

import java.util.UUID;

import com.soosmart.facts.dto.file.FileMetaDataDto;

public record PurchaseOderDto(
        UUID id,
        String numeroProforma,
        String numeroBordereau,
        FileMetaDataDto file
) {
}
