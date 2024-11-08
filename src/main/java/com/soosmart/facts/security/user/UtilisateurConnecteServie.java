package com.soosmart.facts.security.user;

import com.soosmart.facts.entity.user.Utilisateur;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UtilisateurConnecteServie {

    public Utilisateur getUtilisateurConnecte() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (Utilisateur) authentication.getPrincipal();
        }
        return null;
    }
}
