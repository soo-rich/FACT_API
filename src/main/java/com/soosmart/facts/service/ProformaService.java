package com.soosmart.facts.service;

import com.soosmart.facts.dto.articleQuantite.SaveArticleQuantiteDTO;
import com.soosmart.facts.dto.proforma.ProformaDTO;
import com.soosmart.facts.dto.proforma.SaveProformaDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface ProformaService {
    ProformaDTO saveProforma(SaveProformaDTO saveProformaDTO);
    String updateProformaReference(String reference);
    ProformaDTO updateProformaArticleQuantite(UUID id,  List<SaveArticleQuantiteDTO> articleQuantiteslist);
    void deleteProforma(String numero);
    ProformaDTO getProforma(String numero);
    List<ProformaDTO> getProformas();
    List<String> getProformasNumereList();
}
