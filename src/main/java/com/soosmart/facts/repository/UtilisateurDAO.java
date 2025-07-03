package com.soosmart.facts.repository;

import com.soosmart.facts.entity.user.Utilisateur;
import com.soosmart.facts.enumpack.TypeDeRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtilisateurDAO extends JpaRepository<Utilisateur, UUID> {
    Optional<Utilisateur> findByEmail(String email);
    Optional<Utilisateur> findByUsername(String username);
    Optional<Utilisateur> findByRole_Libelle(TypeDeRole libelle);
    Page<Utilisateur> findByRole_LibelleNotIn(Pageable pageable, Iterable<TypeDeRole> excludRoles);
}
