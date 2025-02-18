package com.soosmart.facts.dto.client;

import org.springframework.http.converter.HttpMessageNotReadableException;

public record SaveClientDTO(
        String lieu,
        String nom,
        String sigle,
        String telephone,
        Boolean potentiel
) {
    public SaveClientDTO {
        if (lieu.isBlank() || nom.isBlank() || sigle.isBlank() || telephone.isBlank()) {
            throw new HttpMessageNotReadableException("il manque des donne");
        }
    }
}
