package org.example;  // Assurez-vous que votre package est bien "org.example"

import java.sql.Connection;
import java.sql.Date;
import util.MyConnection;  // Importez MyConnection depuis le package util
import models.Forum;
import models.Message;
import services.ForumService;
import services.MessageService;

public class Main {
    public static void main(String[] args) {
        // Vérification de la connexion à la base de données
        Connection connection = MyConnection.getInstance().getCnx();

        if (connection != null) {
            System.out.println("Test réussi : connexion établie !");
        } else {
            System.out.println("Échec de la connexion !");
            return;
        }


        ForumService forumService = new ForumService();

        // Ajout d'un forum
        Forum post = new Forum("Mon premier post",
                "Ceci est le contenu de mon post.",
                "https://example.com/image.jpg",
                new Date(System.currentTimeMillis()));

        forumService.add(post);
        System.out.println("Post ajouté avec succès !");

        // Mise à jour d'un forum existant
        Forum updatedForum = new Forum(17,
                "Mon post mis à jour",
                "Voici le contenu mis à jour.",
                "https://example.com/new-image.jpg",
                new Date(System.currentTimeMillis()));

        forumService.update(updatedForum);
        System.out.println("Post mis à jour avec succès !");

        // Suppression d'un forum
        Forum forumToDelete = new Forum();
        forumToDelete.setIdForum(20);
        forumService.delete(forumToDelete);
        System.out.println("Post supprimé avec succès !");

        // Affichage des forums
        System.out.println("\nListe des forums existants :");
        for (Forum forum : forumService.getAll()) {
            System.out.println("ID: " + forum.getIdForum() + ", Titre: " + forum.getTitre() +
                    ", Contenu: " + forum.getContenu() + ", Image: " + forum.getImage() +
                    ", Date de création: " + forum.getDateCreation());
        }


        MessageService messageService = new MessageService();

        // Ajout d'un message
        Message message = new Message("Ceci est mon premier message.",
                new Date(System.currentTimeMillis()), 17);

        messageService.add(message);
        System.out.println("Message ajouté avec succès !");

        // Mise à jour d'un message existant
        Message updatedMessage = new Message(15,
                "Message mis à jour.",
                new Date(System.currentTimeMillis()),
                17);

        messageService.update(updatedMessage);
        System.out.println("Message mis à jour avec succès !");

        // Suppression d'un message
        Message messageToDelete = new Message();
        messageToDelete.setIdmessage(0);
        messageService.delete(messageToDelete);
        System.out.println("Message supprimé avec succès !");

        // Affichage des messages
        System.out.println("\nListe des messages existants :");
        for (Message msg : messageService.getAll()) {
            System.out.println("ID: " + msg.getIdmessage() + ", Contenu: " + msg.getContenu() + ", Date d'envoi: " + msg.getDateEnvoi());
        }
    }
}
