package com.soosmart.facts.Implement;

import com.soosmart.facts.dto.user.ResponseUtilisateur;
import com.soosmart.facts.dto.user.SaveUtilisateurDTO;
import com.soosmart.facts.dto.user.UpdateUtilisateurDTO;
import com.soosmart.facts.entity.user.Role;
import com.soosmart.facts.entity.user.Utilisateur;
import com.soosmart.facts.enumpack.TypeDeRole;
import com.soosmart.facts.exceptions.EntityNotFound;
import com.soosmart.facts.exceptions.NotSameId;
import com.soosmart.facts.exceptions.user.BadEmail;
import com.soosmart.facts.exceptions.user.EmailExiste;
import com.soosmart.facts.exceptions.user.SuperAdminExeciste;
import com.soosmart.facts.exceptions.user.UsernameExiste;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.UtilisateurDAO;
import com.soosmart.facts.service.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UtilisateurImpl implements UtilisateurService, UserDetailsService {

    private final UtilisateurDAO utilisateurDAO;
    private ResponseMapper responseMapper;
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public void createSuprerAdmin(String email, String username, String password) {
        Optional<Utilisateur> superAdmin = this.utilisateurDAO.findByRole_Libelle(TypeDeRole.SUPER_ADMIN);
        if (superAdmin.isPresent()) {
           throw new SuperAdminExeciste("Super Admin Existe");
        } else {
            Utilisateur utilisateur = Utilisateur.builder()
                    .email(email)
                    .username(username)
                    .mdp(passwordEncoder.encode(password))
                    .role(
                            Role.builder()
                                    .libelle(TypeDeRole.SUPER_ADMIN)
                                    .build()
                    )
                    .actif(true)
                    .build();
            if (this.verifierUtilisateurEmail(utilisateur)) {
                this.utilisateurDAO.save(utilisateur);
            } else {
                throw new BadEmail("Email invalide");
            }
        }
    }

    @Override
    public List<ResponseUtilisateur> findAll() {
        return this.utilisateurDAO.findAll().stream().map(
                responseMapper::responseUtilisateur
        ).collect(Collectors.toList());
    }

    @Override
    public Utilisateur findByEmail(String email) {
        Optional<Utilisateur> utilisateur = this.utilisateurDAO.findByEmail(email);
        return utilisateur.orElseThrow(() -> new EntityNotFound("Utilisateur non trouvé"));

    }

    @Override
    public Utilisateur findByUsername(String username) {
        return this.utilisateurDAO.findByUsername(username).orElseThrow(() -> new EntityNotFound("Utilisateur non trouvé"));
    }

    @Override
    public ResponseUtilisateur save(SaveUtilisateurDTO utilisateur) {

        Utilisateur user = Utilisateur.builder()
                .nom(utilisateur.nom())
                .prenom(utilisateur.prenom())
                .email(utilisateur.email())
                .numero(utilisateur.numero())
                .username(utilisateur.username())
                .mdp(passwordEncoder.encode(utilisateur.password()))
                .role(
                        Role.builder()
                                .libelle(TypeDeRole.USER)
                                .build()
                )
                .build();

        if (this.verifierUtilisateurEmail(user)) {
            Utilisateur save = this.utilisateurDAO.save(user);
            return new ResponseUtilisateur(
                    save.getId(),
                    save.getNom(),
                    save.getPrenom(),
                    save.getNumero(),
                    save.getEmail(),
                    save.getUsername(),
                    save.getRole().getLibelle().name(),
                    save.getCreatedAt()
            );
        } else {
            throw new BadEmail("Email invalide");
        }
    }

    @Override
    public Utilisateur update(UUID id, UpdateUtilisateurDTO utilisateur) {

        if (!utilisateur.id().toString().equals(id.toString())) {
            throw new NotSameId("Id invalide");
        } else {

            Optional<Utilisateur> user = this.utilisateurDAO.findById(utilisateur.id()).stream().findFirst();
            if (user.isPresent()) {
                Utilisateur userUpdate = user.get();
                userUpdate.setNom(utilisateur.nom());
                userUpdate.setPrenom(utilisateur.prenom());
                userUpdate.setEmail(utilisateur.email());
                userUpdate.setNumero(utilisateur.numero());

                return this.utilisateurDAO.save(userUpdate);
            } else {
                throw new EntityNotFound("Utilisateur non trouvé");
            }
        }
    }

    @Override
    public void delete(UUID id) {
        this.utilisateurDAO.deleteById(id);
    }

    private Boolean verifierUtilisateurEmail(Utilisateur utilisateur) {
        if (!utilisateur.getEmail().contains("@") || !utilisateur.getEmail().contains(".")) {
            throw new BadEmail("Email invalide");
        } else {
            Optional<Utilisateur> userEmail = this.utilisateurDAO.findByEmail(utilisateur.getEmail());
            if (userEmail.isPresent()) {
                throw new EmailExiste("Email existe deja");
            } else {
                return verifierUtilisateurUsername(utilisateur);
            }
        }
    }

    private Boolean verifierUtilisateurUsername(Utilisateur utilisateur) {
        Optional<Utilisateur> user = this.utilisateurDAO.findByUsername(utilisateur.getUsername());
        if (user.isPresent()) {
            throw new UsernameExiste("Username existe deja");
        } else {
            return true;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username)  {
        return this.utilisateurDAO
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFound("Aucun utilisateur trouve avec cet Username ou Email"));
    }
}
