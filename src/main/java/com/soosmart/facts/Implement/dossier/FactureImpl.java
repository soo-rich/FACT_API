package com.soosmart.facts.Implement.dossier;

import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.dossier.BorderauDao;
import com.soosmart.facts.repository.dossier.FactureDao;
import com.soosmart.facts.repository.dossier.ProformaDao;
import com.soosmart.facts.service.dossier.FactureService;
import com.soosmart.facts.utils.NumeroGenerateur;
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
    public List<FactureDto> getFactureAll() {
        return this.factureDao.findAllByDeletedIsFalse().stream().map(
                this.responseMapper::responseFactureDto
        ).toList();
    }

    @Override
    public List<String> getFacturesNumereList() {
        return this.factureDao.findAllByDeletedIsFalse().stream().map(
                Facture::getNumero
        ).toList();
    }

    @Override
    public FactureDto saveFacture(UUID id_borderau) {
        Optional<Bordereau> bordereau = this.borderauDao.findById(id_borderau).stream().findFirst();
        if (bordereau.isPresent()) {
            Optional<Proforma> proforma = this.proformaRepository.findByReference((bordereau.get().getReference())).stream().findFirst();
            if (proforma.isPresent()) {
                if (proforma.get().getReference().equalsIgnoreCase(bordereau.get().getReference())) {
                    return this.responseMapper.responseFactureDto(this.factureDao.save(Facture.builder()
                                    .numero(this.numeroGenerateur.GenerateFactureNumero())
                                    .reference(proforma.get().getReference())
//                                    .proforma(proforma.get())
                                    .bordereau(bordereau.get())
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
}