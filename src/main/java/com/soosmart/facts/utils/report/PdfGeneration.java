package com.soosmart.facts.utils.report;


import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfGeneration {

    public byte[] generatePdfFromHtml(String html, String filename) {

        String directory = switch (filename.substring(0, 2)) {
            case "FP" -> "proforma";
            case "BL" -> "bordereau";
            case "FA" -> "facture";
            default -> throw new IllegalArgumentException("Type de Document non reconnu");
        };

        try {
            Path path = Paths.get("src/main/resources/pdf/" + directory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String outputFolder = path + File.separator + filename + ".pdf";
            OutputStream outputStreamfile = new FileOutputStream(outputFolder);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.createPDF(outputStreamfile);


            outputStream.close();
            outputStreamfile.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
