package com.soosmart.facts.service.report;

import com.soosmart.facts.dto.dossier.DocumentDTO;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;

public interface ReportService {
    byte[] GenerateReport(String numero);

    DocumentDTO getDocumentByNumero(String numero);

}
