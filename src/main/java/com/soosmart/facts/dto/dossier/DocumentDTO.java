package com.soosmart.facts.dto.dossier;

import com.soosmart.facts.dto.articleQuantite.ArticleQuantiteDTO;
import com.soosmart.facts.dto.client.ClientDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DocumentDTO(
        UUID id,
        String reference,
        String numero,
        List<ArticleQuantiteDTO> articleQuantiteslist,
        Float total_ht,
        Float total_ttc,
        Float total_tva,
        String total_letters,
        ClientDTO client,
        Instant date,
        Boolean paied,
        String signby
) {
}
