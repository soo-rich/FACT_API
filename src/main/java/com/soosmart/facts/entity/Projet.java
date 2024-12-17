package com.soosmart.facts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    private UUID id;

    private String projet_type;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Builder.Default
    private Boolean offre = false;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant create_at;

    @UpdateTimestamp
    private Instant update_at;

}
