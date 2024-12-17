package com.soosmart.facts.controller;

import com.soosmart.facts.dto.project.ProjetDTO;
import com.soosmart.facts.dto.project.SaveProjetDTO;
import com.soosmart.facts.dto.project.UpdateProjet;
import com.soosmart.facts.service.ProjetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projet")
@AllArgsConstructor
public class ProjetController {
    private ProjetService projetService;

    @PostMapping
    public ResponseEntity<ProjetDTO> save(@RequestBody SaveProjetDTO saveProjetDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.projetService.save(saveProjetDTO));
    }

    @GetMapping
    public ResponseEntity<List<ProjetDTO>> getall() {
        return ResponseEntity.status(HttpStatus.OK).body(this.projetService.list());
    }

    @PutMapping("{id}")
    public ResponseEntity<ProjetDTO> update(@PathVariable UUID id, @RequestBody UpdateProjet updateProjet) {
        return ResponseEntity.status(HttpStatus.OK).body(this.projetService.update(id, updateProjet));
    }

    @PatchMapping("{id}")
    public ResponseEntity<Boolean> changeOffre(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.projetService.changeOffre(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        this.projetService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
