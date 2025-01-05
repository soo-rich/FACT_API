package com.soosmart.facts.dto.dossier.borderau;

import com.soosmart.facts.dto.articleQuantite.ArticleQuantiteDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record BorderauDto(
        UUID id,
        String reference,
        String numero,
        List<ArticleQuantiteDTO> articleQuantiteslist,
        Float total_ht,
        Float total_ttc,
        Float total_tva,
        String client,
        Instant date
) {
}
