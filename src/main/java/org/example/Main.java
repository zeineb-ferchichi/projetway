package org.example;

import models.Abonnement;
import models.Transport;
import services.AbonnementService;
import services.TransportService;

import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TransportService transportService = new TransportService();
        AbonnementService abonnementService = new AbonnementService();

        while (true) {
            System.out.println("\n========== MENU ==========");
            System.out.println("1️⃣ Ajouter un Transport");
            System.out.println("2️⃣ Afficher tous les Transports");
            System.out.println("3️⃣ Ajouter un Abonnement");
            System.out.println("4️⃣ Afficher tous les Abonnements");
            System.out.println("5️⃣ Supprimer un Abonnement");
            System.out.println("6️⃣ Mettre à jour un Abonnement");
            System.out.println("7️⃣ Quitter");
            System.out.print("➡️ Choisissez une option : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Pour éviter les bugs d'entrée

            switch (choix) {
                case 1:
                    System.out.print("🚍 Entrez le type de transport : ");
                    String typeTransport = scanner.nextLine();
                    System.out.print("🌍 Entrez la zone géographique : ");
                    String zoneGeographique = scanner.nextLine();

                    // Création d'un transport
                    // Constructeur : Transport(String type_transp, Date date_transp, String zone_geographique)
                    Transport transport = new Transport(typeTransport, new Date(), zoneGeographique);
                    transportService.add(transport);
                    System.out.println("✅ Transport ajouté avec succès !");
                    break;

                case 2:
                    System.out.println("📜 Liste des transports : ");
                    List<Transport> transports = transportService.getAll();
                    for (Transport t : transports) {
                        System.out.println(t);
                    }
                    break;

                case 3:
                    System.out.print("📜 Type d'abonnement : ");
                    String typeAbonnement = scanner.nextLine();
                    System.out.print("💰 Montant : ");
                    double montant = scanner.nextDouble();
                    System.out.print("🆔 ID du transport : ");
                    int transportId = scanner.nextInt();

                    // Création d'un abonnement
                    // Constructeur : Abonnement(String type_abonnem, double montant, Date date_debut, Date date_fin, int transport_id)
                    Abonnement abonnement = new Abonnement(typeAbonnement, montant, new Date(), new Date(), transportId);

                    abonnementService.add(abonnement);
                    System.out.println("✅ Abonnement ajouté avec succès !");
                    break;

                case 4:
                    System.out.println("📜 Liste des abonnements : ");
                    List<Abonnement> abonnements = abonnementService.getAll();
                    for (Abonnement a : abonnements) {
                        System.out.println(a);
                    }
                    break;

                case 5:
                    System.out.print("❌ Entrez l'ID de l'abonnement à supprimer : ");
                    int idAbonnement = scanner.nextInt();
                    Abonnement abonnementToDelete = abonnementService.getById(idAbonnement);
                    if (abonnementToDelete != null) {
                        abonnementService.delete(abonnementToDelete); // Supprime l'objet Abonnement
                        System.out.println("✅ Abonnement supprimé !");
                    } else {
                        System.out.println("❌ Abonnement introuvable !");
                    }
                    break;

                case 6:
                    System.out.print("✏️ Entrez l'ID de l'abonnement à mettre à jour : ");
                    int idUpdate = scanner.nextInt();
                    scanner.nextLine(); // éviter le problème de saut de ligne

                    Abonnement abonnementToUpdate = abonnementService.getById(idUpdate);
                    if (abonnementToUpdate != null) {
                        System.out.print("🆕 Nouveau type d'abonnement : ");
                        String newType = scanner.nextLine();
                        System.out.print("💰 Nouveau montant : ");
                        double newMontant = scanner.nextDouble();

                        abonnementToUpdate.setType_abonnem(newType);
                        abonnementToUpdate.setMontant(newMontant);
                        abonnementService.update(abonnementToUpdate);
                        System.out.println("✅ Abonnement mis à jour !");
                    } else {
                        System.out.println("❌ Abonnement introuvable !");
                    }
                    break;

                case 7:
                    System.out.println("👋 Au revoir !");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("❌ Choix invalide, essayez encore !");
            }
        }
    }
}
