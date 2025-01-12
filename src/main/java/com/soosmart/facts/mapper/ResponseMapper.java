package com.soosmart.facts.mapper;

import com.soosmart.facts.dto.Article.ArticleDTO;
import com.soosmart.facts.dto.articleQuantite.ArticleQuantiteDTO;
import com.soosmart.facts.dto.client.ClientDTO;
import com.soosmart.facts.dto.dossier.borderau.BorderauDto;
import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.dto.dossier.proforma.ProformaDTO;
import com.soosmart.facts.dto.project.ProjetDTO;
import com.soosmart.facts.dto.user.ResponseUtilisateur;
import com.soosmart.facts.entity.*;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.entity.user.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {
    public ResponseUtilisateur responseUtilisateur(Utilisateur utilisateur){
        if (utilisateur == null){
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
                utilisateur.getCreatedAt(),
                utilisateur.getActif()
        );
    }

    public ArticleDTO responseArticleDTO (Article article){
        if(article== null) {
            return null;
        }
        return new ArticleDTO(
                article.getId(),
                article.getLibelle(),
                article.getPrix_unitaire()
        );
    }

    public ClientDTO responseClientDTO(Client client){
        if (client==null){
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

    public ProjetDTO responseProjetDTO(Projet projet){
        if (projet==null){
            return null;
        }
        return new ProjetDTO(
                projet.getId(),
                projet.getProjet_type(),
                projet.getDescription(),
                projet.getOffre(),
                projet.getClient().getNom(),
                projet.getCreate_at(),
                projet.getUpdate_at()
        );
    }

    public ArticleQuantiteDTO responseArticleQuantiteDTO(ArticleQuantite articleQuantite){
        if (articleQuantite==null){
            return null;
        }
        return new ArticleQuantiteDTO(
                articleQuantite.getId(),
                articleQuantite.getArticle().getLibelle(),
                articleQuantite.getQuantite(),
                articleQuantite.getPrix_article()
        );
    }

    public ProformaDTO responseProformaDTO(Proforma proforma){
        if (proforma==null){
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
                proforma.getCreate_at(),
                proforma.getSignedBy(),
                proforma.getAdopted()
        );
    }

    public BorderauDto responseBorderauDto(Bordereau borderau){
        if (borderau==null){
            return null;
        }
        return new BorderauDto(
                borderau.getId(),
                borderau.getReference(),
                borderau.getNumero(),
                borderau.getProforma().getArticleQuantiteList().stream().map(this::responseArticleQuantiteDTO).toList(),
                borderau.getProforma().getTotal_ht(),
                borderau.getProforma().getTotal_ttc(),
                borderau.getProforma().getTotal_tva(),
                borderau.getProforma().getClient().getNom(),
                borderau.getCreate_at()
        );
    }

    public FactureDto responseFactureDto(Facture facture){
        if (facture==null){
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
                facture.getBordereau().getProforma().getCreate_at(),
                facture.getSignedBy()
        );
    }
}
