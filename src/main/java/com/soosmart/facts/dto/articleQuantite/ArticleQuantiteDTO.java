package com.soosmart.facts.dto.articleQuantite;

import java.util.UUID;

public record ArticleQuantiteDTO (
        UUID id,
        String article,
        Integer quantite,
        float prix_article
){
}
