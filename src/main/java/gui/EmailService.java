package gui;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.List;
import java.util.Properties;
import entities.trajet;

public class EmailService {

    public static void envoyerEmailEmploye(String emailEmploye, String nomEmploye, String destination) {
        // Créer un message à envoyer par email
        String subject = "Mise à jour du voyage";
        String body = "Bonjour " + nomEmploye + ",\n\n" +
                "Le voyage à destination de " + destination + " a été modifié.\n\n" +
                "Cordialement,\n" +
                "L'équipe de gestion des voyages.";

        // Code pour envoyer l'email (avec une bibliothèque d'envoi d'emails, comme JavaMail API)
        System.out.println("Envoi de l'email à " + emailEmploye);
        System.out.println("Objet: " + subject);
        System.out.println("Contenu: " + body);

        // Logique d'envoi de l'email ici (par exemple via SMTP)
    }
}
