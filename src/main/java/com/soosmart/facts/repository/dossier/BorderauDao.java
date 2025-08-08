package com.soosmart.facts.repository.dossier;

import com.soosmart.facts.entity.dossier.Bordereau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BorderauDao extends JpaRepository<Bordereau, UUID> {
    @Query("SELECT count(b) FROM Bordereau b WHERE b.createdat >= :startOfDay and b.createdat < :endOfDay")
    Long countBordereauxCreateToday(Instant startOfDay, Instant endOfDay);

    Optional<Bordereau> findByNumero(String numero);

    Page<Bordereau> findAllByDeletedIsFalse(Pageable pageable);

    Page<Bordereau> findAllByDeletedIsFalseAndAdoptedIsFalse(Pageable pageable);

    @Query("SELECT COUNT (b) FROM Bordereau b WHERE b.adopted = true")
    Long countBordereauxAdopedTrue();

    @Query("SELECT COUNT (b) FROM Bordereau b WHERE b.adopted = false")
    Long countBordereauxAdopedFalse();

    @Query("SELECT COUNT (b) FROM Bordereau b WHERE b.deleted = false ")
    Integer countAllByDeletedIsFalse();
}
