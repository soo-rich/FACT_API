package com.soosmart.facts.service;

import com.soosmart.facts.dto.client.ClientDTO;
import com.soosmart.facts.dto.client.SaveClientDTO;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    CustomPageResponse<ClientDTO> list (PaginatedRequest paginatedRequest);
    ClientDTO save(SaveClientDTO saveClientDTO);
    ClientDTO update(UUID id, SaveClientDTO saveClientDTO);
    Boolean delete(UUID id);
    Boolean changePotential(UUID id);
    List<ClientDTO> search(String search);
}
