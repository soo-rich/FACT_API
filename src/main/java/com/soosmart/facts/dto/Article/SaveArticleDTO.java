package com.soosmart.facts.dto.Article;

import org.springframework.http.converter.HttpMessageNotReadableException;

public record SaveArticleDTO(
        String libelle,
        Float prix_unitaire
) {
    public SaveArticleDTO{
        if (libelle==null || libelle.isBlank()){
            throw new HttpMessageNotReadableException("le libelle ne doit pas etre null");
        }
        if (prix_unitaire ==null || prix_unitaire.isNaN()){
            throw new HttpMessageNotReadableException("le prix doit pas etre null");
        }
    }
}
