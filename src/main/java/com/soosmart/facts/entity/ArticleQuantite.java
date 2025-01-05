package com.soosmart.facts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleQuantite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "proforma_id")
    private Proforma proforma;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    private Integer quantite;

    private Float prix_article;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant create_at;

    @UpdateTimestamp
    private Instant update_at;

}
