package com.soosmart.facts.Implement;

import com.soosmart.facts.dto.Article.ArticleDTO;
import com.soosmart.facts.dto.Article.SaveArticleDTO;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.entity.Article;
import com.soosmart.facts.exceptions.EntityNotFound;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.ArticleDAO;
import com.soosmart.facts.service.ArticleService;
import com.soosmart.facts.utils.pagination.PageMapperUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;


@Service
public class ArticleImpl implements ArticleService {

    private final ArticleDAO articleDAO;
    private final ResponseMapper responseMapper;

    // Taille du batch pour optimiser les performances
    private static final int BATCH_SIZE = 100;

    public ArticleImpl(ArticleDAO articleDAO, ResponseMapper responseMapper) {
        this.articleDAO = articleDAO;
        this.responseMapper = responseMapper;
    }

    @Override
    public ArticleDTO save_article(SaveArticleDTO articleDTO) {

        Article article = Article.builder().libelle(articleDTO.libelle()).description(articleDTO.description()).prix_unitaire(articleDTO.prix_unitaire()).build();
        Article save = this.articleDAO.save(article);
        return this.responseMapper.responseArticleDTO(save);
    }

    @Async
    public CompletableFuture<ArticleDTO> save_article_asyn(SaveArticleDTO articleDTO) {
        return CompletableFuture.supplyAsync(() -> this.save_article(articleDTO));
    }


    @Override
    public CustomPageResponse<ArticleDTO> list_article(PaginatedRequest paginatedRequest) {
        return PageMapperUtils.toPageResponse(this.articleDAO.findAllBySupprimerIsFalse(PageMapperUtils.createPageableWithoutSearch(paginatedRequest)), this.responseMapper::responseArticleDTO);
    }

    @Override
    public ArticleDTO update_article(UUID id_article, SaveArticleDTO articleDTO) {
        Optional<Article> articleOld = this.articleDAO.findById(id_article).stream().findFirst();
        if (articleOld.isPresent()) {
            Article articleupdate = articleOld.get();
            articleupdate.setLibelle(articleDTO.libelle());
            articleupdate.setDescription(articleDTO.description());
            articleupdate.setPrix_unitaire(articleDTO.prix_unitaire());

            return this.responseMapper.responseArticleDTO(this.articleDAO.save(articleupdate));
        }
        throw new EntityNotFound("Article not found");
    }

    @Override
    public boolean delete(UUID id) {

        Optional<Article> article = this.articleDAO.findById(id).stream().findFirst();
        if (article.isPresent()) {
            article.get().setSupprimer(true);
            this.articleDAO.save(article.get());
            return true;
        }
        throw new EntityNotFound("Article not found");
    }

    @Override
    public List<ArticleDTO> search(String search) {

        // if (search == null || search.isEmpty()) {
        //     return this.articleDAO.findAllBySupprimerIsFalse()
        //             .stream()
        //             .map(this.responseMapper::responseArticleDTO)
        //             .limit(10)
        //             .toList();
        // }

        return this.articleDAO.findByLibelleContainingIgnoreCaseAndSupprimerIsFalse(search).stream().map(this.responseMapper::responseArticleDTO).toList();
    }


    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<List<ArticleDTO>> saveAllArticlesAsync(List<SaveArticleDTO> articlesDTO) {
        if (articlesDTO == null || articlesDTO.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }

        try {
            // Diviser la liste en batches pour optimiser les performances
            List<List<SaveArticleDTO>> batches = createBatches(articlesDTO, BATCH_SIZE);

            // Traiter chaque batch en parallèle
            List<CompletableFuture<List<ArticleDTO>>> batchFutures = batches.stream()
                    .map(this::processBatch)
                    .toList();

            // Attendre que tous les batches soient terminés et combiner les résultats
            CompletableFuture<Void> allBatches = CompletableFuture.allOf(
                    batchFutures.toArray(new CompletableFuture[0])
            );

            return allBatches.thenApply(v ->
                    batchFutures.stream()
                            .flatMap(future -> future.join().stream())
                            .toList()
            );

        } catch (Exception e) {
            CompletableFuture<List<ArticleDTO>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    /**
     * Traite un batch d'articles
     */
    private CompletableFuture<List<ArticleDTO>> processBatch(List<SaveArticleDTO> batch) {
        return CompletableFuture.supplyAsync(() -> {
            // Convertir les DTOs en entités
            List<Article> articles = batch.stream()
                    .map(dto -> Article.builder()
                            .libelle(dto.libelle())
                            .description(dto.description())
                            .prix_unitaire(dto.prix_unitaire())
                            .build())
                    .toList();

            // Sauvegarder le batch en une seule requête
            List<Article> savedArticles = this.articleDAO.saveAll(articles);

            // Convertir les entités sauvegardées en DTOs
            return savedArticles.stream()
                    .map(this.responseMapper::responseArticleDTO)
                    .toList();
        });
    }

    /**
     * Divise une liste en batches de taille donnée
     */
    private <T> List<List<T>> createBatches(List<T> list, int batchSize) {
        return IntStream.range(0, (list.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> list.subList(
                        i * batchSize,
                        Math.min((i + 1) * batchSize, list.size())
                ))
                .toList();
    }

    @Override
    @Async
    public CompletableFuture<List<ArticleDTO>> save_article_list(List<SaveArticleDTO> articleDTO) {
        List<CompletableFuture<ArticleDTO>> futures = articleDTO.stream().map(this::save_article_asyn).toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        return allOf.thenApply(v -> futures.stream().map(CompletableFuture::join).toList());
    }
}
