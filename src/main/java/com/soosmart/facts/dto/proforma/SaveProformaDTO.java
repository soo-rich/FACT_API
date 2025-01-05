package com.soosmart.facts.dto.proforma;

import com.soosmart.facts.dto.articleQuantite.SaveArticleQuantiteDTO;

import java.util.List;
import java.util.UUID;

public record SaveProformaDTO(
        UUID projet_id,
        UUID client_id,
        String reference,
        List<SaveArticleQuantiteDTO> articleQuantiteslist
) {
}
