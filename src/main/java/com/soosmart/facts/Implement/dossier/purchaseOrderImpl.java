package com.soosmart.facts.Implement.dossier;

import com.soosmart.facts.dto.dossier.purchseorder.PurchaseOderDto;
import com.soosmart.facts.dto.dossier.purchseorder.PurchaseOderOneDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.dossierexterne.PurchaseOrder;
import com.soosmart.facts.exceptions.EntityNotFound;
import com.soosmart.facts.exceptions.file.FileValidationException;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.dossier.PurchaseOrderDao;
import com.soosmart.facts.service.dossier.ProformaService;
import com.soosmart.facts.service.dossier.PurchaseOrderService;
import com.soosmart.facts.service.file.FileMetadataService;
import com.soosmart.facts.utils.FileValidationService;
import com.soosmart.facts.utils.pagination.PageMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class purchaseOrderImpl implements PurchaseOrderService {

    private FileValidationService fileValidationService;
    private final FileMetadataService fileMetadataService;
    private final ProformaService proformaService;
    private final PurchaseOrderDao purchaseOrderDao;
    private final ResponseMapper responseMapper;

    @Override
    public PurchaseOderDto savepurchaseOrder(String proformaNumero, MultipartFile file) {
        FileValidationService.ValidationResult validationResult =
                fileValidationService.validateFile(file);

        if (!validationResult.valid()) {
            throw new FileValidationException(validationResult.errorMessage());
        }

        return this.responseMapper.responsePurchaseOder(this.purchaseOrderDao.save(PurchaseOrder.builder().proforma(this.proformaService.getProformaEntity(proformaNumero)).file(this.fileMetadataService.save(file, "bc")).build()));
    }

    @Override
    public PurchaseOderOneDto findOne(UUID id) {
        return this.responseMapper.responsePurchaseOderOne(this.purchaseOrderDao.findById(id).orElseThrow(() -> new EntityNotFound("Fichier non trouver")));
    }

    @Override
    public CustomPageResponse<PurchaseOderDto> listpurchaseorder(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(this.purchaseOrderDao.findAllBySupprimerIsFalse(PageMapperUtils.createPageableWithoutSearch(paginatedRequest)), responseMapper::responsePurchaseOder);
    }

    @Override
    public PurchaseOderDto updatepurchase(UUID purchaseOrderId, MultipartFile file) {
        return null;
    }

    @Override
    public Boolean remove(UUID id) {
        Optional<PurchaseOrder> purchaseOrder = this.purchaseOrderDao.findById(id);

        if (purchaseOrder.isPresent()) {
            PurchaseOrder order = purchaseOrder.get();
            order.setSupprimer(true);
            order = this.purchaseOrderDao.save(order);
            return order.getSupprimer();
        } else return false;

    }
}
