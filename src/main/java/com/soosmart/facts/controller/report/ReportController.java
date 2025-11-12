package com.soosmart.facts.controller.report;

import com.soosmart.facts.Implement.report.PdfDocumentService;
import com.soosmart.facts.dto.dossier.DocumentDTO;
import com.soosmart.facts.service.dossier.DocumentService;
import com.soosmart.facts.service.report.ReportService;
import com.soosmart.facts.utils.report.PdfGeneration;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("document")
public class ReportController {
    private final ReportService reportService;
    private final DocumentService documentService;
    private final PdfDocumentService pdfDocumentService;


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

    /**
     * Endpoint générique pour générer n'importe quel type de document
     */
    @GetMapping("/generate/{numero}")
    public ResponseEntity<byte[]> genererDocument(
            @PathVariable String numero
    ) {
        try {
            byte[] pdfBytes;
            String filename;


            pdfBytes = pdfDocumentService.generateReport(numero);
            switch (numero.substring(0, 2)) {
                case "FP":
                    filename = "Facture_" + numero + ".pdf";
                    break;
                case "BL":
                    filename = "Bordereau_" + numero + ".pdf";
                    break;
                case "FA":
                    filename = "Proforma_" + numero + ".pdf";
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }

            return createPdfResponse(pdfBytes, filename);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Crée la réponse HTTP pour un PDF
     */
    private ResponseEntity<byte[]> createPdfResponse(byte[] pdfBytes, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
