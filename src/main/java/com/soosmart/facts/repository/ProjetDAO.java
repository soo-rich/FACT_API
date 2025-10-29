package com.soosmart.facts.repository;

import com.soosmart.facts.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjetDAO extends JpaRepository<Projet, UUID> {
    List<Projet> findAllByProjetTypeContainsIgnoreCase(String search);

    List<Projet> findAll();
}
