package com.soosmart.facts.mapper;

import com.soosmart.facts.dto.Article.ArticleDTO;
import com.soosmart.facts.dto.articleQuantite.ArticleQuantiteDTO;
import com.soosmart.facts.dto.client.ClientDTO;
import com.soosmart.facts.dto.dossier.borderau.BorderauDto;
import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.dto.dossier.proforma.ProformaDTO;
import com.soosmart.facts.dto.dossier.purchseorder.PurchaseOderDto;
import com.soosmart.facts.dto.dossier.purchseorder.PurchaseOderOneDto;
import com.soosmart.facts.dto.file.FileMetaDataDto;
import com.soosmart.facts.dto.project.ProjetDTO;
import com.soosmart.facts.dto.report.DocumentReportDTO;
import com.soosmart.facts.dto.stat.Table;
import com.soosmart.facts.dto.user.ResponseUtilisateur;
import com.soosmart.facts.entity.Article;
import com.soosmart.facts.entity.ArticleQuantite;
import com.soosmart.facts.entity.Client;
import com.soosmart.facts.entity.Projet;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Document;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.entity.dossierexterne.PurchaseOrder;
import com.soosmart.facts.entity.file.FileMetadata;
import com.soosmart.facts.entity.user.Utilisateur;
import com.soosmart.facts.enumpack.TypeDeRole;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ResponseMapper {
    public ResponseUtilisateur responseUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }

        if (utilisateur.getRole().getLibelle().equals(TypeDeRole.SUPER_ADMIN)) {
            return null;
        }

        return new ResponseUtilisateur(utilisateur.getId(), utilisateur.getNom(), utilisateur.getPrenom(),
                utilisateur.getNumero(), utilisateur.getEmail(), utilisateur.getUsername(),
                utilisateur.getRole().getLibelle().name(), utilisateur.getCreatedat(), utilisateur.getActif());
    }

    public ArticleDTO responseArticleDTO(Article article) {
        if (article == null) {
            return null;
        }
        return new ArticleDTO(article.getId(), article.getLibelle(), article.getDescription(),
                article.getPrix_unitaire());
    }

    public ClientDTO responseClientDTO(Client client) {
        if (client == null) {
            return null;
        }
        return new ClientDTO(client.getId(), client.getLieu(), client.getNom(), client.getSigle(),
                client.getTelephone(), client.getPotentiel());
    }

    public ProjetDTO responseProjetDTO(Projet projet) {
        if (projet == null) {
            return null;
        }
        return new ProjetDTO(projet.getId(), projet.getProjetType(), projet.getDescription(), projet.getOffre(),
                projet.getClient().getNom(), projet.getCreatedat(), projet.getUpdate_at());
    }

    public ArticleQuantiteDTO responseArticleQuantiteDTO(ArticleQuantite articleQuantite) {
        if (articleQuantite == null) {
            return null;
        }
        return new ArticleQuantiteDTO(articleQuantite.getId(), articleQuantite.getArticle().getLibelle(),
                articleQuantite.getArticle().getDescription(), articleQuantite.getQuantite(),
                articleQuantite.getPrix_article());
    }

    public ProformaDTO responseProformaDTO(Proforma proforma) {
        if (proforma == null) {
            return null;
        }
        return new ProformaDTO(proforma.getId(), proforma.getReference(), proforma.getNumero(),
                proforma.getArticleQuantiteList().stream().map(this::responseArticleQuantiteDTO).toList(),
                proforma.getTotal_ht(), proforma.getTotal_ttc(), proforma.getTotal_tva(), proforma.getClient().getNom(),
                proforma.getCreatedat(), proforma.getSignedBy(), proforma.getAdopted(), proforma.getOldversion(), proforma.getUniqueIdDossier());
    }

    public BorderauDto responseBorderauDto(Bordereau borderau) {
        if (borderau == null) {
            return null;
        }
        return new BorderauDto(
                borderau.getId(),
                borderau.getReference(),
                borderau.getNumero(),
                borderau.getProforma().getNumero(),
                borderau.getProforma().getArticleQuantiteList().stream().map(this::responseArticleQuantiteDTO).toList(),
                borderau.getProforma().getTotal_ht(),
                borderau.getProforma().getTotal_ttc(),
                borderau.getProforma().getTotal_tva(),
                borderau.getProforma().getClient().getNom(),
                borderau.getAdopted(),
                borderau.getCreatedat(),
                borderau.getUniqueIdDossier());
    }

    public FactureDto responseFactureDto(Facture facture) {
        if (facture == null) {
            return null;
        }
        return new FactureDto(facture.getId(), facture.getReference(), facture.getNumero(),
                facture.getBordereau().getNumero(),
                facture.getBordereau().getProforma().getArticleQuantiteList().stream()
                        .map(this::responseArticleQuantiteDTO).toList(),
                facture.getBordereau().getProforma().getTotal_ht(), facture.getBordereau().getProforma().getTotal_ttc(),
                facture.getBordereau().getProforma().getTotal_tva(),
                facture.getBordereau().getProforma().getClient().getNom(),
                facture.getBordereau().getProforma().getCreatedat(), facture.getSignedBy(),
                facture.getUniqueIdDossier());
    }

    public Table responseTable(Document document) {
        if (document == null) {
            return null;
        }
        return new Table(document.getNumero(), document.getCreatedat(), document.getTotal_ttc());
    }

    public FileMetaDataDto responseFileMetadate(FileMetadata fileMetadata) {
        if (fileMetadata == null) {
            return null;
        }
        return new FileMetaDataDto(fileMetadata.getFileName(), fileMetadata.getStorageUrl(),
                fileMetadata.getContentType(), fileMetadata.getFileSize(), fileMetadata.getUploadedBy(),
                fileMetadata.getStorageProvider(), fileMetadata.getUpdate_at());
    }

    public PurchaseOderDto responsePurchaseOder(PurchaseOrder purchaseOrder) {
        if (purchaseOrder == null) {
            return null;
        }
        return new PurchaseOderDto(purchaseOrder.getId(), purchaseOrder.getProforma().getNumero(),
                purchaseOrder.getBordereau().getNumero(), this.responseFileMetadate(purchaseOrder.getFile()));
    }

    public PurchaseOderOneDto responsePurchaseOderOne(PurchaseOrder purchaseOrder) {
        if (purchaseOrder == null)
            return null;
        return new PurchaseOderOneDto(
                this.responseBorderauDto(purchaseOrder.getBordereau()),
                this.responseProformaDTO(purchaseOrder.getProforma()),
                this.responseFileMetadate(purchaseOrder.getFile()));
    }

    public DocumentReportDTO responseDocumentReportDTO(Object document) {
        if (document == null) {
            return null;
        }


        if (document instanceof Proforma p) {
            if (p.getSignedBy().contains("null") || p.getSignedBy().isEmpty()) {
                p.setSignedBy(null);
            }
            return new DocumentReportDTO(
                    p.getReference(),
                    p.getNumero(),
                    Date.from(p.getCreatedat()),
                    p.getArticleQuantiteList().stream().map(this::responseArticleQuantiteDTO).toList(),
                    this.responseClientDTO(p.getClient()),
                    p.getRole(),
                    p.getSignedBy(),
                    p.getTotal_ht().doubleValue(),
                    p.getTotal_tva().doubleValue(),
                    p.getTotal_ttc().doubleValue()
            );

        }
        if (document instanceof Bordereau b) {
             if (b.getSignedBy().contains("null") || b.getSignedBy().isEmpty()) {
                b.setSignedBy(null);
            }
            return new DocumentReportDTO(
                    b.getReference(),
                    b.getNumero(),
                    Date.from(b.getCreatedat()),
                    b.getProforma().getArticleQuantiteList().stream().map(this::responseArticleQuantiteDTO).toList(),
                    this.responseClientDTO(b.getProforma().getClient()),
                    b.getRole(),
                    b.getSignedBy(),
                    b.getTotal_ht().doubleValue(),
                    b.getTotal_tva().doubleValue(),
                    b.getTotal_ttc().doubleValue()
            );
        }
        if (document instanceof Facture f) {
            if (f.getSignedBy().contains("null") || f.getSignedBy().isEmpty()) {
                f.setSignedBy(null);
            }
            return new DocumentReportDTO(
                    f.getReference(),
                    f.getNumero(),
                    Date.from(f.getCreatedat()),
                    f.getBordereau().getProforma().getArticleQuantiteList().stream().map(this::responseArticleQuantiteDTO).toList(),
                    this.responseClientDTO(f.getBordereau().getProforma().getClient()),
                    f.getRole(),
                    f.getSignedBy(),
                    f.getTotal_ht().doubleValue(),
                    f.getTotal_tva().doubleValue(),
                    f.getTotal_ttc().doubleValue()
            );
        }
        throw new IllegalArgumentException("Type de document non reconnu");
    }
}
