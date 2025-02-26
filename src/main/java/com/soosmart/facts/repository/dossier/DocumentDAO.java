package com.soosmart.facts.repository.dossier;

import com.soosmart.facts.entity.dossier.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentDAO extends JpaRepository<Document, UUID> { }
