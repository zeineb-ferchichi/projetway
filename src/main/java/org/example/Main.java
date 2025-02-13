package org.example;

import entities.voyage;
import entities.trajet;
import util.Datasource;
import servies.service;
import servies.voyageservice;
import servies.trajetservice;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Establish database connection
        Connection connection = Datasource.getInstance().getConnection();

        // Verify database connection
        if (connection == null) {
            System.out.println("Failed to establish database connection!");
            return; // Exit if connection fails
        }
        System.out.println("Success: Database connection established!");

        // Create instances of the voyage and trajet services
        service<voyage> voyageService = new voyageservice();
        service<trajet> trajetService = new trajetservice();

        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            // Display main menu options
            System.out.println("\nMain Menu:");
            System.out.println("1. Manage Voyages");
            System.out.println("2. Manage Trajets");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    manageVoyages(voyageService, scanner);
                    break;
                case 2:
                    manageTrajets(trajetService, scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 3);

        scanner.close();
    }

    // Method to manage voyages
    private static void manageVoyages(service<voyage> voyageService, Scanner scanner) {
        int choice;
        do {
            System.out.println("\nVoyage Management:");
            System.out.println("1. Add a new voyage");
            System.out.println("2. Display all voyages");
            System.out.println("3. Update a voyage");
            System.out.println("4. Delete a voyage");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    // Add a new voyage
                    System.out.println("Enter destination:");
                    String destination = scanner.nextLine();
                    System.out.println("Enter departure date (YYYY-MM-DD):");
                    String dateDepart = scanner.nextLine();
                    System.out.println("Enter return date (YYYY-MM-DD):");
                    String dateRetour = scanner.nextLine();

                    voyage newVoyage = new voyage(destination, dateDepart, dateRetour);
                    if (!isVoyageExists(voyageService, newVoyage)) {
                        voyageService.add(newVoyage);
                        System.out.println("New voyage added successfully!");
                    } else {
                        System.out.println("Voyage already exists. Skipping insertion.");
                    }
                    break;

                case 2:
                    // Display all voyages
                    List<voyage> voyages = voyageService.getAll();
                    System.out.println("List of all voyages:");
                    voyages.forEach(System.out::println);
                    break;

                case 3:
                    // Update a voyage
                    System.out.println("Enter the ID of the voyage to update:");
                    int voyageIdToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    voyage voyageToUpdate = findVoyageById(voyageService, voyageIdToUpdate);
                    if (voyageToUpdate != null) {
                        System.out.println("Enter new destination:");
                        String newDestination = scanner.nextLine();
                        System.out.println("Enter new departure date (YYYY-MM-DD):");
                        String newDateDepart = scanner.nextLine();
                        System.out.println("Enter new return date (YYYY-MM-DD):");
                        String newDateRetour = scanner.nextLine();

                        voyageToUpdate.setDestination(newDestination);
                        voyageToUpdate.setDate_depart(newDateDepart);
                        voyageToUpdate.setDate_retour(newDateRetour);

                        voyageService.update(voyageToUpdate);
                        System.out.println("Voyage updated successfully!");
                    } else {
                        System.out.println("Voyage with ID " + voyageIdToUpdate + " not found.");
                    }
                    break;

                case 4:
                    // Delete a voyage
                    System.out.println("Enter the ID of the voyage to delete:");
                    int voyageIdToDelete = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    voyage voyageToDelete = findVoyageById(voyageService, voyageIdToDelete);
                    if (voyageToDelete != null) {
                        voyageService.delete(voyageToDelete);
                        System.out.println("Voyage deleted successfully!");
                    } else {
                        System.out.println("Voyage with ID " + voyageIdToDelete + " not found.");
                    }
                    break;

                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 5);
    }

    // Method to manage trajets
    private static void manageTrajets(service<trajet> trajetService, Scanner scanner) {
        int choice;
        do {
            System.out.println("\nTrajet Management:");
            System.out.println("1. Add a new trajet");
            System.out.println("2. Display all trajets");
            System.out.println("3. Update a trajet");
            System.out.println("4. Delete a trajet");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    // Add a new trajet
                    System.out.println("Enter voyage ID:");
                    int idvoyage = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    System.out.println("Enter type of transport:");
                    String typeTransport = scanner.nextLine();
                    System.out.println("Enter compagnie:");
                    String compagnie = scanner.nextLine();
                    System.out.println("Enter numero:");
                    String numero = scanner.nextLine();
                    System.out.println("Enter departure date (YYYY-MM-DD):");
                    String dateDepart = scanner.nextLine();
                    System.out.println("Enter arrival date (YYYY-MM-DD):");
                    String dateArrivee = scanner.nextLine();
                    System.out.println("Enter departure city:");
                    String villeDepart = scanner.nextLine();
                    System.out.println("Enter arrival city:");
                    String villeArrivee = scanner.nextLine();
                    System.out.println("Enter cost:");
                    double cout = scanner.nextDouble();
                    scanner.nextLine(); // Consume the newline character

                    trajet newTrajet = new trajet(idvoyage, typeTransport, compagnie, numero, dateDepart, dateArrivee, villeDepart, villeArrivee, cout);
                    trajetService.add(newTrajet);
                    System.out.println("New trajet added successfully!");
                    break;

                case 2:
                    // Display all trajets
                    List<trajet> trajets = trajetService.getAll();
                    System.out.println("List of all trajet:");
                    trajets.forEach(System.out::println);
                    break;

                case 3:
                    // Update a trajet
                    System.out.println("Enter the ID of the trajet to update:");
                    int trajetIdToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    trajet trajetToUpdate = findTrajetById(trajetService, trajetIdToUpdate);
                    if (trajetToUpdate != null) {
                        System.out.println("Enter new type of transport:");
                        String newTypeTransport = scanner.nextLine();
                        System.out.println("Enter new compagnie:");
                        String newCompagnie = scanner.nextLine();
                        System.out.println("Enter new numero:");
                        String newNumero = scanner.nextLine();
                        System.out.println("Enter new departure date (YYYY-MM-DD):");
                        String newDateDepart = scanner.nextLine();
                        System.out.println("Enter new arrival date (YYYY-MM-DD):");
                        String newDateArrivee = scanner.nextLine();
                        System.out.println("Enter new departure city:");
                        String newVilleDepart = scanner.nextLine();
                        System.out.println("Enter new arrival city:");
                        String newVilleArrivee = scanner.nextLine();
                        System.out.println("Enter new cost:");
                        double newCout = scanner.nextDouble();
                        scanner.nextLine(); // Consume the newline character

                        trajetToUpdate.setType_transport(newTypeTransport);
                        trajetToUpdate.setCompagnie(newCompagnie);
                        trajetToUpdate.setNumero(newNumero);
                        trajetToUpdate.setDate_depart(newDateDepart);
                        trajetToUpdate.setDate_arrivee(newDateArrivee);
                        trajetToUpdate.setVille_depart(newVilleDepart);
                        trajetToUpdate.setVille_arrivee(newVilleArrivee);
                        trajetToUpdate.setCout(newCout);

                        trajetService.update(trajetToUpdate);
                        System.out.println("Trajet updated successfully!");
                    } else {
                        System.out.println("Trajet with ID " + trajetIdToUpdate + " not found.");
                    }
                    break;

                case 4:
                    // Delete a trajet
                    System.out.println("Enter the ID of the trajet to delete:");
                    int trajetIdToDelete = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    trajet trajetToDelete = findTrajetById(trajetService, trajetIdToDelete);
                    if (trajetToDelete != null) {
                        trajetService.delete(trajetToDelete);
                        System.out.println("Trajet deleted successfully!");
                    } else {
                        System.out.println("Trajet with ID " + trajetIdToDelete + " not found.");
                    }
                    break;

                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 5);
    }

    // Check if the voyage already exists
    private static boolean isVoyageExists(service<voyage> voyageService, voyage voyage) {
        List<voyage> voyages = voyageService.getAll();
        return voyages.stream().anyMatch(v ->
                v.getDestination().equalsIgnoreCase(voyage.getDestination()) &&
                        v.getDate_depart().equals(voyage.getDate_depart()) &&
                        v.getDate_retour().equals(voyage.getDate_retour()));
    }

    // Find a voyage by ID
    private static voyage findVoyageById(service<voyage> voyageService, int id) {
        List<voyage> voyages = voyageService.getAll();
        return voyages.stream()
                .filter(v -> v.getIdvoyage() == id)
                .findFirst()
                .orElse(null);
    }

    // Find a trajet by ID
    private static trajet findTrajetById(service<trajet> trajetService, int id) {
        List<trajet> trajets = trajetService.getAll();
        return trajets.stream()
                .filter(t -> t.getTrajet_id() == id)
                .findFirst()
                .orElse(null);
    }
}