package com.soosmart.facts.repository.dossier;

import com.soosmart.facts.entity.dossier.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentDAO extends JpaRepository<Document, UUID> {
    @Query(value = "SELECT d.numero AS numero, d.createdat AS date, d.total_ttc AS total_ttc " +
            "FROM document d " +
            "LEFT JOIN proforma p ON d.id = p.id " +
            "LEFT JOIN facture f ON d.id = f.id " +
            "LEFT JOIN bordereau b ON d.id = b.id " +
            "WHERE d.deleted=false " +
            "ORDER BY d.createdat DESC " +
            "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Object[]> findAllByDeletedIsFalse(@Param("offset") int offset, @Param("limit") int limit);

    Integer countByDeletedIsFalse();

    @Query(value = "SELECT document_type AS DocumentType, COUNT(*) AS total FROM document GROUP BY document_type", nativeQuery = true)
    List<Object[]> countDocumentTypes();
}