package com.soosmart.facts.repository.dossier;

import com.soosmart.facts.entity.dossier.Facture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FactureDao extends JpaRepository<Facture, UUID> {
    @Query("SELECT count(f) from Facture f  WHERE f.createdat >= :startOfDay and f.createdat < :endOfDay")
    Long countFacturesCreateToday(Instant startOfDay, Instant endOfDay);

    Optional<Facture> findByNumero(String numero);

    Page<Facture> findAllByDeletedIsFalse(Pageable pageable);

    @Query("SELECT COUNT (f) FROM Facture f WHERE f.deleted = false ")
    int countAllByDeletedIsFalse();

    @Query("SELECT SUM(f.total_ttc) FROM Facture f WHERE f.deleted = false and f.isPaid= true")
    Double getTotalFacture();

    @Query("SELECT SUM(f.total_ttc) FROM Facture f WHERE f.deleted = false and f.isPaid= false")
    Double getTotalFactureUnPaiud();
}
