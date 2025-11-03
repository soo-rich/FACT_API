package com.soosmart.facts.controller.dossier;

import com.soosmart.facts.dto.dossier.purchseorder.PurchaseOderDto;
import com.soosmart.facts.dto.dossier.purchseorder.PurchaseOderOneDto;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.service.dossier.PurchaseOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("purchase-order")
@AllArgsConstructor
public class purchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;


    @GetMapping
    public ResponseEntity<CustomPageResponse<PurchaseOderDto>> getall(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pagesize", defaultValue = "10") int pagesize,
            @RequestParam(value = "search", defaultValue = "") String search
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.purchaseOrderService.listpurchaseorder(new PaginatedRequest(page, pagesize, search)));
    }

    // @PostMapping( consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
    // public ResponseEntity<PurchaseOderDto> save(@RequestPart(value = "proforma") String proforma, @RequestPart(value = "bc") MultipartFile bc) {
    //     return ResponseEntity.status(HttpStatus.CREATED).body(this.purchaseOrderService.savepurchaseOrder(proforma, bc));
    // }

    @PutMapping
    public ResponseEntity<PurchaseOderDto> update(@RequestPart(value = "id") UUID id, @RequestPart(value = "bc") MultipartFile bc) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.purchaseOrderService.updatepurchase(id, bc));
    }

    @GetMapping("{id}")
    public ResponseEntity<PurchaseOderOneDto> getpurchaseorder(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(this.purchaseOrderService.findOne(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(this.purchaseOrderService.remove(id));
    }

}
