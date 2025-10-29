package com.soosmart.facts.service;

import com.soosmart.facts.dto.Article.ArticleDTO;
import com.soosmart.facts.dto.Article.SaveArticleDTO;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;

import java.util.List;
import java.util.UUID;

public interface ArticleService {
    ArticleDTO  save_article(SaveArticleDTO articleDTO);
    CustomPageResponse<ArticleDTO> list_article(PaginatedRequest paginatedRequest);
    List<ArticleDTO> list_article();
    ArticleDTO update_article(UUID id_article, SaveArticleDTO articleDTO);
    boolean delete(UUID id);
    List<ArticleDTO> search(String search);
}
