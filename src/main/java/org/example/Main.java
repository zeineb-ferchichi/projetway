package org.example;

import Entitie.Notedefrait;
import Entitie.User;
import Service.NotedefraitService;
import Service.UserService;
import util.DataSource;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection connection = DataSource.getInstance().getConnection();
        if (connection != null) {
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("Échec de la connexion !");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();
        NotedefraitService notedefraitService = new NotedefraitService();

        while (true) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Gérer les utilisateurs");
            System.out.println("2. Gérer les notes de frais");
            System.out.println("3. Quitter");
            System.out.print("Choix : ");
            int choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    gererUtilisateurs(userService, scanner);
                    break;
                case 2:
                    gererNotedefrait(notedefraitService, scanner);
                    break;
                case 3:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Choix invalide, réessayez.");
            }
        }
    }

    private static void gererUtilisateurs(UserService userService, Scanner scanner) {
        while (true) {
            System.out.println("\n--- Gestion des Utilisateurs ---");
            System.out.println("1. Ajouter un utilisateur");
            System.out.println("2. Mettre à jour un utilisateur");
            System.out.println("3. Supprimer un utilisateur");
            System.out.println("4. Afficher tous les utilisateurs");
            System.out.println("5. Retour au menu principal");
            System.out.print("Choix : ");
            int choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    scanner.nextLine(); // Consommer le saut de ligne
                    System.out.print("Nom : ");
                    String nom = scanner.nextLine();
                    System.out.print("Prénom : ");
                    String prenom = scanner.nextLine();
                    System.out.print("Email : ");
                    String email = scanner.nextLine();
                    System.out.print("Identifiant : ");
                    String identifiant = scanner.nextLine();
                    System.out.print("Rôle (directeur/employee) : ");
                    String role = scanner.nextLine();
                    System.out.print("Mot de passe : ");
                    String motDePasse = scanner.nextLine();

                    User user = new User(0, nom, prenom, email, identifiant, role, motDePasse);

                    // Vérification avant insertion
                    if (userService.validateUser(user)) {
                        userService.insert(user);
                        System.out.println("Utilisateur ajouté avec succès !");
                    } else {
                        System.out.println("Échec de l'ajout de l'utilisateur. Vérifiez les erreurs !");
                    }
                    break;


                case 2:
                    System.out.print("ID de l'utilisateur à modifier : ");
                    int idUpdate = scanner.nextInt();
                    scanner.nextLine();

                    User userToUpdate = userService.getById(idUpdate);

                    if (userToUpdate != null) {
                        System.out.print("Nouveau nom : ");
                        userToUpdate.setNom(scanner.nextLine());

                        System.out.print("Nouveau prénom : ");
                        userToUpdate.setPrenom(scanner.nextLine());

                        System.out.print("Nouvelle adresse Gmail : ");
                        userToUpdate.setGmail(scanner.nextLine());

                        System.out.print("Nouvel identifiant : ");
                        userToUpdate.setIdentifiant(scanner.nextLine());

                        System.out.print("Nouveau rôle (directeur/employee) : ");
                        userToUpdate.setRole(scanner.nextLine());

                        System.out.print("Nouveau mot de passe : ");
                        userToUpdate.setMotdepasse(scanner.nextLine());

                    } else {
                        System.out.println(" Utilisateur non trouvé.");
                    }

                    break;
                case 3:
                    System.out.print("ID de l'utilisateur à supprimer : ");
                    int idDelete = scanner.nextInt();
                    userService.deleteById(idDelete);
                    break;
                case 4:
                    afficherUsers(userService.getAll());
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Choix invalide, réessayez.");
            }
        }
    }

    private static void gererNotedefrait(NotedefraitService notedefraitService, Scanner scanner) {
        while (true) {
            System.out.println("\n--- Gestion des Notes de Frais ---");
            System.out.println("1. Ajouter une note de frais");
            System.out.println("2. Mettre à jour une note de frais");
            System.out.println("3. Supprimer une note de frais");
            System.out.println("4. Afficher toutes les notes de frais");
            System.out.println("5. Retour au menu principal");
            System.out.print("Choix : ");
            int choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    scanner.nextLine();
                    System.out.print("Nom de l'activité : ");
                    String nomActivite = scanner.nextLine();
                    System.out.print("Description : ");
                    String description = scanner.nextLine();
                    System.out.print("Lien de la facture : ");
                    String lienFacture = scanner.nextLine();

                    Notedefrait notedefrait = new Notedefrait(0, nomActivite, description, lienFacture);

                    // Vérification avant insertion
                    if (notedefraitService.validateNotedefrait(notedefrait)) {
                        notedefraitService.insert(notedefrait);
                        System.out.println("Note de frais ajoutée avec succès !");
                    } else {
                        System.out.println("Échec de l'ajout de la note de frais. Vérifiez les erreurs !");
                    }
                    break;

                case 2:
                    System.out.print("ID de la note de frais à modifier : ");
                    int idUpdate = scanner.nextInt();
                    scanner.nextLine();

                    Notedefrait notedefraitToUpdate = notedefraitService.getById(idUpdate);

                    if (notedefraitToUpdate != null) {
                        System.out.print("Nouveau nom de l'activité : ");
                        notedefraitToUpdate.setNomactivite(scanner.nextLine());

                        System.out.print("Nouvelle description : ");
                        notedefraitToUpdate.setDescription(scanner.nextLine());

                        System.out.print("Nouveau lien de facture : ");
                        notedefraitToUpdate.setLienfacture(scanner.nextLine());

                    } else {
                        System.out.println(" Note de frais non trouvée.");
                    }
    

                    break;
                case 3:
                    System.out.print("ID de la note de frais à supprimer : ");
                    int idDelete = scanner.nextInt();
                    notedefraitService.deleteById(idDelete);
                    break;
                case 4:
                    afficherNotedefraits(notedefraitService.getAll());
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Choix invalide, réessayez.");
            }
        }
    }

    private static void afficherUsers(List<User> users) {
        for (User u : users) {
            System.out.println(u);
        }
    }

    private static void afficherNotedefraits(List<Notedefrait> notedefraits) {
        for (Notedefrait n : notedefraits) {
            System.out.println(n);
        }
    }
}
