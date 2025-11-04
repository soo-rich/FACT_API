package com.soosmart.facts.entity.dossierexterne;

import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.entity.file.FileMetadata;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(generator = "uuid", strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    private FileMetadata file;

    @Builder.Default
    private Boolean supprimer = false;

    @OneToOne
    private Proforma proforma;

    @OneToOne
    private Bordereau bordereau;

    @CreationTimestamp
    @Column(updatable = false, name = "createdat")
    private Instant createdat;

    @UpdateTimestamp
    private Instant update_at;

}
