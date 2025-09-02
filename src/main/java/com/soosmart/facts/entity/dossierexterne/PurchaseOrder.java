package com.soosmart.facts.entity.dossierexterne;

import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.entity.file.FileMetadata;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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

}
