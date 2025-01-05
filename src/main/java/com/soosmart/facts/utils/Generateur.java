package com.soosmart.facts.utils;

import com.soosmart.facts.repository.dossier.ProformaDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class Generateur {
    private final ProformaDao proformaRepository;
    // Générer un numéro de proforma
    public String GenerateproformaNumero() {
        // Obtenir l'année (2 derniers chiffres), le mois et le jour
        String year = String.valueOf(LocalDate.now().getYear()).substring(2); // Année (2 chiffres)
        String month = String.format("%02d", LocalDate.now().getMonthValue()); // Mois
        String day = String.format("%02d", LocalDate.now().getDayOfMonth());  // Jour
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(); // Début de la journée
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(); // Fin de la journée
        return "FP" + year + month + day + this.proformaRepository.countProformasCreateToday(startOfDay, endOfDay);
    }
}
