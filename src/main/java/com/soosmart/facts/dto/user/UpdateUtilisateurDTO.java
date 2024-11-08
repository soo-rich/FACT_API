package com.soosmart.facts.dto.user;

import java.util.UUID;

public record UpdateUtilisateurDTO(
        UUID id,
        String nom,
        String prenom,
        String email,
        Integer numero

) {
}
