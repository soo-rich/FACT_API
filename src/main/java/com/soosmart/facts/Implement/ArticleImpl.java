package com.soosmart.facts.Implement;

import com.soosmart.facts.dto.Article.ArticleDTO;
import com.soosmart.facts.dto.Article.SaveArticleDTO;
import com.soosmart.facts.entity.Article;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.ArticleDAO;
import com.soosmart.facts.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleImpl implements ArticleService {

    private ArticleDAO articleDAO;
    private ResponseMapper responseMapper;

    @Override
    public ArticleDTO save_article(SaveArticleDTO articleDTO) {

        Article article = Article.builder().libelle(articleDTO.libelle()).prix_unitaire(articleDTO.prix()).build();
        System.out.println("save article "+articleDTO.libelle());
        Article save = this.articleDAO.save(article);
        return this.responseMapper.responseArticleDTO(save);
    }

    @Override
    public List<ArticleDTO> list_article() {
        return this.articleDAO.findAll().stream().map(this.responseMapper::responseArticleDTO).collect(Collectors.toList());
    }

    @Override
    public ArticleDTO update_article(UUID id_article, SaveArticleDTO articleDTO) {
        Optional<Article> articleOld = this.articleDAO.findById(id_article).stream().findFirst();
        if (articleOld.isPresent()) {
            Article articleupdate = articleOld.get();
            articleupdate.setLibelle(articleDTO.libelle());
            articleupdate.setPrix_unitaire(articleDTO.prix());

            return this.responseMapper.responseArticleDTO(this.articleDAO.save(articleupdate));
        }
        return null;
    }

    @Override
    public boolean delete(UUID id) {

        Optional<Article> article = this.articleDAO.findById(id).stream().findFirst();
        if (article.isPresent()) {
            this.articleDAO.delete(article.get());
            return true;
        }
        return false;

    }
}
