package com.soosmart.facts.controller.report;

import com.soosmart.facts.service.report.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
        this.reportService.GenerateReport(numero);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("{numero}/download")
    public ResponseEntity<?> downloadReport(@PathVariable String numero){
        this.reportService.DownloadReport(numero);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
