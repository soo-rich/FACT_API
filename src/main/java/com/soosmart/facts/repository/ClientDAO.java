package com.soosmart.facts.repository;

import com.soosmart.facts.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientDAO extends JpaRepository<Client, UUID> {
    Optional<Client> findAllByNomContainsIgnoreCase(String nom);
    Page<Client> findAllBySupprimerIsFalse(Pageable pageable);
}
