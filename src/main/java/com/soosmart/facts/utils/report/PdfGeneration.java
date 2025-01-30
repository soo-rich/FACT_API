package com.soosmart.facts.utils.report;

import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class PdfGeneration {
    public static void generatePdfFromHtml(String html, String filename) throws IOException {
        String outputFolder = System.getProperty("user.home") + File.separator + filename+".pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }
}
