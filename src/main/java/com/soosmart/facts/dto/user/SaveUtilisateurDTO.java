package com.soosmart.facts.dto.user;

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
            throw new IllegalArgumentException("Le nom ne doit pas etre null ou vide");
        }
        if (prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException("Le prenom ne doit pas etre null ou vide");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("L'email ne doit pas etre null ou vide");
        }
        if (numero == null) {
            throw new IllegalArgumentException("Le numero ne doit pas etre null");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Le username ne doit pas etre null ou vide");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Le password ne doit pas etre null ou vide");
        }
    }
}
