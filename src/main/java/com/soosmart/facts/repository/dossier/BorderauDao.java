package com.soosmart.facts.repository.dossier;

import com.soosmart.facts.entity.dossier.Bordereau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BorderauDao extends JpaRepository<Bordereau, UUID> {
    @Query("SELECT count(b) FROM Bordereau b WHERE b.create_at >= :startOfDay and b.create_at < :endOfDay")
    Long countBordereauxCreateToday(Instant startOfDay, Instant endOfDay);

    Bordereau findByNumero(String numero);

    List<Bordereau> findAllByDeletedIsFalse();

    List<Bordereau> findAllByDeletedIsFalseAndAdoptedIsFalse();
}
