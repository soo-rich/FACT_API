package com.soosmart.facts.Implement.stat;

import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.dto.stat.*;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.dossier.BorderauDao;
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
    private final BorderauDao borderauDao;
    private final ProformaDao proformaDao;
    private final DocumentDAO documentDAO;
    private final ResponseMapper responseMapper;

    @Override
    public Statistique getStatistique() {
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(); // Début de la journée
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(); // Fin de la journée

        return new Statistique(
                new Facture(this.factureDao.countAllByDeletedIsFalse(),
                        Math.toIntExact(this.factureDao.countFacturesCreateToday(startOfDay, endOfDay)),
                        null,
                        null),
                new Facture(this.borderauDao.countAllByDeletedIsFalse(),
                        Math.toIntExact(this.borderauDao.countBordereauxCreateToday(startOfDay, endOfDay)),
                        this.borderauDao.countBordereauxAdopedTrue(),
                        this.borderauDao.countBordereauxAdopedFalse()),
                new Facture(this.proformaDao.countAllByDeletedIsFalse(),
                        Math.toIntExact(this.proformaDao.countProformasCreateToday(startOfDay, endOfDay)),
                        this.proformaDao.countAllAdoptedIsTrue(),
                        this.proformaDao.countAllAdoptedIsFalse())
        );
    }

    @Override
    public FacturePaid getTotalFacture() {
        return new FacturePaid(this.factureDao.getTotalFacture(), this.factureDao.getTotalFactureUnPaiud());
    }

    @Override
    public List<Chart> getChartData() {
        return this.documentDAO.countDocumentTypes().stream()
                .map(item -> new Chart(
                        item[0].toString(),

                        (long) (item[1] instanceof Long ? ((Long) item[1]).intValue() : 0)
                )).toList();
    }

    @Override
    public CustomPageResponse<Table> ListDocument(PaginatedRequest paginatedRequest) {
        List<Object[]> allByDeletedIsFalse = this.documentDAO.findAllByDeletedIsFalse(
                paginatedRequest.page() * paginatedRequest.pagesize(),
                paginatedRequest.pagesize()
        );

        CustomPageResponse<Table> customPageResponse = new CustomPageResponse<>();
        customPageResponse.setContent(
                allByDeletedIsFalse.stream()
                        .map(item -> new Table(
                                item[0].toString(),
                                ((Instant) item[1]),
                                (Float) item[2]
                        ))
                        .toList()
        );
        customPageResponse.setTotalElements(this.documentDAO.countByDeletedIsFalse());
        customPageResponse.setTotalPages(
                (int) Math.ceil((double) customPageResponse.getTotalElements() / paginatedRequest.pagesize())
        );

        return customPageResponse;

    }
}
