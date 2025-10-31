package com.soosmart.facts.dto.articleQuantite;

public record SaveArticleWithQuantiteDTO(
        String libelle,
        String description,
        Float prix_unitaire,
        Integer quantite
){}
