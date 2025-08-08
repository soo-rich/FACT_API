package com.soosmart.facts.dto.stat;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor()
@NoArgsConstructor()
@Builder()
@Getter
@Setter
public class TableClasse {
    private String numero;
    private Instant date;
    private BigDecimal totalTtc;
}
