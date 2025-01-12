package com.soosmart.facts.controller;


import com.soosmart.facts.dto.user.ResponseUtilisateur;
import com.soosmart.facts.dto.user.SaveUtilisateurDTO;
import com.soosmart.facts.dto.user.UpdateUtilisateurDTO;
import com.soosmart.facts.entity.user.Utilisateur;
import com.soosmart.facts.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @GetMapping
    public ResponseEntity<List<ResponseUtilisateur>> getUser(@RequestParam(value = "email", required = false)  String email) {
        if (email == null) {
            return ResponseEntity.status(HttpStatus.OK).body(this.utilisateurService.findAll());
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(List.of(this.utilisateurService.findByEmail(email)));
        }
    }

    @GetMapping("{id}/activate")
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
