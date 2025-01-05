package com.soosmart.facts.entity.dossier;


import jakarta.persistence.*;
import lombok.*;
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
