package com.soosmart.facts.service.dossier;

import com.soosmart.facts.dto.dossier.purchseorder.PurchaseOderDto;
import com.soosmart.facts.dto.dossier.purchseorder.PurchaseOderOneDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PurchaseOrderService {

    PurchaseOderDto savepurchaseOrder(String proformaNumero, String bordereauNumero,  MultipartFile file);

    PurchaseOderOneDto findOne(UUID id);

    CustomPageResponse<PurchaseOderDto> listpurchaseorder(PaginatedRequest paginatedRequest);

    PurchaseOderDto updatepurchase(UUID purchaseOrderId, MultipartFile file);

    Boolean remove(UUID id);

}
