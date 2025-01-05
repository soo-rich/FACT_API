package com.soosmart.facts.Implement;

import com.soosmart.facts.dto.articleQuantite.SaveArticleQuantiteDTO;
import com.soosmart.facts.dto.proforma.ProformaDTO;
import com.soosmart.facts.dto.proforma.SaveProformaDTO;
import com.soosmart.facts.entity.Client;
import com.soosmart.facts.entity.Projet;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.ClientDAO;
import com.soosmart.facts.repository.ProjetDAO;
import com.soosmart.facts.repository.dossier.ProformaDao;
import com.soosmart.facts.service.ArticleQuantiteService;
import com.soosmart.facts.service.ProformaService;
import com.soosmart.facts.utils.NumeroGenerateur;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProformaImpl implements ProformaService {

    private final ProformaDao proformaRepository;
    private final ProjetDAO projetRepository;
    private final ClientDAO clientRepository;
    private final ArticleQuantiteService articleQuantiteService;
    private final ResponseMapper responseMapper;
    private final NumeroGenerateur numeroGenerateur;


    @Override
    public ProformaDTO saveProforma(SaveProformaDTO saveProformaDTO) {
        Optional<Projet> projet = this.projetRepository.findById(saveProformaDTO.projet_id()).stream().findFirst();
        if (projet.isPresent()) {
            Proforma proforma = Proforma.builder()
                    .numero(this.numeroGenerateur.GenerateproformaNumero())
                    .reference(saveProformaDTO.reference())
                    .projet(projet.get())
                    .client(projet.get().getClient())
                    .articleQuantiteList(this.articleQuantiteService.saveAllArticleQuantitelist(saveProformaDTO.articleQuantiteslist()))
                    .build();
            return this.responseMapper.responseProformaDTO(this.proformaRepository.save(this.CalculateProformaTotal(proforma)));
        } else {
            Optional<Client> client = this.clientRepository.findById(saveProformaDTO.client_id()).stream().findFirst();
            if (client.isEmpty()) {
                throw new EntityExistsException("Client not found");
            }
            Proforma proforma = Proforma.builder()
                    .numero(this.numeroGenerateur.GenerateproformaNumero())
                    .client(client.get())
                    .reference(saveProformaDTO.reference())
                    .articleQuantiteList(this.articleQuantiteService.saveAllArticleQuantitelist(saveProformaDTO.articleQuantiteslist()))
                    .build();
            return this.responseMapper.responseProformaDTO(this.proformaRepository.save(this.CalculateProformaTotal(proforma)));
        }
    }

    @Override
    public String updateProformaReference( String reference) {
        this.proformaRepository.findAllByReference(reference).forEach(proforma -> {
            proforma.setReference(reference);
            this.proformaRepository.save(proforma);
        });
        return "Reference updated";
    }

    @Override
    public ProformaDTO updateProformaArticleQuantite(UUID id, List<SaveArticleQuantiteDTO> articleQuantiteslist) {
        Optional<Proforma> proforma = this.proformaRepository.findById(id).stream().findFirst();
        if (proforma.isPresent()) {
            Proforma proformanew = Proforma.builder()
                    .numero(this.numeroGenerateur.GenerateproformaNumero())
                    .reference(proforma.get().getReference())
                    .projet(proforma.get().getProjet())
                    .client(proforma.get().getClient())
                    .articleQuantiteList(this.articleQuantiteService.saveAllArticleQuantitelist(articleQuantiteslist))
                    .build();
            return this.responseMapper.responseProformaDTO(this.proformaRepository.save(this.CalculateProformaTotal(proformanew)));
        } else {
            throw new EntityExistsException("Proforma not found");

        }
    }

    @Override
    public void deleteProforma(String numero) {
        Optional<Proforma> proforma = this.proformaRepository.findByNumero(numero);
        if (proforma.isPresent()) {
            try {
                this.proformaRepository.delete(proforma.get());
            } catch (Exception exception) {
                System.out.println("Cause -> \n" + exception.getCause() + "\nmessage ->\n" + exception.getMessage());
                ;
            }
        }
    }

    @Override
    public ProformaDTO getProforma(String numero) {
        Optional<Proforma> proforma = this.proformaRepository.findByNumero(numero);
        if (proforma.isPresent()) {
            return this.responseMapper.responseProformaDTO(proforma.get());
        } else {
            throw new EntityExistsException("Donne non trouve avec ce numero de proforma");
        }
    }

    @Override
    public List<ProformaDTO> getProformas() {
        return this.proformaRepository.findAll().stream().map(this.responseMapper::responseProformaDTO).toList();
    }

    @Override
    public List<String> getProformasNumereList() {
        return this.proformaRepository.findAll().stream().map(Proforma::getNumero).toList();
    }



    private Proforma CalculateProformaTotal(Proforma proforma) {
        var objectStream = proforma.getArticleQuantiteList()
                .stream()
                .map(
                        articleQuantite ->
                                articleQuantite.getPrix_article() * articleQuantite.getQuantite()
                );
        proforma.setTotal_ht(
                objectStream.reduce(0f, Float::sum)
        );
        proforma.setTotal_tva(proforma.getTotal_ht() * 0.18f);
        proforma.setTotal_ttc(proforma.getTotal_ht() + proforma.getTotal_tva());

        return proforma;
    }
}
