package com.soosmart.facts.entity.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor@NoArgsConstructor
@Builder
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(generator = "uuid",strategy = GenerationType.AUTO)
    private UUID id;
    private Boolean expired;
    private String value;
    @CreationTimestamp
    private Instant creationDate;
    private Instant expirationDate;
}
