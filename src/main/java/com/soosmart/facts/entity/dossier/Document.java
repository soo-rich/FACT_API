package com.soosmart.facts.entity.dossier;

import com.soosmart.facts.entity.ArticleQuantite;
import com.soosmart.facts.entity.Client;
import com.soosmart.facts.entity.Projet;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "document_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class Document {
    String reference; // le nom que portera la pro
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    private UUID id;
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

    @Builder.Default
    private Boolean deleted = false;
}
