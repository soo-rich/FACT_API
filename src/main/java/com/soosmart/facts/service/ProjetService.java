package com.soosmart.facts.service;

import com.soosmart.facts.dto.project.ProjetDTO;
import com.soosmart.facts.dto.project.SaveProjetDTO;
import com.soosmart.facts.dto.project.UpdateProjet;

import java.util.List;
import java.util.UUID;

public interface ProjetService {
    List<ProjetDTO> list();
    ProjetDTO save(SaveProjetDTO saveProjetDTO);
    ProjetDTO update(UUID id, UpdateProjet updateProjet);
    Boolean changeOffre(UUID id);
    void delete(UUID id);
}
