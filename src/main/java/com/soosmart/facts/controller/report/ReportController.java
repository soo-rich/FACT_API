package com.soosmart.facts.controller.report;

import com.soosmart.facts.dto.dossier.DocumentDTO;
import com.soosmart.facts.service.dossier.DocumentService;
import com.soosmart.facts.service.report.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("document")
public class ReportController {
    private final ReportService reportService;
    private final DocumentService documentService;

    @GetMapping("{numero}")
    public ResponseEntity<byte[]> generatereport(@PathVariable String numero) {
        byte[] bytes = this.reportService.GenerateReport(numero);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + numero + ".pdf");
        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(bytes);
    }

    @GetMapping("data/{numero}")
    public ResponseEntity<DocumentDTO> getDocumentByNumero(@PathVariable String numero) {
        return ResponseEntity.status(HttpStatus.OK).body(this.reportService.getDocumentByNumero(numero));
    }


    @PatchMapping("signe/{numero}")
    public ResponseEntity<Boolean> signeDocument(@PathVariable String numero, @RequestParam(required = true) String signedBy) {
        return ResponseEntity.status(HttpStatus.OK).body(this.documentService.signeDocument(numero, signedBy));
    }
}
