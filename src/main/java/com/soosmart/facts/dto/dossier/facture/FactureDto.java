package com.soosmart.facts.dto.dossier.facture;

import com.soosmart.facts.dto.articleQuantite.ArticleQuantiteDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FactureDto(
        UUID id,
        String reference,
        String numero,
        String numeroBorderau,
        List<ArticleQuantiteDTO> articleQuantiteslist,
        Float total_ht,
        Float total_ttc,
        Float total_tva,
        String client,
        Instant date,
        String signby,
        UUID uniqueIdDossier

) {
}
