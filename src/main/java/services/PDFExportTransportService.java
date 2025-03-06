package services

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import models.Transport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class PDFExportTransportService {

    public static void exportTransportsToPDF(List<Transport> transports, String filePath) {
        try {
            File file = new File(filePath);
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // 📌 Ajouter un titre au PDF
            document.add(new Paragraph("Liste des Transports\n\n"));

            // 📌 Définition des colonnes du tableau
            float[] columnWidths = {50f, 100f, 150f, 150f};
            Table table = new Table(columnWidths);

            // 📌 Ajout des en-têtes de colonnes
            table.addCell("ID");
            table.addCell("Type");
            table.addCell("Nom Station");
            table.addCell("Zone Géographique");

            // 📌 Remplissage des données
            for (Transport t : transports) {
                table.addCell(String.valueOf(t.getId_transp()));
                table.addCell(t.getType_transp());
                table.addCell(t.getNom_station());
                table.addCell(t.getZone_geographique());
            }

            document.add(table);
            document.close();

            System.out.println("📄 PDF des transports généré avec succès : " + filePath);

        } catch (FileNotFoundException e) {
            System.err.println("❌ Erreur : Impossible de créer le fichier PDF !");
            e.printStackTrace();
        }
    }
}
