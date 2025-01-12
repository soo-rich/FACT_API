package com.soosmart.facts.dto.Article;

import java.util.UUID;

public record ArticleDTO(
        UUID id,
        String libelle,
        Float prix_unitaire
) {
}
