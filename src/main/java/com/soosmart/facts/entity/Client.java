package com.soosmart.facts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Client {

    @Id
    @GeneratedValue(generator = "uuid", strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private UUID id;
    private String lieu;
    private String nom;
    private String sigle;
    private String telephone;
    private Boolean potentiel;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant create_at;

    @UpdateTimestamp
    private Instant update_at;

    @Override
    public String toString() {
        return this.nom;
    }
}
