package com.soosmart.facts.dto.client;

import java.util.UUID;

public record ClientDTO(
        UUID id,
        String lieu,
        String nom,
        String sigle,
        String telephone,
        Boolean potentiel
) {
}
