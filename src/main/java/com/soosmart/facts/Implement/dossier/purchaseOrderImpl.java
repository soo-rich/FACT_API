package com.soosmart.facts.Implement.dossier;

import com.soosmart.facts.dto.dossier.purchseorder.PurchaseOderDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.dossierexterne.PurchaseOrder;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.dossier.PurchaseOrderDao;
import com.soosmart.facts.service.dossier.BordereauService;
import com.soosmart.facts.service.dossier.ProformaService;
import com.soosmart.facts.service.dossier.purchaseOrderService;
import com.soosmart.facts.service.file.FileMetadataService;
import com.soosmart.facts.utils.pagination.PageMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@AllArgsConstructor
public class purchaseOrderImpl implements purchaseOrderService {

    private final FileMetadataService fileMetadataService;
    private final ProformaService proformaService;
    private final BordereauService bordereauService;
    private final PurchaseOrderDao purchaseOrderDao;
    private final ResponseMapper responseMapper;

    @Override
    public PurchaseOderDto savepurchaseOrder(String proformaNumero, MultipartFile file) {
        return
                this.responseMapper.responsePurchaseOder(
                        this.purchaseOrderDao.save(PurchaseOrder.builder()
                                .proforma(this.proformaService.getProformaEntity(proformaNumero))
                                .file(this.fileMetadataService.save(file))
                                .build()));
    }

    @Override
    public CustomPageResponse<PurchaseOderDto> listpurchaseorder(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(this.purchaseOrderDao.findAllBySupprimerIsFalse(PageMapperUtils.createPageableWithoutSearch(paginatedRequest)),
                responseMapper::responsePurchaseOder
        );
    }

    @Override
    public PurchaseOderDto updatepurchase(UUID purchaseOrderId, MultipartFile file) {
        return null;
    }

    @Override
    public Boolean remove(UUID id) {
        return null;
    }
}
