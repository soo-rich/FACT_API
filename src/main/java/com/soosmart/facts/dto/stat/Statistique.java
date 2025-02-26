package com.soosmart.facts.dto.stat;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;
import java.util.Map;

public record Statistique(
        Facture facture,
        Facture proforma,
        List<Table> tableList,
//        Map<String, Integer> chart
        List<Chart> chart
) {
}
