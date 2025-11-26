package com.soosmart.facts.Implement.dossier;

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
    public Boolean signeDocument(String numero, String signedBy, String signedByRole) {
        String type = numero.substring(0, 2);
        return switch (type) {
            case "FP" -> this.proformaService.signerProformaWithNumner(numero, signedBy, signedByRole) != null;
            case "FA" -> this.factureService.signerFactureWithNumner(numero, signedBy, signedByRole) != null;
            default -> throw new IllegalArgumentException("Invalid document type: " + numero);
        };
    }
}
