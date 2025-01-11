package com.soosmart.facts.dto.client;

import com.soosmart.facts.exceptions.dto.DtoArgumentRquired;

public record SaveClientDTO(
        String lieu,
        String nom,
        String sigle,
        String telephone,
        Boolean potentiel
) {
    public SaveClientDTO {
        if (lieu.isBlank() || nom.isBlank() || sigle.isBlank() || telephone.isBlank()) {
            throw new DtoArgumentRquired("il manque des donne");
        }
    }
}
