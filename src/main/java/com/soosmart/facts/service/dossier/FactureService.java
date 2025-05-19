package com.soosmart.facts.service.dossier;

import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.dossier.Facture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface FactureService {
    void deleteFacture(UUID id);
    FactureDto getFacture(String numero);
    Facture getFactureEntity(String numero);
    CustomPageResponse<FactureDto> getFactureAll(PaginatedRequest paginatedRequest);
    CustomPageResponse<String> getFacturesNumereList(PaginatedRequest paginatedRequest);
    FactureDto saveFacture(UUID id_borderau);
}
