package com.soosmart.facts.entity.dossier;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("FACTURE")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Facture extends Document {

    @OneToOne
    Bordereau bordereau;

    @Builder.Default
    private Boolean isPaid = false; // Indique si la facture est payée ou non

    @Override
    public String toString() {
        return this.reference;
    }
}
