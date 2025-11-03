package com.soosmart.facts.service.dossier;

import com.soosmart.facts.dto.dossier.borderau.BorderauDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.dossier.Bordereau;
// import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface BordereauService {
    BorderauDto saveBordereau(UUID id_proforma);

    // BorderauDto saveBordereau(UUID id_proforma, MultipartFile file);

    CustomPageResponse<BorderauDto> getBordereauAll(PaginatedRequest paginatedRequest);

    CustomPageResponse<BorderauDto> getBordereauAllNotAdopted(PaginatedRequest paginatedRequest);

    BorderauDto getBordereau(UUID id);

    void deleteBordereau(UUID id);

    BorderauDto getBordereauByNumero(String numero);

    Bordereau getBordereauEntity(String numero);
}
