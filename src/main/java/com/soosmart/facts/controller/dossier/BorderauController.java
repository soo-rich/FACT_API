package com.soosmart.facts.controller.dossier;

import com.soosmart.facts.dto.dossier.borderau.BorderauDto;
import com.soosmart.facts.service.dossier.BordereauService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/borderau")

public class BorderauController {
    private final BordereauService bordereauService;

    public BorderauController(BordereauService bordereauService) {
        this.bordereauService = bordereauService;
    }


    @GetMapping
    public ResponseEntity<List<BorderauDto>> getBorderauAll() {
        return ResponseEntity.status(HttpStatus.OK).body(this.bordereauService.getBordereauAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<BorderauDto> getBorderau(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.bordereauService.getBordereau(id));
    }

    @PostMapping
    public ResponseEntity<BorderauDto> saveBorderau(@RequestParam("id") UUID id_proforma) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.bordereauService.saveBordereau(id_proforma));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBorderau(@PathVariable UUID id) {
        this.bordereauService.deleteBordereau(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
