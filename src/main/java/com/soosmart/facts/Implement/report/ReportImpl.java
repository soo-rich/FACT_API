package com.soosmart.facts.Implement.report;

import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.service.dossier.BordereauService;
import com.soosmart.facts.service.dossier.FactureService;
import com.soosmart.facts.service.dossier.ProformaService;
import com.soosmart.facts.service.report.ReportService;
import com.soosmart.facts.utils.NumberToWords;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReportImpl implements ReportService {

    private final ProformaService proformaService;
    private final BordereauService bordereauService;
    private final FactureService factureService;
    private final NumberToWords numberToWords;
    private final ResponseMapper responseMapper;

    @Override
    public byte[] GenerateReport(String numero) {
//        recuperer les 2 premiers caracteres du numero
        String type = numero.substring(0, 2);
        return switch (type) {
            case "FP" -> this.preparedataandGenerateForProforma(this.proformaService.getProformaEntity(numero));
            case "BL" -> this.preparedataandGenerateForBordeau(this.bordereauService.getBordereauEntity(numero));
            case "FA" -> this.preparedataandGenerateForFacture(this.factureService.getFactureEntity(numero));
            default -> throw new IllegalArgumentException("Type de Document non reconnu");
        };


    }

    @Override
    public byte[] preparedataandGenerateForProforma(Proforma proforma) {
        return new byte[0];
    }

    @Override
    public byte[] preparedataandGenerateForBordeau(Bordereau bordereau) {
        return new byte[0];
    }

    @Override
    public byte[] preparedataandGenerateForFacture(Facture facture) {
        return new byte[0];
    }
}
