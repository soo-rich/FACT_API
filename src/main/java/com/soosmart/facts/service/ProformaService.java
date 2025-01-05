package com.soosmart.facts.service;

import com.soosmart.facts.dto.proforma.ProformaDTO;
import com.soosmart.facts.dto.proforma.SaveProformaDTO;

import java.sql.SQLException;
import java.util.List;

public interface ProformaService {
    ProformaDTO saveProforma(SaveProformaDTO saveProformaDTO);
    ProformaDTO updateProforma(SaveProformaDTO saveProformaDTO);
    void deleteProforma(String numero);
    ProformaDTO getProforma(String numero);
    List<ProformaDTO> getProformas();
    List<String> getProformasNumereList();
}
