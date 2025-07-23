package com.soosmart.facts.Implement.report;

import com.soosmart.facts.dto.report.ArticleQuantiteReportDTO;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.service.dossier.BordereauService;
import com.soosmart.facts.service.dossier.FactureService;
import com.soosmart.facts.service.dossier.ProformaService;
import com.soosmart.facts.service.report.ReportService;
import com.soosmart.facts.utils.NumberToWords;
import com.soosmart.facts.utils.report.PdfGeneration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportImpl implements ReportService {

    private final ProformaService proformaService;
    private final BordereauService bordereauService;
    private final FactureService factureService;
    private final NumberToWords numberToWords;
    private final PdfGeneration pdfGeneration;

    @Override
    public byte[] GenerateReport(String numero) {
//        recuperer les 2 premiers caracteres du numero
        String type = numero.substring(0, 2);
        return switch (type) {
            case "FP" -> this.preparedataandGenerateForProforma(this.proformaService.getProformaEntity(numero));
            case "BL" -> this.preparedataandGenerateForBordeau(this.bordereauService.getBordereauEntity(numero));
            case "FA" -> this.preparedataandGenerateForFacture(this.factureService.getFactureEntity(numero));
            default -> throw new IllegalArgumentException("Type de Document non reconnu");
        };


    }

    @Override
    public byte[] preparedataandGenerateForProforma(Proforma proforma) {

        List<ArticleQuantiteReportDTO> articleQuantiteReportDTOS = proforma.getArticleQuantiteList().stream().map(
                articleQuantite -> new ArticleQuantiteReportDTO(
                        articleQuantite.getArticle().getLibelle(),
                        articleQuantite.getQuantite(),
                        articleQuantite.getPrix_article().intValue()
                )).toList();
        TemplateEngine templateEngine = getTemplateEngine();


        Context context = new Context();
        context.setVariable("numero", proforma.getNumero());
        context.setVariable("date", new SimpleDateFormat("dd-MM-yyyy").format(Date.from(proforma.getCreated_at())));
        context.setVariable("reference", proforma.getReference());

        context.setVariable("articles", articleQuantiteReportDTOS);

        context.setVariable("totalht", Math.round(proforma.getTotal_ht()));
        context.setVariable("tva", Math.round(proforma.getTotal_tva()));
        context.setVariable("totalttc", Math.round(proforma.getTotal_ttc()));
        context.setVariable("totalttcword", this.numberToWords.convertNumberToWords(Math.round(proforma.getTotal_ttc())));
        context.setVariable("sign", proforma.getSignedBy());
        context.setVariable("role", proforma.getRole());
        context.setVariable("client", proforma.getClient().getNom());
        context.setVariable("adresse", proforma.getClient().getLieu());

        try {
            String htmlContent = templateEngine.process("proforma", context);
            return this.pdfGeneration.generatePdfFromHtml(htmlContent);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new byte[0];
    }

    @Override
    public byte[] preparedataandGenerateForBordeau(Bordereau bordereau) {
        List<ArticleQuantiteReportDTO> articleQuantiteReportDTOS = bordereau.getProforma().getArticleQuantiteList().stream().map(
                articleQuantite -> new ArticleQuantiteReportDTO(
                        articleQuantite.getArticle().getLibelle(),
                        articleQuantite.getQuantite(),
                        articleQuantite.getPrix_article().intValue()
                )).toList();
        TemplateEngine templateEngine = getTemplateEngine();


        Context context = new Context();
        context.setVariable("numero", bordereau.getNumero());
        context.setVariable("date", new SimpleDateFormat("dd-MM-yyyy").format(Date.from(bordereau.getCreated_at())));
        context.setVariable("reference", bordereau.getReference());

        context.setVariable("articles", articleQuantiteReportDTOS);

        context.setVariable("client", bordereau.getProforma().getClient().getNom());
        context.setVariable("adresse", bordereau.getProforma().getClient().getLieu());

        try {
            String htmlContent = templateEngine.process("bordereau", context);
            return this.pdfGeneration.generatePdfFromHtml(htmlContent);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new byte[0];
    }


    @Override
    public byte[] preparedataandGenerateForFacture(Facture facture) {
        List<ArticleQuantiteReportDTO> articleQuantiteReportDTOS = facture.getBordereau().getProforma().getArticleQuantiteList().stream().map(
                articleQuantite -> new ArticleQuantiteReportDTO(
                        articleQuantite.getArticle().getLibelle(),
                        articleQuantite.getQuantite(),
                        articleQuantite.getPrix_article().intValue()
                )).toList();
        TemplateEngine templateEngine = getTemplateEngine();


        Context context = new Context();
        context.setVariable("numero", facture.getNumero());
        context.setVariable("date", new SimpleDateFormat("dd-MM-yyyy").format(Date.from(facture.getCreated_at())));
        context.setVariable("reference", facture.getReference());

        context.setVariable("articles", articleQuantiteReportDTOS);

        context.setVariable("totalht", Math.round(facture.getBordereau().getProforma().getTotal_ht()));
        context.setVariable("tva",Math.round( facture.getBordereau().getProforma().getTotal_tva()));
        context.setVariable("totalttc", Math.round(facture.getBordereau().getProforma().getTotal_ttc()));
        context.setVariable("totalttcword", this.numberToWords.convertNumberToWords(Math.round(facture.getBordereau().getProforma().getTotal_ttc())));
        context.setVariable("sign", facture.getSignedBy());
        context.setVariable("role", facture.getRole());
        context.setVariable("client", facture.getBordereau().getProforma().getClient().getNom());
        context.setVariable("adresse", facture.getBordereau().getProforma().getClient().getLieu());

        try {
            String htmlContent = templateEngine.process("facture", context);
            return this.pdfGeneration.generatePdfFromHtml(htmlContent);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new byte[0];
    }

    @Override
    public byte[] DownloadReport(String numero) {
        return this.pdfGeneration.downloadDossier(numero);
    }

    private static TemplateEngine getTemplateEngine() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        templateResolver.setOrder(1);
        templateResolver.setCheckExistence(true);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}
