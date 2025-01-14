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
public class Article {
    @Id
    @GeneratedValue(generator = "uuid", strategy = GenerationType.AUTO )
    private UUID id;
    private String libelle;
    private Float prix_unitaire;
    @CreationTimestamp
    @Column(updatable = false)
    private Instant create_at;
    @UpdateTimestamp
    private Instant update_at;

    @Builder.Default
    private Boolean supprimer = false;

    @Override
    public String toString(){
        return this.libelle;
    }
}
