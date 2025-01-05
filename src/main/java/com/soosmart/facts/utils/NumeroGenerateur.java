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
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(); // Fin de la journée
        return "FP" + this.ContatenationDesChiffreduJour()+ this.proformaRepository.countProformasCreateToday(startOfDay, endOfDay);
    }

    // Générer un numéro de facture
    public String GenerateFactureNumero() {
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(); // Début de la journée
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(); // Fin de la journée
        return "FA" +this.ContatenationDesChiffreduJour() + this.factureRepository.countFacturesCreateToday(startOfDay, endOfDay);
    }

    public String GenerateBordereauNumero() {
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(); // Début de la journée
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(); // Fin de la journée
        return "BL" + this.ContatenationDesChiffreduJour() + this.borderauRepository.countBordereauxCreateToday(startOfDay, endOfDay);
    }

    private String ContatenationDesChiffreduJour(){
        String year = String.valueOf(LocalDate.now().getYear()).substring(2); // Année (2 chiffres)
        String month = String.format("%02d", LocalDate.now().getMonthValue()); // Mois
        String day = String.format("%02d", LocalDate.now().getDayOfMonth());  // Jour
        return year + month + day;
    }
}
