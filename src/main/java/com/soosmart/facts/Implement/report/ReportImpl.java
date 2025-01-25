package com.soosmart.facts.Implement.report;

import com.soosmart.facts.dto.dossier.borderau.BorderauDto;
import com.soosmart.facts.dto.dossier.facture.FactureDto;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.service.dossier.BordereauService;
import com.soosmart.facts.service.dossier.FactureService;
import com.soosmart.facts.service.dossier.ProformaService;
import com.soosmart.facts.service.report.ReportService;
import com.soosmart.facts.utils.NumberToWords;
import com.soosmart.facts.utils.report.ReportUtils;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReportImpl implements ReportService {

    private final ProformaService proformaService;
    private final BordereauService bordereauService;
    private final FactureService factureService;
    private final ReportUtils reportUtils;
    private final NumberToWords numberToWords;
    private final ResponseMapper responseMapper;

    @Override
    public byte[] GenerateReport(String numero) {
//            declare du type de retour

//        recupere les deux premier caratere du nemero
        String type = numero.substring(0, 2);
        switch (type) {
            case "FP":
                Proforma proforma = this.proformaService.getProformaEntity(numero);
//               return  this.preparedataandGenerateForProforma(proforma);
                return this.text();
            case "BL":
                BorderauDto borderau = this.bordereauService.getBordereauByNumero(numero);
                return null;
            case "FA":
                FactureDto facture = this.factureService.getFacture(numero);
                System.out.println(facture);
                return null;
            default:
                throw new IllegalArgumentException("Type de Document non reconnu");
        }


    }

    @Override
    public byte[] preparedataandGenerateForProforma(Proforma proforma) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
                    .withZone(ZoneId.systemDefault());
            // Chemin vers le fichier .jasper (compilé)
            String jasperFilePath = "proforma.jasper";

            // Préparer les paramètres
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CLIENT", proforma.getClient().getNom());
            parameters.put("REFERENCE", proforma.getReference());
            parameters.put("NUMERO", proforma.getNumero());
            parameters.put("SIGNBY", proforma.getSignedBy());
            parameters.put("TOTAL_HT", proforma.getTotal_ht().toString());
            parameters.put("TOTAL_TTC", proforma.getTotal_ttc().toString());
            parameters.put("TOTAL_TVA", proforma.getTotal_tva().toString());
            parameters.put("DATE", formatter.format(proforma.getCreate_at()));
            parameters.put("LIEU", proforma.getClient().getLieu());
            parameters.put("PRICE_LETTER", this.numberToWords.convertNumberToWords(proforma.getTotal_ttc()));
            parameters.put("ROLE", "");


            // Préparer les données pour le champ articleQuantiteslist
            // Supposons que vous avez une classe ArticleQuantite avec les champs nécessaires
//            List<ArticleQuantiteReportDTO> articleQuantiteslist = proforma.getArticleQuantiteList().stream().map(responseMapper::responseArticleQuantiteDTOReporrt).toList();

            return this.reportUtils.generateReport(null, jasperFilePath, parameters);


        } catch (JRException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] preparedataandGenerateForBordeau(Bordereau bordereau) {
        return new byte[0];
    }

    @Override
    public byte[] preparedataandGenerateForFacture(Facture facture) {
        return new byte[0];
    }

    public byte[] text() {

        try {
//            File template  = ResourceUtils.getFile("classpath:templates/test/Test.jrxml");
            InputStream template = getClass().getResourceAsStream("/templates/test/Test.jrxml");
//            JasperCompileManager.compileReportToFile(template.getPath());
            JasperReport jasperReport = JasperCompileManager.compileReport(template);
            // Charge le rapport compilé (.jasper)
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(ResourceUtils.getFile("classpath:templates/test/Test.jasper").getPath());

            // Paramètres (optionnel)
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("myname", "Ulrich");

            // Remplit le rapport avec des données (ici, pas de source de données)
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\SUL04\\test.pdf");
            return byteArrayOutputStream.toByteArray();

        } catch (JRException | JRRuntimeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
