package com.soosmart.facts.dto.dossier.proforma;

import com.soosmart.facts.dto.articleQuantite.SaveArticleWithQuantiteDTO;

import java.util.List;
import java.util.UUID;

public record SaveProformaWithArticleDTO(
        UUID projet_id,
        UUID client_id,
        String reference,
        List<SaveArticleWithQuantiteDTO> articleQuantiteslist
) {
}
