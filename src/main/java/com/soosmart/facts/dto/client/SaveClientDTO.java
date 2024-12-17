package com.soosmart.facts.dto.client;

public record SaveClientDTO(
        String lieu,
        String nom,
        String sigle,
        String telephone,
        Boolean potentiel
) {
    public SaveClientDTO {
        if (lieu.isBlank() || nom.isBlank() || sigle.isBlank() || telephone.isBlank()) {
            throw new IllegalArgumentException("il manque des donne");
        }
    }
}
