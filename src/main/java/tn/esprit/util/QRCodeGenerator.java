package tn.esprit.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils; // Importation du module javafx.embed.swing

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class QRCodeGenerator {

    public static Image generateQRCodeImage(String reservationDetails) {
        try {
            Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(reservationDetails, BarcodeFormat.QR_CODE, 250, 250, hintMap);

            // Convertir le BitMatrix en image
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            com.google.zxing.client.j2se.MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            // Retourner l'image générée
            return new Image(new ByteArrayInputStream(pngData));
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Si l'image ne peut pas être générée, renvoie null
        }
    }

    public static byte[] imageToByteArray(javafx.scene.image.Image image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // Convertir l'image JavaFX en BufferedImage
            java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            // Écrire l'image dans le flux de sortie
            javax.imageio.ImageIO.write(bufferedImage, "PNG", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}