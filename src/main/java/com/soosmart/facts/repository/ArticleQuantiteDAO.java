package com.soosmart.facts.repository;

import com.soosmart.facts.entity.ArticleQuantite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleQuantiteDAO extends JpaRepository<ArticleQuantite, UUID> {
}
