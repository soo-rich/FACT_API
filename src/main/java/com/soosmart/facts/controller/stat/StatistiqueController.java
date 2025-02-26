package com.soosmart.facts.controller.stat;

import com.soosmart.facts.dto.stat.Statistique;
import com.soosmart.facts.service.stat.StatService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "stat")
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
}
