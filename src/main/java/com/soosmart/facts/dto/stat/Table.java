package com.soosmart.facts.dto.stat;

import java.time.Instant;

public record Table(
        String numero,
        Instant date,
        Float total
) {
}
