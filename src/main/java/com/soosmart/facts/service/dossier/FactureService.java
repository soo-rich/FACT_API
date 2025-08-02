package com.soosmart.facts.service.dossier;

import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.dossier.Facture;

import java.util.UUID;

public interface FactureService {
    void deleteFacture(UUID id);

    FactureDto getFacture(String numero);

    Facture getFactureEntity(String numero);

    CustomPageResponse<FactureDto> getFactureAll(PaginatedRequest paginatedRequest);

    CustomPageResponse<String> getFacturesNumereList(PaginatedRequest paginatedRequest);

    FactureDto saveFacture(UUID id_borderau);

    FactureDto signerFactureWithNumner(String numero, String who_signed);

    Boolean paid(UUID id_facture);
}
