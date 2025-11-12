package com.soosmart.facts.Implement.report;

import com.itextpdf.html2pdf.HtmlConverter;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.soosmart.facts.dto.report.DocumentReportDTO;
import com.soosmart.facts.dto.articleQuantite.ArticleQuantiteDTO;
import com.soosmart.facts.entity.dossier.Bordereau;
import com.soosmart.facts.entity.dossier.Facture;
import com.soosmart.facts.entity.dossier.Proforma;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.dossier.BorderauDao;
import com.soosmart.facts.repository.dossier.FactureDao;
import com.soosmart.facts.repository.dossier.ProformaDao;
import com.soosmart.facts.utils.NumberToWords;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Service
public class PdfDocumentService {

    @Value("${pdf.logo.path:static/image/logo.png}")
    private String logoPath;

    @Value("${pdf.watermark.path:static/image/identity.png}")
    private String watermarkPath;

    private static final DecimalFormat PRICE_FORMAT;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator(' ');
        PRICE_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    //    private static final DeviceRgb JAUNE_CLAIR = new DeviceRgb(255, 255, 200);
    private static final DeviceRgb GRIS_ENTETE = new DeviceRgb(200, 200, 200);
    private static final DeviceRgb TRANSPARENT = new DeviceRgb(255, 255, 255);
    private final ResponseMapper responseMapper;
    private final ProformaDao proformaDao;
    private final BorderauDao borderauDao;
    private final FactureDao factureDao;

    public PdfDocumentService(ResponseMapper responseMapper, ProformaDao proformaDao, BorderauDao borderauDao, FactureDao factureDao) {
        this.responseMapper = responseMapper;
        this.proformaDao = proformaDao;
        this.borderauDao = borderauDao;
        this.factureDao = factureDao;
    }

    public byte[] generateReport(String numero) throws IOException {
        String type = numero.substring(0, 2);
        return switch (type) {
            case "FP" -> {
                Proforma proforma = this.proformaDao.findByNumero(numero).stream().findFirst().get();
                yield this.genererFactureProforma(this.responseMapper.responseDocumentReportDTO(proforma));
            }
            case "BL" -> {
                Bordereau bordereau = this.borderauDao.findByNumero(numero).stream().findFirst().get();
                yield this.genererBordereauLivraison(this.responseMapper.responseDocumentReportDTO(bordereau));
            }
            case "FA" -> {
                Facture facture = this.factureDao.findByNumero(numero).stream().findFirst().get();
                yield this.genererFacture(this.responseMapper.responseDocumentReportDTO(facture));
            }
            default -> throw new IllegalArgumentException("Type de Document non reconnu");
        };
    }

    // ==================== GÉNÉRATION DES DOCUMENTS ====================

    /**
     * Génère une facture proforma
     */
    public byte[] genererFactureProforma(DocumentReportDTO data) throws IOException {
        return genererDocument(data, "FACTURE PROFORMA", true, true);
    }

    /**
     * Génère un bordereau de livraison
     */
    public byte[] genererBordereauLivraison(DocumentReportDTO data) throws IOException {
        return genererDocument(data, "BORDEREAU DE LIVRAISON", false, false);
    }

    /**
     * Génère une facture simple
     */
    public byte[] genererFacture(DocumentReportDTO data) throws IOException {
        return genererDocument(data, "FACTURE", true, true);
    }

    /**
     * Méthode générique pour générer un document
     */
    private byte[] genererDocument(DocumentReportDTO data, String titre,
                                   boolean avecMontants, boolean avecConditions) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Marges
        document.setMargins(40, 40, 40, 40);

        // Ajouter filigrane sur toutes les pages
        ajouterFiligraneMultiPages(pdfDoc);

        // Construire le document
        ajouterLogo(document);
        ajouterClientADroite(document, data.client());
        document.add(new Paragraph("\n"));

        ajouterTitre(document, titre);
        ajouterTableauReference(document, data);
        ajouterTableauArticles(document, data, avecMontants);

        if (avecMontants) {
            ajouterTotaux(document, data);
            ajouterMontantEnLettres(document, data.total_ttc());
        }

        if (avecConditions) {
            ajouterConditionsStandards(document);
        }

        ajouterSignature(document, data.signby(), data.role());
        ajouterPiedDePage(document);

        document.close();
        return baos.toByteArray();
    }

    // ==================== COMPOSANTS RÉUTILISABLES ====================

    /**
     * Ajoute le logo en haut à gauche
     */
    public void ajouterLogo(Document document) throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource(logoPath);
            ImageData imageData = ImageDataFactory.create(resource.getURL());
            Image logo = new Image(imageData);
            logo.setWidth(150);
            logo.setHeight(60);
            document.add(logo);
        } catch (Exception e) {
            // Fallback : texte si le logo n'est pas trouvé
            Paragraph fallback = new Paragraph("SOOSMART")
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(new DeviceRgb(0, 120, 200));
            document.add(fallback);

            Paragraph group = new Paragraph("Group")
                    .setFontSize(20)
                    .setFontColor(new DeviceRgb(100, 200, 50))
                    .setMarginTop(-10);
            document.add(group);
        }
    }

    /**
     * Ajoute les informations client à droite
     */
    public void ajouterClientADroite(Document document, com.soosmart.facts.dto.client.ClientDTO client) {
        if (client == null) return;
        Paragraph clientInfo = new Paragraph();
        clientInfo.add(new Text("CLIENT :").setBold().setFontSize(10).setUnderline());
        clientInfo.add("\n");

        if (client.nom() != null) {
            clientInfo.add(new Text(client.nom()).setFontSize(10));
            if (client.sigle() != null) {
                clientInfo.add(" (" + client.sigle() + ")");
            }
            clientInfo.add("\n");
        }

        if (client.lieu() != null) {
            clientInfo.add(client.lieu() + "\n");
        }

        Table container = new Table(new float[]{100f, 80f, 100f});
        container.setWidth(UnitValue.createPercentValue(100));
        container.addCell(new Cell().setBorder(null));
        container.addCell(new Cell().setBorder(null));
        Cell rightCell = new Cell().add(clientInfo).setBorder(null).setPaddingLeft(10).setTextAlignment(TextAlignment.LEFT);
        container.addCell(rightCell);

        document.add(container);
    }

    /**
     * Ajoute le titre du document
     */
    public void ajouterTitre(Document document, String titre) {
        Paragraph titreParagraph = new Paragraph(titre)
                .setFontSize(13)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginTop(15)
                .setMarginBottom(8);
        document.add(titreParagraph);
    }

    /**
     * Ajoute le tableau de référence (Numéro, Date, Références)
     */
    public void ajouterTableauReference(Document document, DocumentReportDTO data) {
        Table refTable = new Table(new float[]{1, 1, 4});
        refTable.setWidth(UnitValue.createPercentValue(75));

        // En-têtes
        refTable.addHeaderCell(creerCelluleEntete(new Paragraph("Numéro").setFontSize(9)));
        refTable.addHeaderCell(creerCelluleEntete(new Paragraph("Date").setFontSize(9)));
        refTable.addHeaderCell(creerCelluleEntete(new Paragraph("Références").setFontSize(9)));

        // Données
        refTable.addCell(creerCellule(data.numero() != null ? data.numero() : "", TextAlignment.CENTER));
        refTable.addCell(creerCellule(data.date() != null ? DATE_FORMAT.format(data.date()) : "", TextAlignment.CENTER));

        Cell refCell = creerCellule(data.reference() != null ? data.reference() : "", TextAlignment.CENTER);
        refCell.setFontColor(ColorConstants.RED).setBold();
        refTable.addCell(refCell);

        document.add(refTable);
        document.add(new Paragraph("\n"));
    }

    /**
     * Ajoute le tableau des articles
     */
    public void ajouterTableauArticles(Document document, DocumentReportDTO data, boolean avecMontants) {
        float[] columnWidths = avecMontants ?
                new float[]{0.8f, 6, 1.5f, 1, 2} :
                new float[]{0.8f, 6, 1.5f, 1.5f, 2};

        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

        // En-têtes
        table.addHeaderCell(creerCelluleEntete(new Paragraph("Réf.").setFontSize(9)));
        table.addHeaderCell(creerCelluleEntete(new Paragraph("Désignation").setFontSize(9)));

        if (avecMontants) {
            table.addHeaderCell(creerCelluleEntete(new Paragraph("PU").setFontSize(9)));
            table.addHeaderCell(creerCelluleEntete(new Paragraph("Qté").setFontSize(9)));
            table.addHeaderCell(creerCelluleEntete(new Paragraph("Montant").setFontSize(9)));
        } else {
            table.addHeaderCell(creerCelluleEntete(new Paragraph("Qté\nCommandée").setFontSize(9)));
            table.addHeaderCell(creerCelluleEntete(new Paragraph("Qté\nLivrée").setFontSize(9)));
            table.addHeaderCell(creerCelluleEntete(new Paragraph("Observations").setFontSize(9)));
        }

        // Lignes avec alternance de couleur

        int index = 1;

        for (ArticleQuantiteDTO article : data.articleQuantiteslist()) {

            table.addCell(creerCellule(String.valueOf(index))
                    .setTextAlignment(TextAlignment.CENTER));

            // Désignation avec description
            Paragraph designation = new Paragraph();
            if (article.article() != null) {
                designation.add(new Text(article.article()).setBold());
            }
            if (article.description() != null && !article.description().isEmpty()) {
//                designation.add("\n" + article.description());
                try {
                    // Convertir le HTML en éléments iText
//                    List<IElement> elements = HtmlConverter.convertToElements(article.description());

                    System.out.println("1");
                    // Créer un conteneur pour les éléments
                    Div container = new Div();
                    System.out.println(2);
//                    for (IElement element : elements) {
//                        container.add((IBlockElement) element);
//                    }
                    System.out.println("fin");

                    designation.add(container);
                } catch (Exception e) {
                    // Fallback
                    System.out.println("caath");
//                    designation.add("\n" + cleanHtmlText(article.description()));
                }
            }


            table.addCell(creerCellule("").add(designation));

            if (avecMontants) {
                table.addCell(creerCellule(formatPrice(article.prix_article()))
                        .setTextAlignment(TextAlignment.RIGHT));
                table.addCell(creerCellule(String.valueOf(article.quantite()))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(creerCellule(
                        formatPrice(article.prix_article() * article.quantite()))
                        .setTextAlignment(TextAlignment.RIGHT));
            } else {
                table.addCell(creerCellule(String.valueOf(article.quantite()))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(creerCellule(""));
                table.addCell(creerCellule(""));
            }

            index++;
        }

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    /**
     * Ajoute les totaux (HT, TVA, TTC)
     */
    public void ajouterTotaux(Document document, DocumentReportDTO data) {
        Table totauxTable = new Table(new float[]{7, 1, 2.5f});
        totauxTable.setWidth(UnitValue.createPercentValue(50)).setHorizontalAlignment(HorizontalAlignment.RIGHT);

        // Total HT
        totauxTable.addCell(creerCelluleTotaux("Total HT", GRIS_ENTETE, TextAlignment.LEFT));
        totauxTable.addCell(creerCelluleTotaux("-", GRIS_ENTETE, TextAlignment.CENTER));
        totauxTable.addCell(creerCelluleTotaux(formatPrice(data.total_ht()), GRIS_ENTETE, TextAlignment.RIGHT));

        // TVA

        totauxTable.addCell(creerCelluleTotaux("TVA", TRANSPARENT, TextAlignment.LEFT));
        totauxTable.addCell(creerCelluleTotaux("18%", TRANSPARENT, TextAlignment.CENTER));
        totauxTable.addCell(creerCelluleTotaux(formatPrice(data.tva()), TRANSPARENT, TextAlignment.RIGHT));

        // Total TTC
        totauxTable.addCell(creerCelluleTotaux("Total TTC", GRIS_ENTETE, TextAlignment.LEFT).setBold());
        totauxTable.addCell(creerCelluleTotaux("-", GRIS_ENTETE, TextAlignment.CENTER));
        totauxTable.addCell(creerCelluleTotaux(formatPrice(data.total_ttc()), GRIS_ENTETE, TextAlignment.RIGHT).setBold());

        document.add(totauxTable);
        document.add(new Paragraph("\n"));
    }

    /**
     * Ajoute le montant en lettres
     */
    public void ajouterMontantEnLettres(Document document, Double montant) {
        if (montant == null) return;

        String montantEnLettres = NumberToWords.convertNumberToWords((double) Math.round(montant));
        Paragraph montantParagraph = new Paragraph(
                "Arrêté la présente facture à la somme de " + montantEnLettres + " francs CFA"
        ).setFontSize(10).setItalic();

        document.add(montantParagraph);
        document.add(new Paragraph("\n"));
    }

    /**
     * Ajoute les conditions standards
     */
    public void ajouterConditionsStandards(Document document) {
        Paragraph titre = new Paragraph("Termes et conditions standards:")
                .setFontSize(9)
                .setBold();
        document.add(titre);

        document.add(new Paragraph("Payement : 60% à la commande").setFontSize(9));
        document.add(new Paragraph("           40% à la livraison").setFontSize(9));
        document.add(new Paragraph("Délai de livraison : 2 semaines").setFontSize(9));
        document.add(new Paragraph("Validité de l'offre : 90 jours").setFontSize(9));
        document.add(new Paragraph("\n"));
    }

    /**
     * Ajoute la signature
     */
    public void ajouterSignature(Document document, String nom, String role) {
        if (nom == null) nom = "SOO Nabédé Akiesso";
        if (role == null) role = "Directeur";

        Paragraph signature = new Paragraph()
                .add(new Text(nom + "\n").setBold())
                .add(new Text(role).setItalic())
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(10);

        document.add(signature);
        document.add(new Paragraph("\n"));
    }

    /**
     * Ajoute le pied de page
     */
    public void ajouterPiedDePage(Document document) {
        Paragraph pied = new Paragraph(
                "RCCM : XXXXXXXXXXXXX | IFU XXXXXXXXX SCIN LE MARCHÉ OFFICIEL N°77B888 - LOMÉ\n" +
                        "T: +228 XX XX XX XX | CONTACT@SOSMART.GROUP | WWW.SOSMART.GROUP\n" +
                        "RC: TGLON 2020 X XXXX | NIF: XXXXXXXXX"
        )
                .setFontSize(7)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY)
                .setFixedPosition(40, 20, 515);

        document.add(pied);
    }

    /**
     * Ajoute un filigrane sur toutes les pages
     */
    public void ajouterFiligraneMultiPages(PdfDocument pdfDoc) {
        try {
            ClassPathResource resource = new ClassPathResource(watermarkPath);
            ImageData imageData = ImageDataFactory.create(resource.getURL());

            pdfDoc.addEventHandler(com.itextpdf.kernel.events.PdfDocumentEvent.END_PAGE,
                    new com.itextpdf.kernel.events.IEventHandler() {
                        @Override
                        public void handleEvent(com.itextpdf.kernel.events.Event event) {
                            com.itextpdf.kernel.events.PdfDocumentEvent docEvent =
                                    (com.itextpdf.kernel.events.PdfDocumentEvent) event;
                            PdfCanvas canvas = new PdfCanvas(
                                    docEvent.getPage().newContentStreamBefore(),
                                    docEvent.getPage().getResources(),
                                    docEvent.getDocument()
                            );

                            // Définir la transparence
                            PdfExtGState gs = new PdfExtGState();
                            gs.setFillOpacity(0.15f);
                            canvas.saveState();
                            canvas.setExtGState(gs);

                            // Position au centre de la page
                            float x = docEvent.getPage().getPageSize().getWidth() / 2;
                            float y = docEvent.getPage().getPageSize().getHeight() / 2;

                            try {
                                canvas.addImageAt(imageData, x - 100, y - 100, false);
                            } catch (Exception e) {
                                // Si l'image échoue, ne rien faire
                            }

                            canvas.restoreState();
                        }
                    });
        } catch (Exception e) {
            // Filigrane optionnel - continuer sans si erreur
        }
    }

    // ==================== UTILITAIRES ====================

    private Cell creerCelluleEntete(String text) {
        return new Cell().add(new Paragraph(text).setBold().setFontSize(9))
                .setBackgroundColor(GRIS_ENTETE)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(1);
    }

    private Cell creerCelluleEntete(Paragraph paragraph) {
        return new Cell().add(paragraph)
                .setBackgroundColor(GRIS_ENTETE)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(1);
    }

    private Cell creerCellule(String text) {
        return new Cell().add(new Paragraph(text).setFontSize(9))
                .setPaddingLeft(5)
                .setPadding(5);
    }

    private Cell creerCellule(String text, TextAlignment alignment) {
        return new Cell().add(new Paragraph(text).setTextAlignment(alignment).setFontSize(9))
                .setPadding(1);
    }

    private Cell creerCelluleAvecFond(String text, DeviceRgb color) {
        return new Cell().add(new Paragraph(text).setFontSize(9))
                .setBackgroundColor(color)
                .setPadding(5);
    }

    private Cell creerCelluleTotaux(String text, DeviceRgb color, TextAlignment alignment) {
        return new Cell().add(new Paragraph(text).setFontSize(10))
                .setBackgroundColor(color)
                .setTextAlignment(alignment)
                .setPaddingLeft(2)
                .setPaddingRight(2)
                .setPadding(1);
    }

    private String formatPrice(double price) {
        return PRICE_FORMAT.format(price);
    }

    private String cleanHtmlText(String html) {
        if (html == null || html.isEmpty()) return "";
        return Jsoup.parse(html).text();
    }
}