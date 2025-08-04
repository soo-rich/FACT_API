package com.soosmart.facts.entity.dossier;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("BORDEREAU")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Bordereau extends Document {

    @OneToOne
    Proforma proforma;

    @Override
    public String toString() {
        return this.reference;
    }
}
