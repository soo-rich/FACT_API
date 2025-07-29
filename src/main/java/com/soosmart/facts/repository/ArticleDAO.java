package com.soosmart.facts.repository;

import com.soosmart.facts.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleDAO extends JpaRepository<Article, UUID> {
    Page<Article> findAllBySupprimerIsFalse(Pageable pageable);
    List<Article> findAllBySupprimerIsFalse();
    Streamable<Article> findByLibelleContainingIgnoreCaseAndSupprimerIsFalse(String search);
}
