package com.soosmart.facts.dto.user;

import java.time.Instant;
import java.util.UUID;

public record ResponseUtilisateur(
        UUID id,
        String nom,
        String prenom,
        Integer telephone,
        String email,
        String username,
        String role,
        Instant dateCreation,
        Boolean actif
) {
}
