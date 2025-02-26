package com.soosmart.facts.dto.stat;

import java.util.List;

public record Statistique(
        Facture facture,
        Facture proforma,
        List<Table> tableList
) {
}
