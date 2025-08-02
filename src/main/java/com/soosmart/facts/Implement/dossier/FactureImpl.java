package com.soosmart.facts.Implement.dossier;

import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.dossier.BorderauDao;
import com.soosmart.facts.repository.dossier.FactureDao;
import com.soosmart.facts.repository.dossier.ProformaDao;
import com.soosmart.facts.service.dossier.FactureService;
import com.soosmart.facts.utils.NumeroGenerateur;
import com.soosmart.facts.utils.pagination.PageMapperUtils;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FactureImpl implements FactureService {
    private final FactureDao factureDao;
    private final BorderauDao borderauDao;
    private final ProformaDao proformaRepository;
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
        Optional<Bordereau> bordereau = this.borderauDao.findById(id_borderau).stream().findFirst();
        if (bordereau.isPresent()) {
            Optional<Proforma> proforma = this.proformaRepository.findByReference((bordereau.get().getReference())).stream().findFirst();
            if (proforma.isPresent()) {
                if (proforma.get().getReference().equalsIgnoreCase(bordereau.get().getReference())) {
                    bordereau.get().setAdopted(true);
                    this.borderauDao.save(bordereau.get());
                    return this.responseMapper.responseFactureDto(this.factureDao.save(Facture.builder()
                                    .numero(this.numeroGenerateur.GenerateFactureNumero())
                                    .reference(proforma.get().getReference())
                                    .bordereau(bordereau.get())
                                    .total_ttc(proforma.get().getTotal_ttc())
                                    .total_ht(proforma.get().getTotal_ht())
                                    .total_tva(proforma.get().getTotal_tva())
                                    .signedBy(proforma.get().getSignedBy())
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

    }

    @Override
    public Boolean paid(UUID id_facture) {
        Optional<Facture> facture = this.factureDao.findById(id_facture).stream().findFirst();
        if (facture.isPresent()) {
            facture.get().setIsPaid(!facture.get().getIsPaid());
            this.factureDao.save(facture.get());
            return facture.get().getIsPaid();
        } else {
            throw new IllegalArgumentException("Facture not found");
        }  
    }
}