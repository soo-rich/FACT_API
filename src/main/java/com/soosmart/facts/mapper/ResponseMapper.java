package com.soosmart.facts.mapper;

import com.soosmart.facts.dto.Article.ArticleDTO;
import com.soosmart.facts.dto.articleQuantite.ArticleQuantiteDTO;
import com.soosmart.facts.dto.client.ClientDTO;
import com.soosmart.facts.dto.dossier.borderau.BorderauDto;
import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.dto.dossier.proforma.ProformaDTO;
import com.soosmart.facts.dto.project.ProjetDTO;
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
import com.soosmart.facts.entity.user.Utilisateur;
import com.soosmart.facts.enumpack.TypeDeRole;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {
    public ResponseUtilisateur responseUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }

        if (utilisateur.getRole().getLibelle().equals(TypeDeRole.SUPER_ADMIN)) {
            return null;
        }

        return new ResponseUtilisateur(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getNumero(),
                utilisateur.getEmail(),
                utilisateur.getUsername(),
                utilisateur.getRole().getLibelle().name(),
                utilisateur.getCreatedat(),
                utilisateur.getActif()
        );
    }

    public ArticleDTO responseArticleDTO(Article article) {
        if (article == null) {
            return null;
        }
        return new ArticleDTO(
                article.getId(),
                article.getLibelle(),
                article.getPrix_unitaire()
        );
    }

    public ClientDTO responseClientDTO(Client client) {
        if (client == null) {
            return null;
        }
        return new ClientDTO(
                client.getId(),
                client.getLieu(),
                client.getNom(),
                client.getSigle(),
                client.getTelephone(),
                client.getPotentiel()
        );
    }

    public ProjetDTO responseProjetDTO(Projet projet) {
        if (projet == null) {
            return null;
        }
        return new ProjetDTO(
                projet.getId(),
                projet.getProjetType(),
                projet.getDescription(),
                projet.getOffre(),
                projet.getClient().getNom(),
                projet.getCreatedat(),
                projet.getUpdate_at()
        );
    }

    public ArticleQuantiteDTO responseArticleQuantiteDTO(ArticleQuantite articleQuantite) {
        if (articleQuantite == null) {
            return null;
        }
        return new ArticleQuantiteDTO(
                articleQuantite.getId(),
                articleQuantite.getArticle().getLibelle(),
                articleQuantite.getQuantite(),
                articleQuantite.getPrix_article()
        );
    }

    public ProformaDTO responseProformaDTO(Proforma proforma) {
        if (proforma == null) {
            return null;
        }
        return new ProformaDTO(
                proforma.getId(),
                proforma.getReference(),
                proforma.getNumero(),
                proforma.getArticleQuantiteList().stream().map(this::responseArticleQuantiteDTO).toList(),
                proforma.getTotal_ht(),
                proforma.getTotal_ttc(),
                proforma.getTotal_tva(),
                proforma.getClient().getNom(),
                proforma.getCreatedat(),
                proforma.getSignedBy(),
                proforma.getAdopted()
        );
    }

    public BorderauDto responseBorderauDto(Bordereau borderau) {
        if (borderau == null) {
            return null;
        }
        System.out.println("Adopted: " + borderau.getAdopted());
        return new BorderauDto(
                borderau.getId(),
                borderau.getReference(),
                borderau.getNumero(),
                borderau.getProforma().getArticleQuantiteList().stream().map(this::responseArticleQuantiteDTO).toList(),
                borderau.getProforma().getTotal_ht(),
                borderau.getProforma().getTotal_ttc(),
                borderau.getProforma().getTotal_tva(),
                borderau.getProforma().getClient().getNom(),
                borderau.getAdopted(),
                borderau.getCreatedat()
        );
    }

    public FactureDto responseFactureDto(Facture facture) {
        if (facture == null) {
            return null;
        }
        return new FactureDto(
                facture.getId(),
                facture.getReference(),
                facture.getNumero(),
                facture.getBordereau().getProforma().getArticleQuantiteList().stream().map(this::responseArticleQuantiteDTO).toList(),
                facture.getBordereau().getProforma().getTotal_ht(),
                facture.getBordereau().getProforma().getTotal_ttc(),
                facture.getBordereau().getProforma().getTotal_tva(),
                facture.getBordereau().getProforma().getClient().getNom(),
                facture.getBordereau().getProforma().getCreatedat(),
                facture.getSignedBy()
        );
    }

    public Table responseTable(Document document) {
        if (document == null) {
            return null;
        }
        return new Table(
                document.getNumero(),
                document.getCreatedat(),
                document.getTotal_ttc()
        );
    }
}
