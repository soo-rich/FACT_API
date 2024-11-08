package com.soosmart.facts.controller;


import com.soosmart.facts.dto.user.SaveUtilisateurDTO;
import com.soosmart.facts.service.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {
    private UtilisateurService utilisateurService;

    @PostMapping("")
    public ResponseEntity<?> inscription(@RequestBody SaveUtilisateurDTO saveUtilisateurDTO) {
        try {
            return ResponseEntity.status(200).body(this.utilisateurService.save(saveUtilisateurDTO));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
