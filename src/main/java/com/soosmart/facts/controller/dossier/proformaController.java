package com.soosmart.facts.controller.dossier;

import com.soosmart.facts.dto.dossier.proforma.ProformaDTO;
import com.soosmart.facts.dto.dossier.proforma.SaveProformaDTO;
import com.soosmart.facts.dto.dossier.proforma.SaveProformaWithArticleDTO;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.service.dossier.ProformaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/proforma")
public class proformaController {

    private final ProformaService proformaService;

    @GetMapping
    public ResponseEntity<CustomPageResponse<ProformaDTO>> getProformas(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "pagesize", defaultValue = "10") int pagesize, @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(this.proformaService.getProformas(new PaginatedRequest(page,pagesize,search)));
    }

    @GetMapping("/numero")
    public ResponseEntity<CustomPageResponse<String>> getProformasNumereList(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "pagesize", defaultValue = "10") int pagesize, @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(this.proformaService.getProformasNumereList(new PaginatedRequest(page , pagesize, search)));
    }
    @GetMapping("/not-adoped")
    public ResponseEntity<CustomPageResponse<ProformaDTO>> getProformasNotAdapted(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "pagesize", defaultValue = "10") int pagesize, @RequestParam(value = "search", defaultValue = "", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(this.proformaService.getProformasNotAdopted(new PaginatedRequest(page,pagesize,search)));
    }


    @PostMapping()
    public ResponseEntity<ProformaDTO> save(@RequestBody SaveProformaDTO saveProformaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.proformaService.saveProforma(saveProformaDTO));
    }

    @PostMapping(path = "with-article")
    public ResponseEntity<ProformaDTO> savewitharticle(@RequestBody SaveProformaWithArticleDTO saveProformaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.proformaService.saveProforma(saveProformaDTO));
    }

    @GetMapping("{numero}")
    public ResponseEntity<ProformaDTO> getProforma(@PathVariable String numero) {
        return ResponseEntity.status(HttpStatus.OK).body(this.proformaService.getProforma(numero));
    }

    @GetMapping("reference/{id}")
    public ResponseEntity<Void> updateProformaReference(@PathVariable UUID id, @RequestParam("ref") String newReference) {
        this.proformaService.updateProformaReference(id, newReference);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

     @PutMapping("{id}")
    public ResponseEntity<ProformaDTO> update(@PathVariable UUID id, @RequestBody SaveProformaWithArticleDTO saveProformaDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(this.proformaService.update(id, saveProformaDTO));
    }


    @DeleteMapping("{numero}")
    public ResponseEntity<Void> deleteProforma(@PathVariable String numero) {
        this.proformaService.deleteProforma(numero);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
