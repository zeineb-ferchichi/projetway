package services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import models.Abonnement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class PDFExportAbonnementService {

    public static void exportAbonnementsToPDF(List<Abonnement> abonnements, String filePath) {
        try {
            File file = new File(filePath);
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // 📌 Ajouter un titre au PDF
            document.add(new Paragraph("Liste des Abonnements\n\n"));

            // 📌 Définition des colonnes du tableau
            float[] columnWidths = {50f, 80f, 70f, 70f, 100f, 80f};
            Table table = new Table(columnWidths);

            // 📌 Ajout des en-têtes de colonnes
            table.addCell("ID");
            table.addCell("Type");
            table.addCell("Montant");
            table.addCell("Durée");
            table.addCell("Statut");
            table.addCell("Transport ID");

            // 📌 Remplissage des données
            for (Abonnement a : abonnements) {
                table.addCell(String.valueOf(a.getId_abonnem()));
                table.addCell(a.getType_abonnem());
                table.addCell(String.valueOf(a.getMontant()));
                table.addCell(String.valueOf(a.getDuree_valable()));
                table.addCell(a.getStatus_abonnem());
                table.addCell(String.valueOf(a.getTransport_id()));
            }

            document.add(table);
            document.close();

            System.out.println("📄 PDF des abonnements généré avec succès : " + filePath);

        } catch (FileNotFoundException e) {
            System.err.println("❌ Erreur : Impossible de créer le fichier PDF !");
            e.printStackTrace();
        }
    }
}
