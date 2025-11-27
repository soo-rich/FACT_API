package com.soosmart.facts.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Utilisateur implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 50)
    private String nom;
    private String prenom;
    @Column(unique = true)
    private String email;
    @Column(unique = true, length = 25)
    private String numero;
    @Column(unique = true)
    private String username;
    @Column(name = "mot_de_passe")
    private String mdp;

    @OneToOne(cascade = CascadeType.ALL)
    private Role role;

    private String image;




    @CreationTimestamp
    @Column(updatable = false, name = "createdat")
    private Instant createdat;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Builder.Default
    private Boolean actif = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getLibelle().getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.mdp;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.actif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isEnabled() {
        return this.actif;
    }
}
