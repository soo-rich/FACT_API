package com.soosmart.facts.service.report;

import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;

public interface ReportService {
    byte[] GenerateReport(String numero);

    byte[] preparedataandGenerateForProforma(Proforma proforma);

    byte[] preparedataandGenerateForBordeau(Bordereau bordereau);

    byte[] preparedataandGenerateForFacture(Facture facture);
}
