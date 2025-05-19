package com.soosmart.facts.service;

import com.soosmart.facts.dto.Article.ArticleDTO;
import com.soosmart.facts.dto.Article.SaveArticleDTO;

import java.util.UUID;

import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    ArticleDTO  save_article(SaveArticleDTO articleDTO);
    CustomPageResponse<ArticleDTO> list_article(PaginatedRequest paginatedRequest);
    ArticleDTO update_article(UUID id_article, SaveArticleDTO articleDTO);
    boolean delete(UUID id);
}
