package com.soosmart.facts.mapper;

import com.soosmart.facts.dto.Article.ArticleDTO;
import com.soosmart.facts.dto.client.ClientDTO;
import com.soosmart.facts.dto.project.ProjetDTO;
import com.soosmart.facts.dto.user.ResponseUtilisateur;
import com.soosmart.facts.entity.Article;
import com.soosmart.facts.entity.Client;
import com.soosmart.facts.entity.Projet;
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
                utilisateur.getCreatedAt()
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
}
