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

    @GetMapping
    public ResponseEntity<CustomPageResponse<ArticleDTO>> getall(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pagesize", defaultValue = "10") int pagesize,
            @RequestParam(value = "search", defaultValue = "") String search) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.articleService.list_article(new PaginatedRequest(page, pagesize, search)));
    }

    @GetMapping("all")
    public ResponseEntity<List<ArticleDTO>> getallwithoutpage() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.articleService.list_article());
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
