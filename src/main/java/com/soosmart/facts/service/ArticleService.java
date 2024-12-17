package com.soosmart.facts.service;

import com.soosmart.facts.dto.Article.ArticleDTO;
import com.soosmart.facts.dto.Article.SaveArticleDTO;

import java.util.List;
import java.util.UUID;

public interface ArticleService {
    ArticleDTO  save_article(SaveArticleDTO articleDTO);
    List<ArticleDTO> list_article();
    ArticleDTO update_article(UUID id_article, SaveArticleDTO articleDTO);
    boolean delete(UUID id);
}
