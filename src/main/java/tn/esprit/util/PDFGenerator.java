package tn.esprit.util;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import tn.esprit.models.Reservation;
import tn.esprit.services.HebergementService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFGenerator {

    public static void generatePDF(Reservation reservation, String filePath) {
        try {
            // Créer un PdfWriter pour écrire dans le fichier PDF
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Ajouter un titre stylisé au document
            Paragraph title = new Paragraph("Ticket de Réservation")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE);
            document.add(title);

            // Ajouter un espace
            document.add(new Paragraph("\n"));

            // Créer un tableau pour les informations de la réservation
            Table table = new Table(2);
            table.setWidth(400);
            table.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);

            // Ajouter les informations de la réservation au tableau
            addRow(table, "Nom du Client", reservation.getClientName());

            // Formatter les dates en String
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateDebut = dateFormat.format(reservation.getDateDebut());
            String dateFin = dateFormat.format(reservation.getDateFin());

            addRow(table, "Date de Début", dateDebut);
            addRow(table, "Date de Fin", dateFin);

            // Récupérer le nom de l'hébergement
            HebergementService hebergementService = new HebergementService();
            String hebergementName = hebergementService.getById(reservation.getHebergementId()).getNom();
            addRow(table, "Hébergement", hebergementName);

            document.add(table);

            // Ajouter un espace
            document.add(new Paragraph("\n"));

            // Générer et ajouter le QR code
            String reservationDetails = "Client: " + reservation.getClientName() + "\n" +
                    "Début: " + dateDebut + "\n" +
                    "Fin: " + dateFin + "\n" +
                    "Hébergement: " + hebergementName;

            byte[] qrCodeBytes = QRCodeGenerator.imageToByteArray(QRCodeGenerator.generateQRCodeImage(reservationDetails));
            Image pdfImage = new Image(com.itextpdf.io.image.ImageDataFactory.create(qrCodeBytes));
            pdfImage.setWidth(150);
            pdfImage.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
            document.add(pdfImage);

            // Ajouter un pied de page stylisé
            Paragraph footer = new Paragraph("Merci pour votre réservation !")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14)
                    .setItalic()
                    .setFontColor(ColorConstants.GRAY);
            document.add(footer);

            // Fermer le document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addRow(Table table, String label, String value) {
        Cell labelCell = new Cell().add(new Paragraph(label).setBold().setFontColor(ColorConstants.DARK_GRAY));
        Cell valueCell = new Cell().add(new Paragraph(value).setFontColor(ColorConstants.BLACK));
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}