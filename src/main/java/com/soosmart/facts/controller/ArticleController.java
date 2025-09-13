package com.soosmart.facts.controller;

import com.soosmart.facts.dto.Article.ArticleDTO;
import com.soosmart.facts.dto.Article.SaveArticleDTO;
import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "article")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleDTO> save(@RequestBody SaveArticleDTO articleDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.articleService.save_article(articleDTO));
    }

    @PostMapping(value = "all", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<List<ArticleDTO>>> saveAll(
            @RequestBody List<SaveArticleDTO> articlesDTO) {

        // Validation de base
        if (articlesDTO == null || articlesDTO.isEmpty()) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().build()
            );
        }

        // Limiter le nombre d'articles par requête pour éviter les surcharges
        if (articlesDTO.size() > 5000) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest()
                            .header("Error-Message", "Nombre maximum d'articles par requête: 5000")
                            .build()
            );
        }

        return this.articleService.saveAllArticlesAsync(articlesDTO)
                .thenApply(savedArticles ->
                        ResponseEntity.status(HttpStatus.CREATED).body(savedArticles)
                )
                .exceptionally(throwable -> {
                    // Log l'erreur
                    System.err.println("Erreur lors de la sauvegarde des articles: " + throwable.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }


    @GetMapping
    public ResponseEntity<CustomPageResponse<ArticleDTO>> getall(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pagesize", defaultValue = "10") int pagesize,
            @RequestParam(value = "search", defaultValue = "") String search) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.articleService.list_article(new PaginatedRequest(page, pagesize, search)));
    }

    @GetMapping("search")
    public ResponseEntity<List<ArticleDTO>> getall(
            @RequestParam(value = "search", defaultValue = "") String search) {

        return ResponseEntity.status(HttpStatus.OK).body(this.articleService.search(search));
    }

    @PutMapping("{id}")
    public ResponseEntity<ArticleDTO> update(@PathVariable UUID id, @RequestBody SaveArticleDTO articleDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(this.articleService.update_article(id, articleDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        this.articleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
