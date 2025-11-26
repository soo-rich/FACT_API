package com.soosmart.facts.service.report;

import com.soosmart.facts.dto.dossier.DocumentDTO;


public interface ReportService {
    byte[] GenerateReport(String numero);

    DocumentDTO getDocumentByNumero(String numero);

}
