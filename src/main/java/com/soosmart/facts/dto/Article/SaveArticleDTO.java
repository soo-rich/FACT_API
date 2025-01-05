package com.soosmart.facts.dto.Article;

public record SaveArticleDTO(
        String libelle,
        Float prix
) {
    public SaveArticleDTO{
        if (libelle==null || libelle.isBlank()){
            throw new IllegalArgumentException("le libelle ne doit pas etre null");
        }
        if (prix ==null || prix.isNaN()){
            throw new IllegalArgumentException("le prix doit pas etre null");
        }
    }
}
