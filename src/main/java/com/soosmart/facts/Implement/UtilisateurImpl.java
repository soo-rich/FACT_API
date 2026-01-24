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
import com.soosmart.facts.service.file.FileStorageService;
import com.soosmart.facts.utils.EmailService;
import com.soosmart.facts.utils.pagination.PageMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.soosmart.facts.utils.FileUtlis.generateUniqueFileName;
import static com.soosmart.facts.utils.FileUtlis.getFileExtension;

@Service
@AllArgsConstructor
public class UtilisateurImpl implements UtilisateurService {

    private final UtilisateurDAO utilisateurDAO;
    private ResponseMapper responseMapper;
    private BCryptPasswordEncoder passwordEncoder;
    private final UtilisateurConnecteServie utilisateurConnecteServie;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;

    @Override
    public void createSuprerAdmin(String email, String username, String password) {
        Optional<Utilisateur> superAdmin = this.utilisateurDAO.findByRole_Libelle(TypeDeRole.SYS_ADMIN);
        if (superAdmin.isPresent()) {
            throw new SuperAdminExeciste("Super Admin Existe");
        } else {
            Utilisateur utilisateur = Utilisateur.builder()
                    .email(email)
                    .username(username)
                    .mdp(passwordEncoder.encode(password))
                    .role(
                            Role.builder()
                                    .libelle(TypeDeRole.SYS_ADMIN)
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
                this.utilisateurDAO.findByRole_LibelleNotIn(
                        PageMapperUtils.createPageableWithoutSearch(paginatedRequest), List.of(TypeDeRole.SYS_ADMIN)),
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
    public ResponseUtilisateur save(SaveUtilisateurDTO utilisateur, MultipartFile image) {
        Random rand = new Random();
        String defaultPassword = utilisateur.username() + "@" + (1000 + rand.nextInt(9000));
        Utilisateur user = Utilisateur.builder()
                .nom(utilisateur.nom())
                .prenom(utilisateur.prenom())
                .email(utilisateur.email())
                .numero(utilisateur.numero())
                .username(utilisateur.username())
                .mdp(passwordEncoder.encode(defaultPassword))
                .role(
                        utilisateur.role().name().isBlank()
                                ? Role.builder()
                                        .libelle(TypeDeRole.USER)
                                        .build()
                                : Role.builder()
                                        .libelle(utilisateur.role())
                                        .build())
                .image(image!=null ? this.fileStorageService.uploadFileToSubFolder(image,
                        String.format("/%s/%s", "user",
                                generateUniqueFileName(
                                       String.format("%s_%s_%s", utilisateur.nom(),utilisateur.nom() , UUID.randomUUID().toString()),
                                        getFileExtension(image.getOriginalFilename())))) : null)
                .build();

        if (this.verifierUtilisateurEmail(user)) {
            Utilisateur save = this.utilisateurDAO.save(user);
            // Send default password email
            this.emailService.sendDefaultPasswordMail(save.getEmail(), defaultPassword, save.getUsername());
            return this.responseMapper.responseUtilisateur(save);
        } else {
            throw new BadEmail("Email invalide");
        }
    }

    @Override
    public ResponseUtilisateur update(UUID id, UpdateUtilisateurDTO utilisateur, MultipartFile image) {

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
                userUpdate.setImage(image!=null ? this.fileStorageService.uploadFileToSubFolder(image,
                        String.format("/%s/%s", "user",
                                generateUniqueFileName(
                                       String.format("%s_%s_%s", utilisateur.nom(),utilisateur.nom() , UUID.randomUUID().toString()),
                                        getFileExtension(image.getOriginalFilename())))) : null
                );
                userUpdate.setRole(
                        utilisateur.role().name().equals(userUpdate.getRole().getLibelle().name())
                                ? userUpdate.getRole()
                                : Role.builder()
                                        .libelle(utilisateur.role())
                                        .build());

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

    @Override
    public Boolean forgetPassword(String email) {
        Optional<Utilisateur> user = this.utilisateurDAO.findByEmail(email);
        if (user.isPresent()) {
            Utilisateur utilisateur = user.get();
            Random rand = new Random();
            String newPassword = utilisateur.getUsername() + "@" + (1000 + rand.nextInt(9000));
            utilisateur.setMdp(passwordEncoder.encode(newPassword));
            this.utilisateurDAO.save(utilisateur);
            // Send new password email
            this.emailService.sendForgotPasswordEmail(utilisateur.getEmail(), newPassword);
            return true;
        } else {
            throw new EntityNotFound("Utilisateur non trouvé");
        }
    }
}
