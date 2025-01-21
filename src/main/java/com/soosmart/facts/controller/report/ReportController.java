package com.soosmart.facts.controller.report;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("report")
public class ReportController {

    @GetMapping("{nuero}")
    public ResponseEntity<?> generatereport(@PathVariable String nuero){
        return null;
    }
}
