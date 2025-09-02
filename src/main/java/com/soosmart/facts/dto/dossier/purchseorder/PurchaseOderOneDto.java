package com.soosmart.facts.dto.dossier.purchseorder;

import com.soosmart.facts.dto.dossier.borderau.BorderauDto;
import com.soosmart.facts.dto.dossier.proforma.ProformaDTO;
import com.soosmart.facts.dto.file.FileMetaDataDto;

public record PurchaseOderOneDto(
        BorderauDto borderauDto,
        ProformaDTO proformaDTO,
        FileMetaDataDto file
) {
}
