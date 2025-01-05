package com.soosmart.facts.service.dossier;

import com.soosmart.facts.dto.articleQuantite.SaveArticleQuantiteDTO;
import com.soosmart.facts.dto.dossier.proforma.ProformaDTO;
import com.soosmart.facts.dto.dossier.proforma.SaveProformaDTO;

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
    ProformaDTO signerProforma(UUID id, String who_signed);
    ProformaDTO signedbywhoconnectProforma(UUID id);
}
