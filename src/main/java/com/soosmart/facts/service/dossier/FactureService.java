package com.soosmart.facts.service.dossier;

import com.soosmart.facts.dto.dossier.facture.FactureDto;

import java.util.List;
import java.util.UUID;

public interface FactureService {
    void deleteFacture(UUID id);
    FactureDto getFacture(String numero);
    List<FactureDto> getFactureAll();
    List<String> getFacturesNumereList();
    FactureDto saveFacture(UUID id_borderau);
}
