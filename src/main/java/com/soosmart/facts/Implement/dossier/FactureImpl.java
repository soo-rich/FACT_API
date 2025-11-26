package com.soosmart.facts.Implement.dossier;

import com.soosmart.facts.dto.dossier.TreeNodeDto;
import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Document;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.entity.dossierexterne.PurchaseOrder;
import com.soosmart.facts.enumpack.TreeNodeType;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.dossier.*;
import com.soosmart.facts.service.dossier.FactureService;
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
public class FactureImpl implements FactureService {

    private final FactureDao factureDao;
    private final BorderauDao borderauDao;
    private final ProformaDao proformaRepository;
    private final DocumentDAO documentDAO;
    private final PurchaseOrderDao purchaseOrderDao;
    private final NumeroGenerateur numeroGenerateur;
    private final ResponseMapper responseMapper;

    @Override
    public void deleteFacture(UUID id) {
        Optional<Facture> facture = this.factureDao.findById(id).stream().findFirst();
        if (facture.isPresent()) {
            facture.get().setDeleted(true);
            this.factureDao.save(facture.get());
        } else {
            throw new IllegalArgumentException("Facture not found");
        }
    }

    @Override
    public FactureDto getFacture(String numero) {
        return this.responseMapper.responseFactureDto(this.factureDao.findByNumero(numero).stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("Facture not found")
        ));
    }

    @Override
    public Facture getFactureEntity(String numero) {
        return this.factureDao.findByNumero(numero).stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("Facture not found")
        );
    }

    @Override
    public CustomPageResponse<FactureDto> getFactureAll(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(this.factureDao.findAllByDeletedIsFalse(PageMapperUtils.createPageableWithoutSearch(paginatedRequest)), responseMapper::responseFactureDto);
    }

    @Override
    public CustomPageResponse<String> getFacturesNumereList(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(
                this.factureDao.findAllByDeletedIsFalse(PageMapperUtils.createPageableWithoutSearch(paginatedRequest)),
                Facture::getNumero
        );
    }

    @Override
    public FactureDto saveFacture(UUID id_borderau) {
        try {
            Optional<Bordereau> bordereau = this.borderauDao.findById(id_borderau).stream().findFirst();

            if (bordereau.isPresent()) {
                Proforma proforma = this.proformaRepository.findByReferenceAndDeletedIsFalseAndOldversionIsFalse((bordereau.get().getReference()));
                if (proforma != null) {
                    if (proforma.getReference().equalsIgnoreCase(bordereau.get().getReference())) {
                        bordereau.get().setAdopted(true);
                        this.borderauDao.save(bordereau.get());
                        return this.responseMapper.responseFactureDto(this.factureDao.save(Facture.builder()
                                        .numero(this.numeroGenerateur.GenerateFactureNumero())
                                        .reference(proforma.getReference())
                                        .bordereau(bordereau.get())
                                        .total_ttc(proforma.getTotal_ttc())
                                        .total_ht(proforma.getTotal_ht())
                                        .total_tva(proforma.getTotal_tva())
                                        .uniqueIdDossier(bordereau.get().getUniqueIdDossier())
                                        .signedBy(proforma.getSignedBy())
                                        .build()
                                )
                        );
                    } else {
                        throw new IllegalArgumentException("Bordereau reference not match with proforma reference");
                    }

                } else {
                    throw new EntityExistsException("Proforma not found");
                }
            } else {
                throw new EntityExistsException("Bordereau not found");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong while saving facture: " + e.getMessage());
        }
    }

    @Override
    public FactureDto signerFactureWithNumner(String numero, String who_signed, String signedByRole) {
        Optional<Facture> facture = this.factureDao.findByNumero(numero).stream().findFirst();
        if (facture.isPresent()) {
            facture.get().setSignedBy(who_signed);
            facture.get().setRole(signedByRole);
            return this.responseMapper.responseFactureDto(this.factureDao.save(facture.get()));
        } else {
            throw new EntityExistsException("Facture not found");
        }
    }

    @Override
    public Boolean paid(UUID id_facture) {
        Optional<Facture> facture = this.factureDao.findById(id_facture).stream().findFirst();
        if (facture.isPresent()) {
            facture.get().setIsPaid(true);
            this.factureDao.save(facture.get());
            return facture.get().getIsPaid();
        } else {
            throw new IllegalArgumentException("Facture not found");
        }
    }

    @Override
    public TreeNodeDto getFacturesByDossier(String numero) {
        Optional<Facture> facture = this.factureDao.findByNumero(numero);
        if (facture.isEmpty()) {
            throw new EntityExistsException("Facture not found");
        }

        Facture factureEntity = facture.get();

        // Récupérer le bordereau de la facture
        Bordereau bordereau = factureEntity.getBordereau();
        if (bordereau == null) {
            throw new IllegalArgumentException("Bordereau not found for this facture");
        }

        // Récupérer le bon de commande associé au bordereau
        PurchaseOrder purchaseOrder = this.purchaseOrderDao.findByBordereau_Id(bordereau.getId());

        // Récupérer toutes les proformas ayant la même référence que le bordereau
        List<Document> documents = this.documentDAO.findByUniqueIdDossierAndDeletedIsFalse(factureEntity.getUniqueIdDossier());
        List<TreeNodeDto> proformaNodes = documents.stream()
                .filter(d -> d instanceof Proforma)
                .map(d -> (Proforma) d)
                .filter(p -> p.getReference().equals(bordereau.getReference()))
                .map(p -> new TreeNodeDto(
                        p.getId(),
                        TreeNodeType.PROFORMA,
                        p.getNumero(),
                        p.getReference(),
                        p.getAdopted(),
                        null // Les proformas n'ont pas d'enfants
                ))
                .toList();

        // Créer le noeud bon de commande
        TreeNodeDto bonCommandeNode = purchaseOrder != null ? new TreeNodeDto(
                purchaseOrder.getId(),
                TreeNodeType.BON_DE_COMMANDE,
                null, // Le bon de commande n'a pas de numéro dans votre modèle
                bordereau.getReference(),
                null,
                null // Le bon de commande n'a pas d'enfants
        ) : null;

        // Créer la liste des enfants du bordereau (bon de commande + proformas)
        List<TreeNodeDto> bordereauChildren = new java.util.ArrayList<>();
        if (bonCommandeNode != null) {
            bordereauChildren.add(bonCommandeNode);
        }
        bordereauChildren.addAll(proformaNodes);

        // Créer le noeud bordereau
        TreeNodeDto bordereauNode = new TreeNodeDto(
                bordereau.getId(),
                TreeNodeType.BORDEREAU,
                bordereau.getNumero(),
                bordereau.getReference(),
                bordereau.getAdopted(),
                bordereauChildren
        );

        // Créer le noeud facture (racine)
        return new TreeNodeDto(
                factureEntity.getId(),
                TreeNodeType.FACTURE,
                factureEntity.getNumero(),
                factureEntity.getReference(),
                true,
                List.of(bordereauNode)
        );
    }
}