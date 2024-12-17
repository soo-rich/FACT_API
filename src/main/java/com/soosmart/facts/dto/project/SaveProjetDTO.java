package com.soosmart.facts.dto.project;

public record SaveProjetDTO(
        String projet_type,
        String description,
        Boolean offre,
        String client_id
) {
}
