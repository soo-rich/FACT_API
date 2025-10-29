package com.soosmart.facts.service;

import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.dto.project.ProjetDTO;
import com.soosmart.facts.dto.project.SaveProjetDTO;
import com.soosmart.facts.dto.project.UpdateProjet;

import java.util.List;
import java.util.UUID;

public interface ProjetService {
    CustomPageResponse<ProjetDTO> list(PaginatedRequest paginatedRequest);
    ProjetDTO save(SaveProjetDTO saveProjetDTO);
    ProjetDTO update(UUID id, UpdateProjet updateProjet);
    Boolean changeOffre(UUID id);
    void delete(UUID id);
    List<ProjetDTO> search(String search); List<ProjetDTO> getall();
}
