package com.soosmart.facts.controller.dossier;

import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.service.dossier.FactureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/facture")

public class FactureController {

    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }


    @GetMapping("{numero}")
    public ResponseEntity<FactureDto> getFacture(@PathVariable String numero) {
        return ResponseEntity.status(HttpStatus.OK).body(this.factureService.getFacture(numero));
    }

    @GetMapping
    public ResponseEntity<List<FactureDto>> getFactureAll() {
        return ResponseEntity.status(HttpStatus.OK).body(this.factureService.getFactureAll());
    }

    @GetMapping("/numero")
    public ResponseEntity<List<String>> getFacturesNumereList() {
        return ResponseEntity.status(HttpStatus.OK).body(this.factureService.getFacturesNumereList());
    }

    @PostMapping("{id_borderau}")
    public ResponseEntity<FactureDto> saveFacture(@PathVariable UUID id_borderau) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.factureService.saveFacture(id_borderau));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable UUID id) {
        this.factureService.deleteFacture(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
