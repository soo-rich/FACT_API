package com.soosmart.facts.Implement.stat;

import com.soosmart.facts.dto.stat.Facture;
import com.soosmart.facts.dto.stat.Statistique;
import com.soosmart.facts.entity.dossier.Document;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.dossier.DocumentDAO;
import com.soosmart.facts.repository.dossier.FactureDao;
import com.soosmart.facts.repository.dossier.ProformaDao;
import com.soosmart.facts.service.stat.StatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class StatImpl implements StatService {

    private final FactureDao factureDao;
    private final ProformaDao proformaDao;
    private final DocumentDAO documentDAO;
    private final ResponseMapper responseMapper;

    @Override
    public Statistique getStatistique() {
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(); // Début de la journée
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(); // Fin de la journée

        List<Document> documents = this.documentDAO.findAll();

        return new Statistique(
                new Facture(this.factureDao.countAllByDeletedIsFalse(), Math.toIntExact(this.factureDao.countFacturesCreateToday(startOfDay, endOfDay))),
                new Facture(this.proformaDao.countAllByDeletedIsFalse(), Math.toIntExact(this.proformaDao.countProformasCreateToday(startOfDay, endOfDay))),
                documents.subList(0, Math.min(documents.size(), 20)).stream().map(responseMapper::responseTable).toList()
                );
    }
}
