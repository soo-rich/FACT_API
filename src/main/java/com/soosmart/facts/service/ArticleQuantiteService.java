package com.soosmart.facts.service;

import com.soosmart.facts.dto.articleQuantite.SaveArticleQuantiteDTO;
import com.soosmart.facts.entity.ArticleQuantite;

import java.util.List;
import java.util.UUID;

public interface ArticleQuantiteService {
    List<ArticleQuantite> saveAllArticleQuantitelist (List<SaveArticleQuantiteDTO> articleQuantiteDTOS);
    void updateArticleQuantite(UUID uuid, SaveArticleQuantiteDTO saveArticleQuantiteDTO);
    void deleteArticleQuantiteList();
    void getArticleQuantitesList();
}
