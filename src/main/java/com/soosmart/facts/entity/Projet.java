package com.soosmart.facts.entity;

import com.soosmart.facts.entity.dossier.Proforma;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany
    private List<Proforma> proformaList;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Instant created_at;

    @UpdateTimestamp
    private Instant update_at;

}
