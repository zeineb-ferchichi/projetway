package org.example;

import java.sql.Connection;
import java.sql.Date;
import java.util.Scanner;
import util.MyConnection;
import models.Forum;
import models.Message;
import services.ForumService;
import services.MessageService;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = MyConnection.getInstance().getCnx();

        if (connection == null) {
            System.out.println("Échec de la connexion à la base de données !");
            return;
        } else {
            System.out.println("Connexion établie avec succès !");
        }


        ForumService forumService = new ForumService();
        MessageService messageService = new MessageService();

        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Ajouter un forum");
            System.out.println("2. Mettre à jour un forum");
            System.out.println("3. Supprimer un forum");
            System.out.println("4. Afficher les forums");
            System.out.println("5. Ajouter un message");
            System.out.println("6. Mettre à jour un message");
            System.out.println("7. Supprimer un message");
            System.out.println("8. Afficher les messages");
            System.out.println("9. Quitter");
            System.out.print("Choisissez une option : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne

            switch (choix) {
                case 1:
                    System.out.print("Titre du forum : ");
                    String titre = scanner.nextLine();
                    System.out.print("Contenu du forum : ");
                    String contenu = scanner.nextLine();
                    System.out.print("URL de l'image : ");
                    String image = scanner.nextLine();

                    Forum post = new Forum(titre, contenu, image, new Date(System.currentTimeMillis()));
                    forumService.add(post);
                    System.out.println("Post ajouté avec succès !");
                    break;

                case 2:
                    System.out.print("ID du forum à mettre à jour : ");
                    int idUpdate = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nouveau titre : ");
                    String newTitre = scanner.nextLine();
                    System.out.print("Nouveau contenu : ");
                    String newContenu = scanner.nextLine();
                    System.out.print("Nouvelle image : ");
                    String newImage = scanner.nextLine();

                    Forum updatedForum = new Forum(idUpdate, newTitre, newContenu, newImage, new Date(System.currentTimeMillis()));
                    forumService.update(updatedForum);
                    System.out.println("Post mis à jour avec succès !");
                    break;

                case 3:
                    System.out.print("ID du forum à supprimer : ");
                    int idDelete = scanner.nextInt();

                    forumService.delete(new Forum(idDelete, "", "", "", null));
                    System.out.println("Post supprimé avec succès !");
                    break;

                case 4:
                    System.out.println("\nListe des forums :");
                    for (Forum forum : forumService.getAll()) {
                        System.out.println("ID: " + forum.getIdForum() + ", Titre: " + forum.getTitre() + ", Contenu: " + forum.getContenu());
                    }
                    break;

                case 5:
                    System.out.print("Contenu du message : ");
                    String msgContent = scanner.nextLine();

                    Forum lastForum = forumService.getLastForum();
                    int forumId = (lastForum != null) ? lastForum.getIdForum() : 0;

                    Message message = new Message(msgContent, new Date(System.currentTimeMillis()), forumId);
                    messageService.add(message);
                    System.out.println("Message ajouté avec succès !");
                    break;


                case 6:
                    System.out.print("ID du message à mettre à jour : ");
                    int msgUpdateId = scanner.nextInt();
                    scanner.nextLine(); // Consommer la ligne restante
                    System.out.print("Nouveau contenu  : ");

                    // Lire le contenu sans attendre qu'Entrée soit pressé à la fin
                    String updatedMsg = scanner.nextLine(); // Utilise nextLine() pour lire l'entrée complète après un mot

                    // Mise à jour du message
                    Message updatedMessage = new Message(msgUpdateId, updatedMsg, new Date(System.currentTimeMillis()), 0); // On met 0 pour l'ID du forum si pas de modification
                    messageService.update(updatedMessage);
                    System.out.println("Message mis à jour avec succès !");
                    break;

                case 7:
                    System.out.print("ID du message à supprimer : ");
                    int msgDeleteId = scanner.nextInt();

                    messageService.delete(new Message(msgDeleteId, "", null, 0));
                    System.out.println("Message supprimé avec succès !");
                    break;

                case 8:
                    System.out.println("\nListe des messages :");
                    for (Message msg : messageService.getAll()) {
                        System.out.println("ID: " + msg.getIdmessage() + ", Contenu: " + msg.getContenu() + ", ID Forum: " + (msg.getIdforum() == 0 ? "Aucun" : msg.getIdforum()));
                    }
                    break;

                case 9:
                    System.out.println("Fermeture du programme.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }
}
