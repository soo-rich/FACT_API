package com.soosmart.facts.dto.report;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ArticleQuantiteReportDTO {

    private String article;
    private Integer quantite;
    @Setter
    private Integer prix_article;

    public ArticleQuantiteReportDTO() {
        super();
    }

    public ArticleQuantiteReportDTO(String article, Integer quantite, Integer prix_article) {
        super();
        this.article = article;
        this.quantite = quantite;
        this.prix_article = prix_article;
    }

    public Integer getMontant() {
        return  this.prix_article * this.quantite;
    }

}
