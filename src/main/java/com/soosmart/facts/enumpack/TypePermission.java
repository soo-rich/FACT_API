package com.soosmart.facts.enumpack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypePermission {
    SUPER_ADMINISTRATEUR_CREATE,
    SUPER_ADMINISTRATEUR_READ,
    SUPER_ADMINISTRATEUR_UPDATE,
    SUPER_ADMINISTRATEUR_DELETE,

    ADMINISTRATEUR_CREATE,
    ADMINISTRATEUR_READ,
    ADMINISTRATEUR_UPDATE,
    ADMINISTRATEUR_DELETE,


    UTILISATEUR_CREATE,
    UTILISATEUR_READ,
    UTILISATEUR_UPDATE;

    private String libelle;
}
