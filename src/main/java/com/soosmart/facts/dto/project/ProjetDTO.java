package com.soosmart.facts.dto.project;

import java.time.Instant;
import java.util.UUID;

public record ProjetDTO(
        UUID id,
        String projet_type,
        String description,
        Boolean offre,
        String client,
        Instant createdat,
        Instant update_at
) {
}
