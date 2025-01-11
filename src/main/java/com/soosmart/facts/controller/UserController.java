package com.soosmart.facts.controller;


import com.soosmart.facts.dto.user.ResponseUtilisateur;
import com.soosmart.facts.dto.user.SaveUtilisateurDTO;
import com.soosmart.facts.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {
    private UtilisateurService utilisateurService;

    @PostMapping("")
    public ResponseEntity<ResponseUtilisateur> inscription(@Valid @RequestBody SaveUtilisateurDTO saveUtilisateurDTO) {
        return ResponseEntity.status(200).body(this.utilisateurService.save(saveUtilisateurDTO));
    }
}
