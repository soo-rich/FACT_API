package com.soosmart.facts.controller;

import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.dto.project.ProjetDTO;
import com.soosmart.facts.dto.project.SaveProjetDTO;
import com.soosmart.facts.dto.project.UpdateProjet;
import com.soosmart.facts.service.ProjetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,value="/projet")
@AllArgsConstructor
public class ProjetController {
    private ProjetService projetService;

    @PostMapping
    public ResponseEntity<ProjetDTO> save(@RequestBody SaveProjetDTO saveProjetDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.projetService.save(saveProjetDTO));
    }

    @GetMapping
    public ResponseEntity<CustomPageResponse<ProjetDTO>> getall(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pagesize", defaultValue = "10") int pagesize, @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(this.projetService.list(new PaginatedRequest(page,pagesize,search)));
    }

    @PutMapping("{id}")
    public ResponseEntity<ProjetDTO> update(@PathVariable UUID id, @RequestBody UpdateProjet updateProjet) {
        return ResponseEntity.status(HttpStatus.OK).body(this.projetService.update(id, updateProjet));
    }

    @GetMapping("{id}")
    public ResponseEntity<Boolean> changeOffre(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.projetService.changeOffre(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        this.projetService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
