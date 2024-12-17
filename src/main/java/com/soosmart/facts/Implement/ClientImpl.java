package com.soosmart.facts.Implement;

import com.soosmart.facts.dto.client.ClientDTO;
import com.soosmart.facts.dto.client.SaveClientDTO;
import com.soosmart.facts.entity.Client;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.ClientDAO;
import com.soosmart.facts.service.ClientService;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientImpl implements ClientService {

    private final ClientDAO clientDAO;
    private final ResponseMapper responseMapper;

    @Override
    public List<ClientDTO> list() {
        return this.clientDAO.findAll().stream().map(
                this.responseMapper::responseClientDTO
        ).toList();
    }

    @Override
    public ClientDTO save(SaveClientDTO saveClientDTO) {
        return this.responseMapper.responseClientDTO(this.clientDAO.save(Client.builder()
                .lieu(saveClientDTO.lieu())
                .nom(saveClientDTO.nom())
                .sigle(saveClientDTO.sigle())
                .telephone(saveClientDTO.telephone())
                .potentiel(saveClientDTO.potentiel())
                .build())
        );
    }

    @Override
    public ClientDTO update(UUID id, SaveClientDTO saveClientDTO) {
        Optional<Client> clientold = this.clientDAO.findById(id).stream().findFirst();
        if (clientold.isPresent()) {
            Client client_new = clientold.get();
            client_new.setLieu(saveClientDTO.lieu());
            client_new.setNom(saveClientDTO.nom());
            client_new.setSigle(saveClientDTO.sigle());
            client_new.setTelephone(saveClientDTO.telephone());
            client_new.setPotentiel(saveClientDTO.potentiel());
            return this.responseMapper.responseClientDTO(this.clientDAO.save(client_new));
        }
        return this.responseMapper.responseClientDTO(clientold.get());
    }

    @Override
    public Boolean delete(UUID id) {
        try {
            this.clientDAO.delete(this.clientDAO.getReferenceById(id));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean changePotential(UUID id) {
        Optional<Client> client = this.clientDAO.findById(id).stream().findFirst();
        if (client.isPresent()) {
            try {
                Client client1 = client.get();
                client1.setPotentiel(!client1.getPotentiel());
                this.clientDAO.save(client1);
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Erreur Durant le mise a jour du client");
            }
        } else {
            throw new EntityExistsException("ce Client n'existe pas");
        }

    }
}
