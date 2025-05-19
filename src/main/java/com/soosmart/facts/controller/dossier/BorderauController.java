package com.soosmart.facts.controller.dossier;

import com.soosmart.facts.dto.dossier.borderau.BorderauDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.service.dossier.BordereauService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/borderau")

public class BorderauController {
    private final BordereauService bordereauService;

    public BorderauController(BordereauService bordereauService) {
        this.bordereauService = bordereauService;
    }


    @GetMapping
    public ResponseEntity<CustomPageResponse<BorderauDto>> getBorderauAll(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pagesize", defaultValue = "10") int pagesize, @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(this.bordereauService.getBordereauAll(new PaginatedRequest(page, pagesize, search)));
    }

    @GetMapping("not-use")
    public ResponseEntity<CustomPageResponse<BorderauDto>> getBorderauAllNotAdopt(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pagesize", defaultValue = "10") int pagesize, @RequestParam(value = "search", defaultValue = "", required = false) String search) {

        return ResponseEntity.status(HttpStatus.OK).body(this.bordereauService.getBordereauAllNotAdopted(new PaginatedRequest(page, pagesize, search)));
    }

    @GetMapping("{id}")
    public ResponseEntity<BorderauDto> getBorderau(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.bordereauService.getBordereau(id));
    }

    @PostMapping("{id_proforma}")
    public ResponseEntity<BorderauDto> saveBorderau(@PathVariable UUID id_proforma) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.bordereauService.saveBordereau(id_proforma));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBorderau(@PathVariable UUID id) {
        this.bordereauService.deleteBordereau(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
