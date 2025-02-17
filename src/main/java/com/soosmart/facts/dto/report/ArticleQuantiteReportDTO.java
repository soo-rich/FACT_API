package com.soosmart.facts.dto.report;

public class ArticleQuantiteReportDTO {

    private String article;
    private Integer quantite;
    private Integer prix_article;
    private Integer montant;

    public ArticleQuantiteReportDTO() {
        super();
    }

    public ArticleQuantiteReportDTO(String article, Integer quantite, Integer prix_article) {
        super();
        this.article = article;
        this.quantite = quantite;
        this.prix_article = prix_article;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getPrix_article() {
        return prix_article;
    }

    public void setPrix_article(Integer prix_article) {
        this.prix_article = prix_article;
    }

    public Integer getMontant() {
        return montant = this.prix_article * this.quantite;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }
}
