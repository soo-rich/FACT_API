package com.soosmart.facts.Implement.report;

import com.soosmart.facts.dto.dossier.borderau.BorderauDto;
import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.dto.dossier.proforma.ProformaDTO;
import com.soosmart.facts.service.dossier.BordereauService;
import com.soosmart.facts.service.dossier.FactureService;
import com.soosmart.facts.service.dossier.ProformaService;
import com.soosmart.facts.service.report.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReportImpl implements ReportService {

    private final ProformaService proformaService;
    private final BordereauService bordereauService;
    private final FactureService factureService;

    @Override
    public void GenerateReport(String numero) {

//        recupere les deux premier caratere du nemero
        String type = numero.substring(0, 2);
        switch (type) {
            case "FP":
                ProformaDTO proforma = this.proformaService.getProforma(numero);
                System.out.println(proforma);
                break;
            case "BL":
                BorderauDto borderau = this.bordereauService.getBordereauByNumero(numero);
                System.out.println(borderau);
                break;
            case "FA":
                FactureDto facture = this.factureService.getFacture(numero);
                System.out.println(facture);
                break;
            default:
                throw new IllegalArgumentException("Type de Document non reconnu");
        }


    }

    @Override
    public void DownloadReport(String numero) {

    }
}
