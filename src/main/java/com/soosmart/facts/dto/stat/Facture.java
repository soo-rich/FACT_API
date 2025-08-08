package com.soosmart.facts.dto.stat;

public record Facture(
        Integer total,
        Integer total_today,
        Long adopted_true,
        Long adopted_false
) {
}
