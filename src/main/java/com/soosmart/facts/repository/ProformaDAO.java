package com.soosmart.facts.repository;

import com.soosmart.facts.entity.Proforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProformaDAO extends JpaRepository<Proforma, UUID> {

    @Query("SELECT COUNT (p) FROM Proforma p WHERE p.create_at >= :startOfDay and p.create_at < :endOfDay")
    Long countProformasCreateToday(@Param("startOfDay") Instant startOfDay, @Param("endOfDay")Instant endOfDay);

    Optional<Proforma> findByNumero(String numero);
}
