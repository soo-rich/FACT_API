package com.soosmart.facts.utils;

import com.soosmart.facts.repository.dossier.BorderauDao;
import com.soosmart.facts.repository.dossier.FactureDao;
import com.soosmart.facts.repository.dossier.ProformaDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class NumeroGenerateur {
    private final ProformaDao proformaRepository;
    private final FactureDao factureRepository;
    private final BorderauDao borderauRepository;

    // Générer un numéro de proforma
    public String GenerateproformaNumero() {
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(); // Début de la journée
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(); // Fin de la
                                                                                                         // journée

        Long numbreoftoday = this.proformaRepository.countProformasCreateToday(startOfDay, endOfDay);

        return "FP" + this.ContatenationDesChiffreduJour()
                + (numbreoftoday <= 9 ? "0" + numbreoftoday : numbreoftoday);
    }

    // Générer un numéro de facture
    public String GenerateFactureNumero() {
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(); // Début de la journée
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(); // Fin de la
                                                                                                         // journée

        Long numbreoftoday = this.factureRepository.countFacturesCreateToday(startOfDay, endOfDay);

        return "FA" + this.ContatenationDesChiffreduJour()
                + (numbreoftoday <= 9 ? "0" + numbreoftoday : numbreoftoday);
    }

    public String GenerateBordereauNumero() {
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(); // Début de la journée
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(); // Fin de la
                                                                                                         // journée

        Long numbreoftoday = this.borderauRepository.countBordereauxCreateToday(startOfDay, endOfDay);
        return "BL" + this.ContatenationDesChiffreduJour()
                + (numbreoftoday <= 9 ? "0" + numbreoftoday : numbreoftoday);
    }

    private String ContatenationDesChiffreduJour() {
        String year = String.valueOf(LocalDate.now().getYear()).substring(2); // Année (2 chiffres)
        String month = String.format("%02d", LocalDate.now().getMonthValue()); // Mois
        String day = String.format("%02d", LocalDate.now().getDayOfMonth()); // Jour
        return year + month + day;
    }
}
