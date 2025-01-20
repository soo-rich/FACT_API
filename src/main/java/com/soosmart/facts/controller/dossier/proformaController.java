package com.soosmart.facts.controller.dossier;

import com.soosmart.facts.dto.articleQuantite.SaveArticleQuantiteDTO;
import com.soosmart.facts.dto.dossier.proforma.ProformaDTO;
import com.soosmart.facts.dto.dossier.proforma.SaveProformaDTO;
import com.soosmart.facts.service.dossier.ProformaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/proforma")
public class proformaController {

    private final ProformaService proformaService;

    @GetMapping
    public ResponseEntity<List<ProformaDTO>> getProformas() {
        return ResponseEntity.status(HttpStatus.OK).body(this.proformaService.getProformas());
    }

    @GetMapping("/numero")
    public ResponseEntity<List<String>> getProformasNumereList() {
        return ResponseEntity.status(HttpStatus.OK).body(this.proformaService.getProformasNumereList());
    }
    @GetMapping("/not-adoped")
    public ResponseEntity<List<ProformaDTO>> getProformasNotAdapted() {
        return ResponseEntity.status(HttpStatus.OK).body(this.proformaService.getProformasNotAdopted());
    }


    @PostMapping()
    public ResponseEntity<ProformaDTO> save(@RequestBody SaveProformaDTO saveProformaDTO) {
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
    public ResponseEntity<ProformaDTO> updateProformaArticleQuantite(@PathVariable UUID id, @RequestBody List<SaveArticleQuantiteDTO> articleQuantiteslist) {
        return ResponseEntity.status(HttpStatus.OK).body(this.proformaService.updateProformaArticleQuantite(id, articleQuantiteslist));
    }


    @DeleteMapping("{numero}")
    public ResponseEntity<Void> deleteProforma(@PathVariable String numero) {
        this.proformaService.deleteProforma(numero);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
