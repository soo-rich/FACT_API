package com.soosmart.facts.Implement.dossier;

import com.soosmart.facts.dto.dossier.purchseorder.PurchaseOderDto;
import com.soosmart.facts.dto.dossier.purchseorder.PurchaseOderOneDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.entity.dossierexterne.PurchaseOrder;
import com.soosmart.facts.exceptions.EntityNotFound;
import com.soosmart.facts.exceptions.file.FileValidationException;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.dossier.BorderauDao;
import com.soosmart.facts.repository.dossier.ProformaDao;
import com.soosmart.facts.repository.dossier.PurchaseOrderDao;
import com.soosmart.facts.service.dossier.ProformaService;
import com.soosmart.facts.service.dossier.PurchaseOrderService;
import com.soosmart.facts.service.file.FileMetadataService;
import com.soosmart.facts.utils.FileValidationService;
import com.soosmart.facts.utils.NumeroGenerateur;
import com.soosmart.facts.utils.pagination.PageMapperUtils;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PurchaseOrderImpl implements PurchaseOrderService {

    private FileValidationService fileValidationService;
    private final FileMetadataService fileMetadataService;
    private final ProformaService proformaService;
    private final BorderauDao borderauRepository;

    private final ProformaDao proformaRepository;
    private final NumeroGenerateur numeroGenerateur;

    private final PurchaseOrderDao purchaseOrderDao;
    private final ResponseMapper responseMapper;

    @Override
    public PurchaseOderDto savepurchaseOrder(String proformaNumero, MultipartFile file, String filename) {
        FileValidationService.ValidationResult validationResult = fileValidationService.validateFile(file);

        if (!validationResult.valid()) {
            throw new FileValidationException(validationResult.errorMessage());
        }

        Proforma proforma = this.proformaService.getProformaEntity(proformaNumero);
        if (proforma != null) {
            proforma.setAdopted(true);
            Proforma save = this.proformaRepository.save(proforma);
            Bordereau bordereau = Bordereau.builder()
                    .numero(this.numeroGenerateur.GenerateBordereauNumero())
                    .reference(save.getReference())
                    .proforma(save)
                    .total_ttc(save.getTotal_ttc())
                    .total_ht(save.getTotal_ht())
                    .total_tva(save.getTotal_tva())
                    .build();

            Bordereau b = this.borderauRepository.save(bordereau);

            return this.responseMapper
                    .responsePurchaseOder(
                            this.purchaseOrderDao
                                    .save(
                                            PurchaseOrder
                                                    .builder()
                                                    .proforma(save)
                                                    .bordereau(b)

                                                    .file(this.fileMetadataService.save(file, filename, "bc"))
                                                    .build()));
        } else {
            throw new EntityExistsException("Proforma not found");
        }
    }

    @Override
    public PurchaseOderOneDto findOne(UUID id) {
        return this.responseMapper.responsePurchaseOderOne(
                this.purchaseOrderDao.findById(id).orElseThrow(() -> new EntityNotFound("Fichier non trouver")));
    }

    @Override
    public CustomPageResponse<PurchaseOderDto> listpurchaseorder(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(
                this.purchaseOrderDao
                        .findAllBySupprimerIsFalse(PageMapperUtils.createPageableWithoutSearch(paginatedRequest)),
                responseMapper::responsePurchaseOder);
    }

    @Override
    public PurchaseOderDto updatepurchase(UUID purchaseOrderId, MultipartFile file) {
        PurchaseOrder exciste = this.purchaseOrderDao.findById(purchaseOrderId)
                .orElseThrow(() -> new EntityNotFound("Fichier non trouver"));
        FileValidationService.ValidationResult validationResult = fileValidationService.validateFile(file);

        if (!validationResult.valid()) {
            throw new FileValidationException(validationResult.errorMessage());
        }
        exciste.setFile(this.fileMetadataService.save(file, "bc"));
        return this.responseMapper.responsePurchaseOder(this.purchaseOrderDao.save(exciste));
    }

    @Override
    public Boolean remove(UUID id) {
        Optional<PurchaseOrder> purchaseOrder = this.purchaseOrderDao.findById(id);

        if (purchaseOrder.isPresent()) {
            PurchaseOrder order = purchaseOrder.get();
            order.setSupprimer(true);
            order = this.purchaseOrderDao.save(order);
            return order.getSupprimer();
        } else
            return false;

    }
}
