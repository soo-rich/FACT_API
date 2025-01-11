package com.soosmart.facts.dto.user;

import com.soosmart.facts.exceptions.dto.DtoArgumentRquired;

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
            throw new DtoArgumentRquired("Le nom ne doit pas etre null ou vide");
        }
        if (prenom == null || prenom.isBlank()) {
            throw new DtoArgumentRquired("Le prenom ne doit pas etre null ou vide");
        }
        if (email == null || email.isBlank()) {
            throw new DtoArgumentRquired("L'email ne doit pas etre null ou vide");
        }
        if (numero == null) {
            throw new DtoArgumentRquired("Le numero ne doit pas etre null");
        }
        if (username == null || username.isBlank()) {
            throw new DtoArgumentRquired("Le username ne doit pas etre null ou vide");
        }
        if (password == null || password.isBlank()) {
            throw new DtoArgumentRquired("Le password ne doit pas etre null ou vide");
        }
    }
}
