package com.soosmart.facts.dto.articleQuantite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleQuantiteReportDTO {
    String article;
    Integer quantite;
    float prix_article;
}
