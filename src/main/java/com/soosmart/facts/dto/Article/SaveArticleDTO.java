package com.soosmart.facts.dto.Article;

import com.soosmart.facts.exceptions.dto.DtoArgumentRquired;

public record SaveArticleDTO(
        String libelle,
        Float prix
) {
    public SaveArticleDTO{
        if (libelle==null || libelle.isBlank()){
            throw new DtoArgumentRquired("le libelle ne doit pas etre null");
        }
        if (prix ==null || prix.isNaN()){
            throw new DtoArgumentRquired("le prix doit pas etre null");
        }
    }
}
