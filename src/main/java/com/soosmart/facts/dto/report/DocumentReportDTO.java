package com.soosmart.facts.dto.report;

import com.soosmart.facts.dto.articleQuantite.ArticleQuantiteDTO;
import com.soosmart.facts.dto.client.ClientDTO;

import java.util.Date;
import java.util.List;

public record DocumentReportDTO(
        String reference,
        String numero,
        Date date,
        List<ArticleQuantiteDTO> articleQuantiteslist,
        ClientDTO client,
        String role,
        String signby,
        Double total_ht,
        Double tva,
        Double total_ttc) {
}
