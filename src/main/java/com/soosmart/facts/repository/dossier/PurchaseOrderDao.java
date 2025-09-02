package com.soosmart.facts.repository.dossier;

import com.soosmart.facts.entity.dossierexterne.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseOrderDao extends JpaRepository<PurchaseOrder, UUID> {
    Page<PurchaseOrder> findAllBySupprimerIsFalse(Pageable pageable);
}
