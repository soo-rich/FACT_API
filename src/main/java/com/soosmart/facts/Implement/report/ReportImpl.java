package com.soosmart.facts.Implement.report;

import com.soosmart.facts.dto.report.ArticleQuantiteReportDTO;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.mapper.ResponseMapper;
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

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@AllArgsConstructor
public class ReportImpl implements ReportService {

    private final ProformaService proformaService;
    private final BordereauService bordereauService;
    private final FactureService factureService;
    private final NumberToWords numberToWords;
    private final ResponseMapper responseMapper;
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

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);


        Context context = new Context();
        context.setVariable("numero", proforma.getNumero());
        context.setVariable("date", new SimpleDateFormat("dd-MM-yyyy").format(Date.from(proforma.getCreate_at())));
        context.setVariable("ref", proforma.getReference());
        context.setVariable("items", proforma.getArticleQuantiteList().stream().map(
                articleQuantite -> new ArticleQuantiteReportDTO(
                        articleQuantite.getArticle().getLibelle(),
                        articleQuantite.getQuantite(),
                        articleQuantite.getArticle().getPrix_unitaire()
                )).toList());

        try {
            String htmlContent = templateEngine.process("proforma", context);
            return this.pdfGeneration.generatePdfFromHtml(htmlContent, proforma.getNumero());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new byte[0];
    }

    @Override
    public byte[] preparedataandGenerateForBordeau(Bordereau bordereau) {
        return new byte[0];
    }

    @Override
    public byte[] preparedataandGenerateForFacture(Facture facture) {
        return new byte[0];
    }
}
