package com.soosmart.facts.Implement;

import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.dto.user.ResponseUtilisateur;
import com.soosmart.facts.dto.user.SaveUtilisateurDTO;
import com.soosmart.facts.dto.user.UpdateUtilisateurDTO;
import com.soosmart.facts.dto.user.authentication.ChangePasswordDTO;
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
import com.soosmart.facts.security.user.UtilisateurConnecteServie;
import com.soosmart.facts.service.UtilisateurService;
import com.soosmart.facts.utils.EmailService;
import com.soosmart.facts.utils.pagination.PageMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UtilisateurImpl implements UtilisateurService {

    private final UtilisateurDAO utilisateurDAO;
    private ResponseMapper responseMapper;
    private BCryptPasswordEncoder passwordEncoder;
    private final UtilisateurConnecteServie utilisateurConnecteServie;
    private final EmailService emailService;

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
                                    .build())
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
    public CustomPageResponse<ResponseUtilisateur> findAll(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(
                this.utilisateurDAO.findByRole_LibelleNotIn(PageMapperUtils.createPageableWithoutSearch(paginatedRequest), List.of(TypeDeRole.SUPER_ADMIN)),
                this.responseMapper::responseUtilisateur);
    }

    @Override
    public ResponseUtilisateur findByEmail(String email) {
        Optional<Utilisateur> utilisateur = this.utilisateurDAO.findByEmail(email);
        if (utilisateur.isPresent()) {
            return this.responseMapper.responseUtilisateur(utilisateur.get());
        } else {
            throw new EntityNotFound("Utilisateur non trouvé");
        }

    }

    @Override
    public ResponseUtilisateur findByUsername(String username) {
        return this.responseMapper.responseUtilisateur(this.utilisateurDAO.findByUsername(username)
                .orElseThrow(() -> new EntityNotFound("Utilisateur non trouvé")));
    }

    @Override
    public ResponseUtilisateur save(SaveUtilisateurDTO utilisateur) {
        Random rand = new Random();
        String defaultPassword = utilisateur.nom() + rand.nextDouble();
        Utilisateur user = Utilisateur.builder()
                .nom(utilisateur.nom())
                .prenom(utilisateur.prenom())
                .email(utilisateur.email())
                .numero(utilisateur.numero())
                .username(utilisateur.username())
                .mdp(passwordEncoder.encode(defaultPassword))
                .role(
                        utilisateur.role().name().isBlank()
                                ?
                                Role.builder()
                                        .libelle(TypeDeRole.USER)
                                        .build()
                                :
                                Role.builder()
                                        .libelle(utilisateur.role())
                                        .build()
                )
                .build();

        if (this.verifierUtilisateurEmail(user)) {
            Utilisateur save = this.utilisateurDAO.save(user);
            // Send default password email
            this.emailService.sendDefaultPasswordMail(save.getEmail(), defaultPassword, save.getUsername());
            return new ResponseUtilisateur(
                    save.getId(),
                    save.getNom(),
                    save.getPrenom(),
                    save.getNumero(),
                    save.getEmail(),
                    save.getUsername(),
                    save.getRole().getLibelle().name(),
                    save.getCreatedat(),
                    save.getActif());
        } else {
            throw new BadEmail("Email invalide");
        }
    }

    @Override
    public ResponseUtilisateur update(UUID id, UpdateUtilisateurDTO utilisateur) {

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
                userUpdate.setRole(
                        utilisateur.role().name().equals(userUpdate.getRole().getLibelle().name())
                                ?
                                userUpdate.getRole()
                                :
                                Role.builder()
                                        .libelle(utilisateur.role())
                                        .build()
                );

                return this.responseMapper.responseUtilisateur(this.utilisateurDAO.save(userUpdate));
            } else {
                throw new EntityNotFound("Utilisateur non trouvé");
            }
        }
    }

    @Override
    public void delete(UUID id) {
        this.utilisateurDAO.deleteById(id);
    }

    @Override
    public Boolean activateUser(UUID id) {
        Optional<Utilisateur> user = this.utilisateurDAO.findById(id);
        if (user.isPresent()) {
            Utilisateur utilisateur = user.get();
            utilisateur.setActif(!utilisateur.getActif());
            this.utilisateurDAO.save(utilisateur);
            return utilisateur.getActif();
        } else {
            throw new EntityNotFound("Utilisateur non trouvé");
        }
    }

    @Override
    public ResponseUtilisateur userconnecte() {
        return this.responseMapper.responseUtilisateur(this.utilisateurConnecteServie.getUtilisateurConnecte());
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utilisateurDAO
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Aucun utilisateur trouve avec cet Username ou Email"));
    }

    @Override
    public Boolean changePassword(ChangePasswordDTO changePasswordDTO) {
        Utilisateur utilisateur = this.utilisateurConnecteServie.getUtilisateurConnecte();
        if (utilisateur == null) {
            throw new EntityNotFound("Utilisateur non trouvé");
        }
        if (!passwordEncoder.matches(changePasswordDTO.oldPassword(), utilisateur.getMdp())) {
            throw new EntityNotFound("Mot de passe incorrect");
        }
        utilisateur.setMdp(passwordEncoder.encode(changePasswordDTO.newPassword()));
        this.utilisateurDAO.save(utilisateur);
        return true;
    }
}
