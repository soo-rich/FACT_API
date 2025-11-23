package com.soosmart.facts.Implement.dossier;

import com.soosmart.facts.dto.articleQuantite.SaveArticleQuantiteDTO;
import com.soosmart.facts.dto.dossier.proforma.ProformaDTO;
import com.soosmart.facts.dto.dossier.proforma.SaveProformaDTO;
import com.soosmart.facts.dto.dossier.proforma.SaveProformaWithArticleDTO;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.Article;
import com.soosmart.facts.entity.Client;
import com.soosmart.facts.entity.Projet;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.ArticleDAO;
import com.soosmart.facts.repository.ClientDAO;
import com.soosmart.facts.repository.ProjetDAO;
import com.soosmart.facts.repository.dossier.ProformaDao;
import com.soosmart.facts.security.user.UtilisateurConnecteServie;
import com.soosmart.facts.service.ArticleQuantiteService;
import com.soosmart.facts.service.dossier.ProformaService;
import com.soosmart.facts.utils.NumeroGenerateur;
import com.soosmart.facts.utils.pagination.PageMapperUtils;
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
    private final ArticleDAO articleRepository;
    private final ProjetDAO projetRepository;
    private final ClientDAO clientRepository;
    private final ArticleQuantiteService articleQuantiteService;
    private final ResponseMapper responseMapper;
    private final NumeroGenerateur numeroGenerateur;
    private final UtilisateurConnecteServie utilisateurConnecteServie;

    @Override
    public ProformaDTO saveProforma(SaveProformaDTO saveProformaDTO) {
        if (saveProformaDTO.projet_id() != null) {
            Optional<Projet> projet = this.projetRepository.findById(saveProformaDTO.projet_id()).stream().findFirst();
            Proforma proforma1 = new Proforma();
            if (projet.isPresent()) {
                Proforma proforma = Proforma.builder()
                        .numero(this.numeroGenerateur.GenerateproformaNumero())
                        .reference(saveProformaDTO.reference())
                        .projet(projet.get())
                        .client(projet.get().getClient())
                        .uniqueIdDossier(UUID.randomUUID())
                        .signedBy(this.utilisateurConnecteServie.getUtilisateurConnecte().getNom() + " "
                                + this.utilisateurConnecteServie.getUtilisateurConnecte().getPrenom())
                        .articleQuantiteList(this.articleQuantiteService
                                .saveAllArticleQuantitelist(saveProformaDTO.articleQuantiteslist()))
                        .role(this.utilisateurConnecteServie.getUtilisateurConnecte().getRole().getLibelle().name())
                        .build();
                proforma1 = this.CalculateProformaTotal(proforma);
            }
            return this.responseMapper.responseProformaDTO(this.proformaRepository.save(proforma1));

        } else if (saveProformaDTO.client_id() != null) {
            Optional<Client> client = this.clientRepository.findById(saveProformaDTO.client_id()).stream().findFirst();
            if (client.isEmpty()) {
                throw new EntityExistsException("Client not found");
            }
            Proforma proforma = Proforma.builder()
                    .numero(this.numeroGenerateur.GenerateproformaNumero())
                    .client(client.get())
                    .uniqueIdDossier(UUID.randomUUID())
                    .reference(saveProformaDTO.reference())
                    .articleQuantiteList(this.articleQuantiteService
                            .saveAllArticleQuantitelist(saveProformaDTO.articleQuantiteslist()))
                    .build();
            return this.responseMapper
                    .responseProformaDTO(this.proformaRepository.save(this.CalculateProformaTotal(proforma)));
        } else {
            throw new EntityExistsException("Projet or Client not found");
        }
    }

    @Override
    public ProformaDTO saveProforma(SaveProformaWithArticleDTO saveProformaDTO) {

        Proforma proforma = Proforma.builder()
                .numero(this.numeroGenerateur.GenerateproformaNumero())
                .reference(saveProformaDTO.reference()).build();
        List<SaveArticleQuantiteDTO> articleQuantiteDTOS = saveProformaDTO.articleQuantiteslist().stream().map(item -> {
            Article a = this.articleRepository.save(Article.builder()
                    .libelle(item.libelle())
                    .prix_unitaire(item.prix_unitaire())
                    .description(item.description())
                    .build());
            return new SaveArticleQuantiteDTO(a.getId(), item.quantite(), a.getPrix_unitaire());
        }).toList();

        if (saveProformaDTO.projet_id() != null) {
            Optional<Projet> projet = this.projetRepository.findById(saveProformaDTO.projet_id()).stream().findFirst();
            if (projet.isPresent()) {
                proforma.setReference(saveProformaDTO.reference());
                proforma.setProjet(projet.get());
                proforma.setClient(projet.get().getClient());
                proforma.setUniqueIdDossier(UUID.randomUUID());
                proforma.setSignedBy(this.utilisateurConnecteServie.getUtilisateurConnecte().getNom() + " "
                        + this.utilisateurConnecteServie.getUtilisateurConnecte().getPrenom());
                proforma.setArticleQuantiteList(
                        this.articleQuantiteService.saveAllArticleQuantitelist(articleQuantiteDTOS));
                proforma.setRole(this.utilisateurConnecteServie.getUtilisateurConnecte().getRole().getLibelle().name());
            }
            return this.responseMapper
                    .responseProformaDTO(this.proformaRepository.save(this.CalculateProformaTotal(proforma)));

        } else if (saveProformaDTO.client_id() != null) {
            Optional<Client> client = this.clientRepository.findById(saveProformaDTO.client_id()).stream().findFirst();
            if (client.isEmpty()) {
                throw new EntityExistsException("Client not found");
            }

            proforma.setClient(client.get());
            proforma.setReference(saveProformaDTO.reference());
            proforma.setUniqueIdDossier(UUID.randomUUID());
            proforma.setArticleQuantiteList(
                    this.articleQuantiteService.saveAllArticleQuantitelist(articleQuantiteDTOS));

            return this.responseMapper
                    .responseProformaDTO(this.proformaRepository.save(this.CalculateProformaTotal(proforma)));
        } else {
            throw new EntityExistsException("Projet or Client not found");
        }
    }

    @Override
    public ProformaDTO update(UUID id, SaveProformaWithArticleDTO saveProformaDTO) {
        Optional<Proforma> proforma = this.proformaRepository.findById(id).stream().findFirst();
        if (proforma.isPresent()) {
            proforma.get().setOldversion(true);
            Proforma oldprofoma = this.proformaRepository.save(proforma.get());
            List<SaveArticleQuantiteDTO> articleQuantiteDTOS = saveProformaDTO.articleQuantiteslist().stream()
                    .map(item -> {
                        Article a = this.articleRepository.save(Article.builder()
                                .libelle(item.libelle())
                                .prix_unitaire(item.prix_unitaire())
                                .description(item.description())
                                .build());
                        return new SaveArticleQuantiteDTO(a.getId(), item.quantite(), a.getPrix_unitaire());
                    }).toList();
            Proforma proformanew = Proforma.builder()
                    .numero(this.numeroGenerateur.GenerateproformaNumero())
                    .reference(oldprofoma.getReference())
                    .projet(oldprofoma.getProjet())
                    .client(oldprofoma.getClient())
                    .uniqueIdDossier(oldprofoma.getUniqueIdDossier())
                    .signedBy(this.utilisateurConnecteServie.getUtilisateurConnecte().getNom() + " "
                            + this.utilisateurConnecteServie.getUtilisateurConnecte().getPrenom())
                    .articleQuantiteList(this.articleQuantiteService.saveAllArticleQuantitelist(articleQuantiteDTOS))
                    .role(this.utilisateurConnecteServie.getUtilisateurConnecte().getRole().getLibelle().name())
                    .build();

            return this.responseMapper
                    .responseProformaDTO(this.proformaRepository.save(this.CalculateProformaTotal(proformanew)));
        } else {
            throw new EntityExistsException("Proforma not found");
        }

    }

    @Override
    public String updateProformaReference(UUID id, String newReference) {
        Optional<Proforma> proforma = this.proformaRepository.findById(id).stream().findFirst();
        if (proforma.isPresent()) {
            proforma.get().setReference(newReference);
            this.proformaRepository.save(proforma.get());
            return "Reference updated";
        } else {
            throw new EntityExistsException("Proforma not found");
        }
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
            return this.responseMapper
                    .responseProformaDTO(this.proformaRepository.save(this.CalculateProformaTotal(proformanew)));
        } else {
            throw new EntityExistsException("Proforma not found");

        }
    }

    @Override
    public void deleteProforma(String numero) {
        Optional<Proforma> proforma = this.proformaRepository.findByNumero(numero);
        if (proforma.isPresent()) {
            try {
                proforma.get().setDeleted(true);
                this.proformaRepository.save(proforma.get());
            } catch (Exception exception) {
                System.out.println("Cause -> \n" + exception.getCause() + "\nmessage ->\n" + exception.getMessage());
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
    public CustomPageResponse<ProformaDTO> getProformas(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(
                this.proformaRepository
                        .findAllByDeletedIsFalse(PageMapperUtils.createPageableWithoutSearch(paginatedRequest)),
                this.responseMapper::responseProformaDTO);
    }

    @Override
    public CustomPageResponse<ProformaDTO> getProformasNotAdopted(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(
                this.proformaRepository.findAllByDeletedIsFalseAndAdoptedIsFalseAndOldversionIsFalse(
                        PageMapperUtils.createPageableWithoutSearch(paginatedRequest)),
                this.responseMapper::responseProformaDTO);
    }

    @Override
    public CustomPageResponse<String> getProformasNumereList(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(
                this.proformaRepository.findAllByDeletedIsFalse(
                        PageMapperUtils.createPageableWithoutSearch(paginatedRequest)),
                Proforma::getNumero);
    }

    @Override
    public ProformaDTO signerProforma(UUID id, String who_signed) {
        Optional<Proforma> proforma = this.proformaRepository.findById(id).stream().findFirst();
        if (proforma.isPresent()) {
            proforma.get().setSignedBy(who_signed);
            return this.responseMapper.responseProformaDTO(this.proformaRepository.save(proforma.get()));
        } else {
            throw new EntityExistsException("Proforma not found");
        }
    }

    @Override
    public ProformaDTO signerProformaWithNumner(String number, String who_signed) {
        Optional<Proforma> proforma = this.proformaRepository.findByNumero(number);
        if (proforma.isPresent()) {
            proforma.get().setSignedBy(who_signed);
            return this.responseMapper.responseProformaDTO(this.proformaRepository.save(proforma.get()));
        } else {
            throw new EntityExistsException("Proforma not found with number: " + number);
        }
    }

    @Override
    public ProformaDTO signedbywhoconnectProforma(UUID id) {
        Optional<Proforma> proforma = this.proformaRepository.findById(id).stream().findFirst();
        if (proforma.isPresent()) {
            proforma.get().setSignedBy(this.utilisateurConnecteServie.getUtilisateurConnecte().getNom() + " "
                    + this.utilisateurConnecteServie.getUtilisateurConnecte().getPrenom());
            return this.responseMapper.responseProformaDTO(this.proformaRepository.save(proforma.get()));
        } else {
            throw new EntityExistsException("Proforma not found");
        }
    }

    @Override
    public Proforma getProformaEntity(String numero) {
        Optional<Proforma> proforma = this.proformaRepository.findByNumero(numero);
        if (proforma.isPresent()) {
            return proforma.get();
        } else {
            throw new EntityExistsException("Proforma not found");
        }
    }

    private Proforma CalculateProformaTotal(Proforma proforma) {
        Float totalHt = proforma.getArticleQuantiteList().stream()
                .map(articleQuantite -> articleQuantite.getPrix_article() * articleQuantite.getQuantite())
                .reduce(0f, Float::sum);
        proforma.setTotal_ht(totalHt);
        proforma.setTotal_tva(proforma.getTotal_ht() * 0.18f);
        proforma.setTotal_ttc(proforma.getTotal_ht() + proforma.getTotal_tva());
        return proforma;
    }
}
