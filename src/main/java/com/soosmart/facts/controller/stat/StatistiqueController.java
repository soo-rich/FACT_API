package com.soosmart.facts.controller.stat;

import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.dto.stat.Chart;
import com.soosmart.facts.dto.stat.FacturePaid;
import com.soosmart.facts.dto.stat.Statistique;
import com.soosmart.facts.service.stat.StatService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "stat")
public class StatistiqueController {

    private final StatService statService;

    public StatistiqueController(StatService statService) {
        this.statService = statService;
    }


    @GetMapping()
    public ResponseEntity<Statistique> getStatistique() {
        return ResponseEntity.ok(
                this.statService.getStatistique()
        );
    }

    @GetMapping("/total-facture")
    public ResponseEntity<FacturePaid> getTotalFacture() {
        return ResponseEntity.ok(
                this.statService.getTotalFacture()
        );
    }

    @GetMapping("chart-data")
    public ResponseEntity<List<Chart>> countDocument() {
        return ResponseEntity.ok(
                this.statService.getChartData()
        );
    }

    @GetMapping("list-document")
    public ResponseEntity<CustomPageResponse<?>> ListDocument(
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        return ResponseEntity.ok(
                this.statService.ListDocument(
                        new PaginatedRequest(page, size, null)
                )
        );
    }
}
