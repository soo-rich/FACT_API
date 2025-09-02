package com.soosmart.facts.dto.dossier.purchseorder;

import com.soosmart.facts.dto.file.FileMetaDataDto;

public record PurchaseOderDto(
        String numeroProforma,
        String numeroBordereau,
        FileMetaDataDto file
) {
}
