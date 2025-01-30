package com.soosmart.facts.dto.report;

public class ArticleQuantiteReportDTO {

    private String article;
    private Integer quantite;
    private Float prix_article;

    public ArticleQuantiteReportDTO() {
        super();
    }

    public ArticleQuantiteReportDTO(String article, Integer quantite, Float prix_article) {
        super();
        this.article = article;
        this.quantite = quantite;
        this.prix_article = prix_article;
    }

    public String getArticle() {
        return this.article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Float getPrix_article() {
        return prix_article;
    }

    public void setPrix_article(Float prix_article) {
        this.prix_article = prix_article;
    }
}
