package com.soosmart.facts.service;

import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.dto.user.ResponseUtilisateur;
import com.soosmart.facts.dto.user.SaveUtilisateurDTO;
import com.soosmart.facts.dto.user.UpdateUtilisateurDTO;
import com.soosmart.facts.dto.user.authentication.ChangePasswordDTO;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UtilisateurService extends UserDetailsService {

    void createSuprerAdmin(String email, String username, String password);
    CustomPageResponse<ResponseUtilisateur> findAll(PaginatedRequest paginatedRequest);
    ResponseUtilisateur findByEmail(String email);
    ResponseUtilisateur findByUsername(String username);
    ResponseUtilisateur save(SaveUtilisateurDTO utilisateur);
    ResponseUtilisateur update(UUID id, UpdateUtilisateurDTO utilisateur);
    void delete(UUID id);
    Boolean activateUser(UUID id);
    ResponseUtilisateur userconnecte();
    UserDetails loadUserByUsername(String username);

    Boolean changePassword(@Valid ChangePasswordDTO changePasswordDTO);
}
