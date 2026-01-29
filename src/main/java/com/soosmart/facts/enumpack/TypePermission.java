package com.soosmart.facts.enumpack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypePermission {
    SYS_ADMIN_CREATE,
    SYS_ADMIN_READ,
    SYS_ADMIN_UPDATE,
    SYS_ADMIN_DELETE,

    ADMINISTRATEUR_CREATE,
    ADMINISTRATEUR_READ,
    ADMINISTRATEUR_UPDATE,
    ADMINISTRATEUR_DELETE,


    UTILISATEUR_CREATE,
    UTILISATEUR_READ,
    UTILISATEUR_UPDATE,

    CLIENT_READ;

    private String libelle;
}
