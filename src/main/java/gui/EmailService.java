package gui;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.List;
import java.util.Properties;
import entities.trajet;

public class EmailService {

    private static final String SENDER_EMAIL = "mouhamarzoukk70@gmail.com"; // Remplace par ton email
    private static final String SENDER_PASSWORD = "yuha qqwo qoun yvrq"; // Remplace par ton mot de passe

    public static void envoyerEmailEmploye(String email, String nomEmploye, List<trajet> trajets) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Détails de votre voyage");

            // Construction du contenu de l'email
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Bonjour ").append(nomEmploye).append(",\n\n");
            emailContent.append("Voici les détails de votre voyage :\n\n");

            for (trajet t : trajets) {
                emailContent.append("➤ Départ : ").append(t.getVille_depart()).append("\n");
                emailContent.append("➤ Arrivée : ").append(t.getVille_arrivee()).append("\n");
                emailContent.append("➤ Transport : ").append(t.getType_transport()).append("\n");
                emailContent.append("--------------------------\n");
            }

            emailContent.append("\nBon voyage !");

            message.setText(emailContent.toString());

            Transport.send(message);
            System.out.println("E-mail envoyé avec succès à " + email);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Erreur d'envoi de l'email à " + email);
        }
    }
}
