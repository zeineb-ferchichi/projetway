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
            scanner.nextLine(); // Consommer la ligne restante

            switch (choix) {
                case 1:
                    // Ajouter un forum
                    System.out.print("Titre du forum : ");
                    String titre = scanner.nextLine();
                    System.out.print("Contenu du forum : ");
                    String contenu = scanner.nextLine();
                    System.out.print("URL de l'image : ");
                    String image = scanner.nextLine();

                    Forum post = new Forum(titre, contenu, image, new Date(System.currentTimeMillis()));
                    forumService.add(post);
                    System.out.println("Forum ajouté avec succès !");
                    break;

                case 2:
                    // Mettre à jour un forum
                    System.out.print("ID du forum à mettre à jour : ");
                    int idUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consommer la ligne restante

                    if (forumService.getById(idUpdate) != null) {
                        System.out.print("Nouveau titre : ");
                        String newTitre = scanner.nextLine();
                        System.out.print("Nouveau contenu : ");
                        String newContenu = scanner.nextLine();
                        System.out.print("Nouvelle image : ");
                        String newImage = scanner.nextLine();

                        Forum updatedForum = new Forum(idUpdate, newTitre, newContenu, newImage, new Date(System.currentTimeMillis()));
                        forumService.update(updatedForum);
                        System.out.println("Forum mis à jour avec succès !");
                    } else {
                        System.out.println("Forum avec cet ID non trouvé.");
                    }
                    break;

                case 3:
                    // Supprimer un forum
                    System.out.print("ID du forum à supprimer : ");
                    int idDelete = scanner.nextInt();

                    if (forumService.getById(idDelete) != null) {
                        forumService.delete(new Forum(idDelete, "", "", "", null));
                        System.out.println("Forum supprimé avec succès !");
                    } else {
                        System.out.println("Forum avec cet ID non trouvé.");
                    }
                    break;

                case 4:
                    // Afficher les forums
                    System.out.println("\nListe des forums :");
                    for (Forum forum : forumService.getAll()) {
                        System.out.println("ID: " + forum.getIdForum() + ", Titre: " + forum.getTitre() + ", Contenu: " + forum.getContenu());
                    }
                    break;

                case 5:
                    // Ajouter un message
                    System.out.print("Contenu du message : ");
                    String msgContent = scanner.nextLine();

                    // Affichage des forums disponibles pour sélectionner un forum
                    System.out.println("Liste des forums disponibles :");
                    for (Forum forum : forumService.getAll()) {
                        System.out.println("ID: " + forum.getIdForum() + ", Titre: " + forum.getTitre());
                    }
                    System.out.print("Entrez l'ID du forum auquel vous souhaitez ajouter un message : ");
                    int forumId = scanner.nextInt();
                    scanner.nextLine(); // Consommer la ligne restante

                    if (forumService.getById(forumId) != null) {
                        Message message = new Message(msgContent, new Date(System.currentTimeMillis()), forumId);
                        messageService.add(message);
                        System.out.println("Message ajouté avec succès !");
                    } else {
                        System.out.println("Forum avec cet ID non trouvé.");
                    }
                    break;

                case 6:
                    // Mettre à jour un message
                    System.out.print("ID du message à mettre à jour : ");
                    int msgUpdateId = scanner.nextInt();
                    scanner.nextLine(); // Consommer la ligne restante

                    Message messageToUpdate = messageService.getById(msgUpdateId);
                    if (messageToUpdate != null) {
                        System.out.print("Nouveau contenu : ");
                        String updatedMsgContent = scanner.nextLine();

                        // Récupérer l'ID du forum déjà associé au message (ne pas demander à l'utilisateur)
                        int existingForumId = messageToUpdate.getIdforum();

                        // Mettre à jour le message avec le nouveau contenu, sans modifier le forum
                        Message updatedMessage = new Message(msgUpdateId, updatedMsgContent, new Date(System.currentTimeMillis()), existingForumId);
                        messageService.update(updatedMessage);
                        System.out.println("Message mis à jour avec succès !");
                    } else {
                        System.out.println("Message avec cet ID non trouvé.");
                    }
                    break;


                case 7:
                    // Supprimer un message
                    System.out.print("ID du message à supprimer : ");
                    int msgDeleteId = scanner.nextInt();

                    if (messageService.getById(msgDeleteId) != null) {
                        messageService.delete(new Message(msgDeleteId, "", null, 0));
                        System.out.println("Message supprimé avec succès !");
                    } else {
                        System.out.println("Message avec cet ID non trouvé.");
                    }
                    break;

                case 8:
                    // Afficher les messages
                    System.out.println("\nListe des messages :");
                    for (Message msg : messageService.getAll()) {
                        System.out.println("ID: " + msg.getIdmessage() + ", Contenu: " + msg.getContenu() + ", ID Forum: " + (msg.getIdforum() == 0 ? "Aucun" : msg.getIdforum()));
                    }
                    break;

                case 9:
                    // Quitter le programme
                    System.out.println("Fermeture du programme.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }
}
