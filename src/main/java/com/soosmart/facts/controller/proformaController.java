package com.soosmart.facts.controller;

import com.soosmart.facts.dto.proforma.ProformaDTO;
import com.soosmart.facts.dto.proforma.SaveProformaDTO;
import com.soosmart.facts.service.ProformaService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/proforma")
public class proformaController {

    private final ProformaService proformaService;

    @GetMapping
    public ResponseEntity<List<ProformaDTO>> getProformas(){
        return ResponseEntity.status(HttpStatus.OK).body(this.proformaService.getProformas());
    }


    @PostMapping()
    public ResponseEntity<ProformaDTO> save(@RequestBody SaveProformaDTO saveProformaDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.proformaService.saveProforma(saveProformaDTO));
    }

    @GetMapping("{numero}")
    public ResponseEntity<ProformaDTO> getProforma(@PathVariable String numero){
        return ResponseEntity.status(HttpStatus.OK).body(this.proformaService.getProforma(numero));
    }

}
