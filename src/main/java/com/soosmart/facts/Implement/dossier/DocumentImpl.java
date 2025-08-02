package com.soosmart.facts.Implement.dossier;

import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.dto.dossier.proforma.ProformaDTO;
import com.soosmart.facts.service.dossier.DocumentService;
import com.soosmart.facts.service.dossier.FactureService;
import com.soosmart.facts.service.dossier.ProformaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DocumentImpl implements DocumentService {
    private final FactureService factureService;
    private final ProformaService proformaService;


    @Override
    public Boolean signeDocument(String numero, String signedBy) {
        String type = numero.substring(0, 2);
        switch (type) {
            case "PF":
                ProformaDTO proforma = this.proformaService.signerProformaWithNumner(numero, signedBy);
            case "FA":
                FactureDto facture = this.factureService.signerFactureWithNumner(numero, signedBy);
            default:
                throw new IllegalArgumentException("Invalid document type: " + numero);
        }
    }
}
