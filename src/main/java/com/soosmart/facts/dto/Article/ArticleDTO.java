package com.soosmart.facts.dto.Article;

import java.util.UUID;

public record ArticleDTO(
        UUID id,
        String libelle,
        Double prix_unitaiare
) {
}
