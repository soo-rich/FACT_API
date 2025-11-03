package com.soosmart.facts.dto.user;

import com.soosmart.facts.enumpack.TypeDeRole;

import java.util.UUID;

public record UpdateUtilisateurDTO(
        UUID id,
        String nom,
        String prenom,
        String email,
        String numero,
        TypeDeRole role

) {
}
