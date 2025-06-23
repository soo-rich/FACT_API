package com.soosmart.facts.Implement.dossier;

import com.soosmart.facts.dto.dossier.borderau.BorderauDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.dossier.BorderauDao;
import com.soosmart.facts.repository.dossier.ProformaDao;
import com.soosmart.facts.service.dossier.BordereauService;
import com.soosmart.facts.utils.NumeroGenerateur;
import com.soosmart.facts.utils.pagination.PageMapperUtils;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BordereuImpl implements BordereauService {
    private final BorderauDao borderauRepository;
    private final ProformaDao proformaRepository;
    private final NumeroGenerateur numeroGenerateur;
    private final ResponseMapper responseMapper;

    @Override
    public BorderauDto saveBordereau(UUID id_proforma) {
        Optional<Proforma> proforma = this.proformaRepository.findById(id_proforma).stream().findFirst();
        if (proforma.isPresent()) {
            proforma.get().setAdopted(true);
            Proforma save = this.proformaRepository.save(proforma.get());
            Bordereau bordereau = Bordereau.builder()
                    .numero(this.numeroGenerateur.GenerateBordereauNumero())
                    .reference(save.getReference())
                    .proforma(save)
                    .total_ttc(save.getTotal_ttc())
                    .total_ht(save.getTotal_ht())
                    .total_tva(save.getTotal_tva())
                    .build();
            return this.responseMapper.responseBorderauDto(this.borderauRepository.save(bordereau));

        } else {
            throw new EntityExistsException("Proforma not found");
        }
    }

    @Override
    public CustomPageResponse<BorderauDto> getBordereauAll(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(this.borderauRepository.findAllByDeletedIsFalse(PageMapperUtils.createPageableWithoutSearch(paginatedRequest)), responseMapper::responseBorderauDto);
    }

    @Override
    public CustomPageResponse<BorderauDto> getBordereauAllNotAdopted(PaginatedRequest paginatedRequest) {
         return PageMapperUtils.toPageResponse(this.borderauRepository.findAllByDeletedIsFalseAndAdoptedIsFalse(PageMapperUtils.createPageableWithoutSearch(paginatedRequest)), responseMapper::responseBorderauDto);
    }

    @Override
    public BorderauDto getBordereau(UUID id) {
        return this.responseMapper.responseBorderauDto(this.borderauRepository.findById(id).stream().findFirst().orElseThrow(
                () -> new EntityExistsException("Bordereau not found")
        ));
    }

    @Override
    public void deleteBordereau(UUID id) {
        Optional<Bordereau> bordereau = this.borderauRepository.findById(id).stream().findFirst();
        if (bordereau.isPresent()) {
            bordereau.get().setDeleted(true);
            this.borderauRepository.save(bordereau.get());
        } else {
            throw new EntityExistsException("Bordereau not found");
        }
    }

    @Override
    public BorderauDto getBordereauByNumero(String numero) {
        return this.responseMapper.responseBorderauDto(this.borderauRepository.findByNumero(numero).stream().findFirst().orElseThrow(
                () -> new EntityExistsException("Bordereau not found")
        ));
    }

    @Override
    public Bordereau getBordereauEntity(String numero) {
        return this.borderauRepository.findByNumero(numero).stream().findFirst().orElseThrow(
                () -> new EntityExistsException("Bordereau not found")
        );
    }
}
