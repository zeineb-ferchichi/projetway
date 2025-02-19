package org.example;

import services.MissionService;
import services.RapportService;


import services.IService;
import entities.Mission;
import entities.Rapport;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import entities.Mission;
import entities.Rapport;
import entities.RapportMissionDTO;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IService<Mission> missionService = new MissionService();
        RapportService rapportService = new RapportService(); // ✅ Utilisation directe

        while (true) {
            System.out.println("\n===== MENU CRUD =====");
            System.out.println("1. Ajouter une Mission");
            System.out.println("2. Afficher toutes les Missions");
            System.out.println("3. Modifier une Mission");
            System.out.println("4. Supprimer une Mission");
            System.out.println("5. Ajouter un Rapport");
            System.out.println("6. Afficher tous les Rapports");
            System.out.println("7. Modifier un Rapport");
            System.out.println("8. Supprimer un Rapport");
            System.out.println("9. Voir les rapports avec leurs missions");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.print("Nom de la mission : ");
                    String nom = scanner.nextLine();
                    System.out.print("Description : ");
                    String desc = scanner.nextLine();
                    Mission mission = new Mission(nom, LocalDate.now(), LocalDate.now().plusDays(7), desc);
                    missionService.add(mission);
                    System.out.println("✅ Mission ajoutée !");
                    break;
                case 2:
                    List<Mission> missions = missionService.getAll();
                    missions.forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("ID de la mission à modifier : ");
                    int idM = scanner.nextInt();
                    scanner.nextLine();
                    Mission m = missionService.getById(idM);
                    if (m != null) {
                        System.out.print("Nouveau nom : ");
                        m.setNomMission(scanner.nextLine());
                        missionService.update(m);
                        System.out.println("✅ Mission mise à jour !");
                    }
                    break;
                case 4:
                    System.out.print("ID de la mission à supprimer : ");
                    int idMSup = scanner.nextInt();
                    missionService.delete(new Mission(idMSup));
                    System.out.println("🗑️ Mission supprimée !");
                    break;
                case 5:
                    System.out.print("ID de la mission associée : ");
                    int idMiss = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Libellé du rapport : ");
                    String libelle = scanner.nextLine();
                    System.out.print("Ressources JSON : ");
                    List<String> ressources = Arrays.asList(scanner.nextLine().split(","));
                    Rapport rapport = new Rapport(libelle, LocalDate.now(), ressources, missionService.getById(idMiss));
                    rapportService.add(rapport);
                    System.out.println("✅ Rapport ajouté !");
                    break;
                case 6:
                    List<Rapport> rapports = rapportService.getAll();
                    rapports.forEach(System.out::println);
                    break;
                case 7:
                    System.out.print("ID du rapport à modifier : ");
                    int idR = scanner.nextInt();
                    scanner.nextLine();
                    Rapport r = rapportService.getById(idR);
                    if (r != null) {
                        System.out.print("Nouveau libellé : ");
                        r.setLibelleR(scanner.nextLine());
                        rapportService.update(r);
                        System.out.println("✅ Rapport mis à jour !");
                    }
                    break;
                case 8:
                    System.out.print("ID du rapport à supprimer : ");
                    int idRSup = scanner.nextInt();
                    rapportService.delete(new Rapport(idRSup));
                    System.out.println("🗑️ Rapport supprimé !");
                    break;
                case 9:
                    List<RapportMissionDTO> liste = rapportService.getAllRapportsAvecMissions();
                    liste.forEach(System.out::println);
                    break;

                case 0:
                    System.out.println("👋 Au revoir !");
                    return;
                default:
                    System.out.println("❌ Choix invalide !");
            }
        }
    }
}
