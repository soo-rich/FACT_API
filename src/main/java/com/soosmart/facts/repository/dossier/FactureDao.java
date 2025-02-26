package com.soosmart.facts.repository.dossier;

import com.soosmart.facts.entity.dossier.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FactureDao extends JpaRepository<Facture, UUID> {
    @Query("SELECT count(f) from Facture f  WHERE f.create_at >= :startOfDay and f.create_at < :endOfDay")
    Long countFacturesCreateToday(Instant startOfDay, Instant endOfDay);

    Optional<Facture> findByNumero(String numero);

    List<Facture> findAllByDeletedIsFalse();

    @Query("SELECT COUNT (f) FROM Facture f WHERE f.deleted = false ")
    int countAllByDeletedIsFalse();
}
