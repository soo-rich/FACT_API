package com.soosmart.facts.entity.dossier;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("PROFORMA")
public class Proforma extends Document {


    @Override
    public String toString() {
        return this.reference;
    }
}
