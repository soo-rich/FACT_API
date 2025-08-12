package com.soosmart.facts.dto.Article;

import java.util.UUID;

public record ArticleDTO(
        UUID id,
        String libelle,
        String description,
        Float prix_unitaire
) {
}
