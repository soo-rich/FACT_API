package com.soosmart.facts.dto.stat;


public record Statistique(
        Facture facture,
        Facture bordeau,
        Facture proforma
) {
}
