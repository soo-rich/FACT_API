package com.soosmart.facts.service;

import com.soosmart.facts.dto.client.ClientDTO;
import com.soosmart.facts.dto.client.SaveClientDTO;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<ClientDTO> list ();
    ClientDTO save(SaveClientDTO saveClientDTO);
    ClientDTO update(UUID id, SaveClientDTO saveClientDTO);
    Boolean delete(UUID id);
    Boolean changePotential(UUID id);
}
