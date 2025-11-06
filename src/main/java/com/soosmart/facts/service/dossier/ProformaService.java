package com.soosmart.facts.service.dossier;

import com.soosmart.facts.dto.articleQuantite.SaveArticleQuantiteDTO;
import com.soosmart.facts.dto.dossier.proforma.ProformaDTO;
import com.soosmart.facts.dto.dossier.proforma.SaveProformaDTO;
import com.soosmart.facts.dto.dossier.proforma.SaveProformaWithArticleDTO;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.dossier.Proforma;

import java.util.List;
import java.util.UUID;

public interface ProformaService {
    ProformaDTO saveProforma(SaveProformaDTO saveProformaDTO);
    ProformaDTO saveProforma(SaveProformaWithArticleDTO saveProformaDTO);
    ProformaDTO update(UUID id, SaveProformaWithArticleDTO saveProformaDTO);
    String updateProformaReference(UUID id, String newReference);
    ProformaDTO updateProformaArticleQuantite(UUID id,  List<SaveArticleQuantiteDTO> articleQuantiteslist);
    void deleteProforma(String numero);
    ProformaDTO getProforma(String numero);
    CustomPageResponse<ProformaDTO> getProformas(PaginatedRequest paginatedRequest);
    CustomPageResponse<ProformaDTO> getProformasNotAdopted(PaginatedRequest paginatedRequest);
    CustomPageResponse<String> getProformasNumereList(PaginatedRequest paginatedRequest);
    ProformaDTO signerProforma(UUID id, String who_signed);

    ProformaDTO signerProformaWithNumner(String number, String who_signed);
    ProformaDTO signedbywhoconnectProforma(UUID id);
    Proforma getProformaEntity(String numero);
}
