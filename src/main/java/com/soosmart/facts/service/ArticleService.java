package com.soosmart.facts.service;

import com.soosmart.facts.dto.Article.ArticleDTO;
import com.soosmart.facts.dto.Article.SaveArticleDTO;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ArticleService {
    ArticleDTO  save_article(SaveArticleDTO articleDTO);
    CustomPageResponse<ArticleDTO> list_article(PaginatedRequest paginatedRequest);
    ArticleDTO update_article(UUID id_article, SaveArticleDTO articleDTO);
    boolean delete(UUID id);
    List<ArticleDTO> search(String search);

    CompletableFuture<List<ArticleDTO>> save_article_list(List<SaveArticleDTO> articleDTO);

    /**
     * Sauvegarde une liste d'articles de manière asynchrone par batch
     *
     * @param articlesDTO Liste des articles à sauvegarder
     * @return CompletableFuture contenant la liste des articles sauvegardés
     */
    CompletableFuture<List<ArticleDTO>> saveAllArticlesAsync(List<SaveArticleDTO> articlesDTO);

}
