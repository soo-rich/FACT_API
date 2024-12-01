package com.soosmart.facts.entity.jwt;

import com.soosmart.facts.entity.user.Utilisateur;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Jwt {
    @Id
    @GeneratedValue(generator = "uuid",strategy = GenerationType.AUTO)
    private UUID id;
    @Column(columnDefinition = "TEXT",unique = true )
    private String valeur;
    private boolean desactive;
    private boolean expire;
    @OneToOne(cascade = {CascadeType.PERSIST,  CascadeType.REMOVE})
    private RefreshToken refreshToken;

    @ManyToOne(cascade = {CascadeType.MERGE,  CascadeType.DETACH})
    private Utilisateur utilisateur;
}
