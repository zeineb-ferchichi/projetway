package org.example;

import models.Abonnement;
import models.Transport;
import services.AbonnementService;
import services.TransportService;

import java.util.List;
import java.util.Scanner;

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

            try {
                int choix = Integer.parseInt(scanner.nextLine().trim());

                switch (choix) {
                    case 1:
                        ajouterTransport(scanner, transportService);
                        break;
                    case 2:
                        afficherTransports(transportService);
                        break;
                    case 3:
                        ajouterAbonnement(scanner, abonnementService, transportService);
                        break;
                    case 4:
                        afficherAbonnements(abonnementService);
                        break;
                    case 5:
                        supprimerAbonnement(scanner, abonnementService);
                        break;
                    case 6:
                        mettreAJourAbonnement(scanner, abonnementService);
                        break;
                    case 7:
                        System.out.println("👋 Au revoir !");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("❌ Choix invalide, essayez encore !");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrée invalide ! Veuillez entrer un chiffre.");
            }
        }
    }

    // ✅ Ajouter un transport
    private static void ajouterTransport(Scanner scanner, TransportService transportService) {
        System.out.print("🚍 Entrez le type de transport (Bus, Train, Taxi) : ");
        String typeTransport = scanner.nextLine();
        System.out.print("🚏 Entrez le nom de la station : ");
        String nomStation = scanner.nextLine();
        System.out.print("🌍 Entrez la zone géographique : ");
        String zoneGeographique = scanner.nextLine();

        Transport transport = new Transport(typeTransport, nomStation, zoneGeographique);
        transportService.add(transport);
        System.out.println("✅ Transport ajouté avec succès !");
    }

    // ✅ Afficher les transports
    private static void afficherTransports(TransportService transportService) {
        System.out.println("📜 Liste des transports : ");
        List<Transport> transports = transportService.getAll();
        if (transports.isEmpty()) {
            System.out.println("❌ Aucun transport trouvé !");
        } else {
            transports.forEach(System.out::println);
        }
    }

    // ✅ Ajouter un abonnement
    private static void ajouterAbonnement(Scanner scanner, AbonnementService abonnementService, TransportService transportService) {
        System.out.print("📜 Type d'abonnement (Mensuel, Annuel, Semaine) : ");
        String typeAbonnement = scanner.nextLine();

        double montant = lireDouble(scanner, "💰 Montant : ");
        int transportId = lireInt(scanner, "🆔 ID du transport : ");

        if (transportService.getById(transportId) == null) {
            System.out.println("❌ Transport ID invalide !");
            return;
        }

        int dureeValable = lireInt(scanner, "📆 Durée valable (en jours) : ");
        System.out.print("🔄 Statut de l'abonnement (Actif, Expiré, Suspendu) : ");
        String statusAbonnem = scanner.nextLine();

        Abonnement abonnement = new Abonnement(0, typeAbonnement, montant, dureeValable, transportId, statusAbonnem);
        abonnementService.add(abonnement);
        System.out.println("✅ Abonnement ajouté avec succès !");
    }

    // ✅ Afficher les abonnements
    private static void afficherAbonnements(AbonnementService abonnementService) {
        System.out.println("📜 Liste des abonnements : ");
        List<Abonnement> abonnements = abonnementService.getAll();
        if (abonnements.isEmpty()) {
            System.out.println("❌ Aucun abonnement trouvé !");
        } else {
            abonnements.forEach(System.out::println);
        }
    }

    // ✅ Supprimer un abonnement
    private static void supprimerAbonnement(Scanner scanner, AbonnementService abonnementService) {
        int idAbonnement = lireInt(scanner, "❌ Entrez l'ID de l'abonnement à supprimer : ");
        Abonnement abonnementToDelete = abonnementService.getById(idAbonnement);
        if (abonnementToDelete != null) {
            abonnementService.delete(abonnementToDelete);
            System.out.println("✅ Abonnement supprimé !");
        } else {
            System.out.println("❌ Abonnement introuvable !");
        }
    }

    // ✅ Mettre à jour un abonnement
    private static void mettreAJourAbonnement(Scanner scanner, AbonnementService abonnementService) {
        int idUpdate = lireInt(scanner, "✏️ Entrez l'ID de l'abonnement à mettre à jour : ");

        Abonnement abonnementToUpdate = abonnementService.getById(idUpdate);
        if (abonnementToUpdate != null) {
            System.out.print("🆕 Nouveau type d'abonnement : ");
            String newType = scanner.nextLine();
            double newMontant = lireDouble(scanner, "💰 Nouveau montant : ");
            int newDuree = lireInt(scanner, "📆 Nouvelle durée valable (en jours) : ");
            System.out.print("🔄 Nouveau statut (Actif, Expiré, Suspendu) : ");
            String newStatus = scanner.nextLine();

            abonnementToUpdate.setType_abonnem(newType);
            abonnementToUpdate.setMontant(newMontant);
            abonnementToUpdate.setDuree_valable(newDuree);
            abonnementToUpdate.setStatus_abonnem(newStatus);

            abonnementService.update(abonnementToUpdate);
            System.out.println("✅ Abonnement mis à jour !");
        } else {
            System.out.println("❌ Abonnement introuvable !");
        }
    }

    // ✅ Méthode pour lire un entier avec gestion des erreurs
    private static int lireInt(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrée invalide ! Veuillez entrer un nombre entier.");
            }
        }
    }

    // ✅ Méthode pour lire un double avec gestion des erreurs
    private static double lireDouble(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrée invalide ! Veuillez entrer un nombre décimal.");
            }
        }
    }
}
