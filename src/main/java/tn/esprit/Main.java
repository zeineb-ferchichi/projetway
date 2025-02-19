/*package tn.esprit;

import tn.esprit.models.Hebergement;
import tn.esprit.models.Reservation;
import tn.esprit.services.HebergementService;
import tn.esprit.services.IService;
import tn.esprit.services.ReservationService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        IService<Hebergement> hebergementService = new HebergementService();
        ReservationService reservationService = new ReservationService();
        Scanner scanner = new Scanner(System.in);
        int choix;

        do {
            System.out.println("\n===== MENU GESTION HÉBERGEMENT & RÉSERVATION =====");
            System.out.println("1. Ajouter un hébergement");
            System.out.println("2. Modifier un hébergement");
            System.out.println("3. Supprimer un hébergement");
            System.out.println("4. Afficher tous les hébergements");
            System.out.println("5. Ajouter une réservation");
            System.out.println("6. Modifier une réservation");
            System.out.println("7. Supprimer une réservation");
            System.out.println("8. Afficher toutes les réservations");
            System.out.println("0. Quitter");
            System.out.print("Choisissez une option: ");

            choix = scanner.nextInt();
            scanner.nextLine(); // Évite les problèmes avec nextInt()

            switch (choix) {
                case 1:
                    // Ajout d'un hébergement
                    System.out.print("Nom: ");
                    String nom = scanner.nextLine();
                    System.out.print("Type: ");
                    String type = scanner.nextLine();
                    System.out.print("Adresse: ");
                    String adresse = scanner.nextLine();
                    System.out.print("Ville: ");
                    String ville = scanner.nextLine();
                    System.out.print("Pays: ");
                    String pays = scanner.nextLine();
                    System.out.print("Capacité: ");
                    int capacite = scanner.nextInt();
                    System.out.print("Prix: ");
                    int prix = scanner.nextInt();

                    Hebergement newHebergement = new Hebergement(nom, type, adresse, ville, pays, capacite, prix);
                    hebergementService.add(newHebergement);
                    System.out.println("✅ Hébergement ajouté avec succès !");
                    break;

                case 2:
                    // Modification d'un hébergement
                    System.out.print("ID de l'hébergement à modifier: ");
                    int idUpdate = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Nouveau nom: ");
                    String newNom = scanner.nextLine();
                    System.out.print("Nouveau type: ");
                    String newType = scanner.nextLine();
                    System.out.print("Nouvelle adresse: ");
                    String newAdresse = scanner.nextLine();
                    System.out.print("Nouvelle ville: ");
                    String newVille = scanner.nextLine();
                    System.out.print("Nouveau pays: ");
                    String newPays = scanner.nextLine();
                    System.out.print("Nouvelle capacité: ");
                    int newCapacite = scanner.nextInt();
                    System.out.print("Nouveau prix: ");
                    int newPrix = scanner.nextInt();

                    Hebergement hebergementToUpdate = new Hebergement(idUpdate, newNom, newType, newAdresse, newVille, newPays, newCapacite, newPrix);
                    hebergementService.update(hebergementToUpdate);
                    System.out.println("✅ Hébergement mis à jour avec succès !");
                    break;

                case 3:
                    // Suppression d'un hébergement
                    System.out.print("ID de l'hébergement à supprimer: ");
                    int idDelete = scanner.nextInt();
                    hebergementService.delete(idDelete);
                    System.out.println("✅ Hébergement supprimé avec succès !");
                    break;

                case 4:
                    // Affichage de tous les hébergements
                    System.out.println("📋 Liste des hébergements:");
                    System.out.println(hebergementService.getAll());
                    break;

                case 5:
                    // Ajout d'une réservation
                    System.out.print("Nom du client: ");
                    String clientName = scanner.nextLine();
                    System.out.print("ID de l'hébergement: ");
                    int hebergementId = scanner.nextInt();
                    System.out.print("Date de début (YYYY-MM-DD): ");
                    String debut = scanner.next();
                    System.out.print("Date de fin (YYYY-MM-DD): ");
                    String fin = scanner.next();

                    Date dateDebut = java.sql.Date.valueOf(debut);
                    Date dateFin = java.sql.Date.valueOf(fin);

                    Reservation newReservation = new Reservation(clientName, dateDebut, dateFin, hebergementId);
                    reservationService.add(newReservation);
                    System.out.println("✅ Réservation ajoutée avec succès !");
                    break;

                case 6:
                    // Modification d'une réservation
                    System.out.print("ID de la réservation à modifier: ");
                    int idResUpdate = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Nouveau nom du client: ");
                    String newClientName = scanner.nextLine();
                    System.out.print("Nouvelle date de début (YYYY-MM-DD): ");
                    String newDebut = scanner.next();
                    System.out.print("Nouvelle date de fin (YYYY-MM-DD): ");
                    String newFin = scanner.next();

                    Date newDateDebut = java.sql.Date.valueOf(newDebut);
                    Date newDateFin = java.sql.Date.valueOf(newFin);

                    Reservation reservationToUpdate = new Reservation(idResUpdate, newClientName, newDateDebut, newDateFin, 0);
                    reservationService.update(reservationToUpdate);
                    System.out.println("✅ Réservation mise à jour avec succès !");
                    break;

                case 7:
                    // Suppression d'une réservation
                    System.out.print("ID de la réservation à supprimer: ");
                    int idResDelete = scanner.nextInt();
                    reservationService.delete(idResDelete);
                    System.out.println("✅ Réservation supprimée avec succès !");
                    break;

                case 8:
                    // Affichage de toutes les réservations
                    System.out.println("📋 Liste des réservations:");
                    List<Reservation> reservations = reservationService.getAll();
                    for (Reservation res : reservations) {
                        System.out.println(res);
                    }
                    break;

                case 0:
                    System.out.println("👋 Programme terminé. À bientôt !");
                    break;

                default:
                    System.out.println("❌ Option invalide, veuillez réessayer.");
            }

        } while (choix != 0);

        scanner.close();
    }
}*/
