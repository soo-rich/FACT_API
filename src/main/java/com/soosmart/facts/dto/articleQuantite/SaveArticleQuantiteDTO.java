package com.soosmart.facts.dto.articleQuantite;

import java.util.UUID;

public record SaveArticleQuantiteDTO(
        UUID article_id,
        Integer quantite,
        float prix_article
) {
}
