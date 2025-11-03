package com.soosmart.facts.controller;


import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.dto.user.ResponseUtilisateur;
import com.soosmart.facts.dto.user.SaveUtilisateurDTO;
import com.soosmart.facts.dto.user.UpdateUtilisateurDTO;
import com.soosmart.facts.dto.user.authentication.ChangePasswordDTO;
import com.soosmart.facts.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {
    private UtilisateurService utilisateurService;

    @PostMapping("")
    public ResponseEntity<ResponseUtilisateur> inscription(@Valid @RequestBody SaveUtilisateurDTO saveUtilisateurDTO) {
        return ResponseEntity.status(200).body(this.utilisateurService.save(saveUtilisateurDTO));
    }

    @GetMapping("me")
    public ResponseEntity<ResponseUtilisateur> getMe() {
        return ResponseEntity.status(HttpStatus.OK).body(this.utilisateurService.userconnecte());
    }

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(this.utilisateurService.changePassword(changePasswordDTO));
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getUser(@RequestParam(value = "email", required = false)  String email, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "pagesize", defaultValue = "10") int pagesize, @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        if (email == null) {
            return ResponseEntity.status(HttpStatus.OK).body(this.utilisateurService.findAll(new PaginatedRequest(page, pagesize, search)));
        }
        else {
            System.out.println(email);
            return ResponseEntity.status(HttpStatus.OK).body(List.of(this.utilisateurService.findByEmail(email)));
        }
    }

    @GetMapping("{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Boolean> activateUser(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body( this.utilisateurService.activateUser(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseUtilisateur> updateUser(@PathVariable("id")UUID id, @Valid @RequestBody UpdateUtilisateurDTO updateUtilisateurDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.utilisateurService.update(id, updateUtilisateurDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {
        this.utilisateurService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
