package com.soosmart.facts.repository.dossier;

import com.soosmart.facts.entity.dossier.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentDAO extends JpaRepository<Document, UUID> {
    @Query
            (
                    value = "SELECT document_type FROM document",
                    nativeQuery = true
            )
    List<String> findAllByDocumentType ();
}