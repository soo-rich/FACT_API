package com.soosmart.facts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Proforma {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    private UUID id;

    private String reference; // le nom que portera la pro
    @Column(unique = true)
    private String numero; // le numero de la profoma sa reference
    private String signedBy; // le nom de la personne qui a signé la proforma

    @Builder.Default
    private Boolean adopted = false;
    private Float total_ht;
    private Float total_ttc;
    private Float total_tva;

    @ManyToOne
    private Client client;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ArticleQuantite> articleQuantiteList = new ArrayList<>();

    @ManyToOne
    private Projet projet;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant create_at;

    @UpdateTimestamp
    private Instant update_at;

    @Override
    public String toString() {
        return this.reference;
    }
}
