package com.soosmart.facts.dto.user;

import org.springframework.http.converter.HttpMessageNotReadableException;

public record SaveUtilisateurDTO(
        String nom,
        String prenom,
        String email,
        Integer numero,
        String username,
        String password
) {
    public SaveUtilisateurDTO{
        if (nom == null || nom.isBlank()) {
            throw new HttpMessageNotReadableException("Le nom ne doit pas etre null ou vide");
        }
        if (prenom == null || prenom.isBlank()) {
            throw new HttpMessageNotReadableException("Le prenom ne doit pas etre null ou vide");
        }
        if (email == null || email.isBlank()) {
            throw new HttpMessageNotReadableException("L'email ne doit pas etre null ou vide");
        }
        if (numero == null) {
            throw new HttpMessageNotReadableException("Le numero ne doit pas etre null");
        }
        if (username == null || username.isBlank()) {
            throw new HttpMessageNotReadableException("Le username ne doit pas etre null ou vide");
        }
        if (password == null || password.isBlank()) {
            throw new HttpMessageNotReadableException("Le password ne doit pas etre null ou vide");
        }
    }
}
