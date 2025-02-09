package org.example;  // Assurez-vous que votre package est bien "org.example"

import java.sql.Connection;
import java.sql.Date;
import util.MyConnection;  // Importez MyConnection depuis le package util
import models.Forum;
import services.ForumService;

public class Main {
    public static void main(String[] args) {
        // Vérifiez que MyConnection est dans le bon package
        Connection connection = MyConnection.getInstance().getCnx();  // Appel correct de getInstance() et getCnx()

        // Vérification de la connexion à la base de données
        if (connection != null) {
            System.out.println("Test réussi : connexion établie !");
        } else {
            System.out.println("Échec de la connexion !");
            return;  // Sortir si la connexion échoue
        }

        // Création d'un service Forum pour gérer les opérations sur les forums
        ForumService forumService = new ForumService();

        // Création d'un forum pour l'ajout initial
        Forum post = new Forum("Mon premier post",
                "Ceci est le contenu de mon post.",
                "https://example.com/image.jpg", // Utilisation d'un lien HTTPS sécurisé
                new Date(System.currentTimeMillis())); // Utilisation correcte de java.sql.Date

        // Ajouter le post dans la base de données
        forumService.add(post);
        System.out.println("Post ajouté avec succès !");

        // Mise à jour d'un forum existant
        // Utilisez l'ID du forum déjà ajouté pour mettre à jour
        // Supposons que l'ID du forum est 1 (vous devez le connaître dans votre base de données)
        Forum updatedForum = new Forum(2,  // Assurez-vous d'avoir l'ID du forum à mettre à jour
                "Mon post mis à jour",
                "Voici le contenu mis à jour.",
                "https://example.com/new-image.jpg", // Nouveau lien d'image
                new Date(System.currentTimeMillis())); // Nouvelle date de mise à jour

        // Mise à jour du forum
        forumService.update(updatedForum);
        System.out.println("Post mis à jour avec succès !");

        // Suppression d'un forum existant
        // Utilisez l'ID du forum à supprimer
        Forum forumToDelete = new Forum();
        forumToDelete.setId(8);// ID du forum à supprimer
        forumService.delete(forumToDelete);
        System.out.println("Post supprimé avec succès !");

        // Récupération et affichage de tous les forums
        System.out.println("\nListe des forums existants :");
        for (Forum forum : forumService.getAll()) {
            System.out.println("ID: " + forum.getId() + ", Titre: " + forum.getTitre() + ", Contenu: " + forum.getContenu() + ", Image: " + forum.getImage() + ", Date de création: " + forum.getDateCreation());
        }
    }
}
