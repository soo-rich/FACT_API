package com.soosmart.facts.controller.dossier;

import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
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
    public ResponseEntity<CustomPageResponse<FactureDto>> getFactureAll(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pagesize", defaultValue = "10") int pagesize, @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(this.factureService.getFactureAll(new PaginatedRequest(page, pagesize, search)));
    }

    @GetMapping("/numero")
    public ResponseEntity<CustomPageResponse<String>> getFacturesNumereList(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pagesize", defaultValue = "10") int pagesize, @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(this.factureService.getFacturesNumereList(new PaginatedRequest(page,pagesize, search)));
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
