package com.soosmart.facts.Implement;

import com.soosmart.facts.dto.articleQuantite.SaveArticleQuantiteDTO;
import com.soosmart.facts.entity.ArticleQuantite;
import com.soosmart.facts.repository.ArticleDAO;
import com.soosmart.facts.repository.ArticleQuantiteDAO;
import com.soosmart.facts.service.ArticleQuantiteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ArticleQuantiteImpl implements ArticleQuantiteService {

    private final ArticleQuantiteDAO articleQuantiteRepository;
    private final ArticleDAO articleRepository;

    @Override
    public List<ArticleQuantite> saveAllArticleQuantitelist(List<SaveArticleQuantiteDTO> articleQuantiteDTOS) {
        List<ArticleQuantite> articleQuantites = new ArrayList<>();
        articleQuantiteDTOS.forEach(
                articleQuantiteDTO -> {
                    this.articleRepository.findById(articleQuantiteDTO.article_id())
                            .ifPresentOrElse(
                                    article -> {
                                        ArticleQuantite articleQuantite = this.articleQuantiteRepository.save(ArticleQuantite.builder()
                                                .article(article)
                                                .quantite(articleQuantiteDTO.quantite())
                                                .prix_article(articleQuantiteDTO.prix_article() != 0 ? articleQuantiteDTO.prix_article() : article.getPrix_unitaire())
                                                .build()
                                        );
                                        articleQuantites.add(articleQuantite);

                                    },
                                    () -> {
                                        throw new RuntimeException("Article not found");
                                    }
                            );
                }
        );
        return this.articleQuantiteRepository.saveAll(articleQuantites);
    }

    @Override
    public void updateArticleQuantite(UUID uuid, SaveArticleQuantiteDTO saveArticleQuantiteDTO) {

    }

    @Override
    public void deleteArticleQuantiteList() {

    }

    @Override
    public void getArticleQuantitesList() {

    }
}
