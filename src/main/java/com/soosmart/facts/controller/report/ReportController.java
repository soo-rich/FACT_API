package com.soosmart.facts.controller.report;

import com.soosmart.facts.service.report.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("report")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("{numero}")
    public ResponseEntity<?> generatereport(@PathVariable String numero){
        byte[] bytes = this.reportService.GenerateReport(numero);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=proforma.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(bytes.length)
                .body(bytes);
    }

    @GetMapping("test/{numero}")
    public ResponseEntity<byte[]> test(@PathVariable String numero){
        return ResponseEntity.status(HttpStatus.OK).
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=text.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(this.reportService.GenerateReport(numero));
    }

}
