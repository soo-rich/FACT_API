package com.soosmart.facts.entity.dossier;


import com.soosmart.facts.entity.ArticleQuantite;
import com.soosmart.facts.entity.Client;
import com.soosmart.facts.entity.Projet;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("PROFORMA")
public class Proforma extends Document {

    @Column(nullable = false)
    @Builder.Default
    private Boolean oldversion = false;

    @ManyToOne
    private Client client;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ArticleQuantite> articleQuantiteList;

    @ManyToOne
    private Projet projet;

    @Override
    public String toString() {
        return this.reference;
    }
}
