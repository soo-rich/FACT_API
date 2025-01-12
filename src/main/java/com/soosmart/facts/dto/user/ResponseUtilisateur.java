package com.soosmart.facts.dto.user;

import com.soosmart.facts.entity.user.Role;
import com.soosmart.facts.enumpack.TypeDeRole;

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
