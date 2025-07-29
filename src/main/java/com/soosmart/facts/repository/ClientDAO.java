package com.soosmart.facts.repository;

import com.soosmart.facts.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientDAO extends JpaRepository<Client, UUID> {
    List<Client> findAllByNomContainsIgnoreCase(String nom);
    List<Client> findAllBySupprimerIsFalse();
    Page<Client> findAllBySupprimerIsFalse(Pageable pageable);
}
