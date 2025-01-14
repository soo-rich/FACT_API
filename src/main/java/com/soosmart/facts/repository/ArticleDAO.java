package com.soosmart.facts.repository;

import com.soosmart.facts.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticleDAO extends JpaRepository<Article, UUID> {
    List<Article> findAllBySupprimerIsFalse();
}
