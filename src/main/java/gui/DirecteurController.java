package gui;

import Entitie.Notedefrait;
import Service.NotedefraitService;
import Entitie.User;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DirecteurController {
    @FXML
    private StackPane mainContent;
    @FXML
    private GridPane gridNotesFrais;

    private User currentUser;
    @FXML
    private ImageView currentUserImage;
    @FXML
    private Label currentUserName;
    private final UserService userService = new UserService();

    @FXML
    private TextField profileUserName;

    @FXML
    private TextField profileUserPrenom;

    @FXML
    private TextField profileUserGmail;

    @FXML
    private TextField profileUserMotdepasse;

    @FXML
    private GridPane gridNotes;

    private final NotedefraitService notedefraitService = new NotedefraitService();
    private Notedefrait selectedNoteFrais;

    // Declare UI elements
    @FXML
    private TextField tfNomActiviteModif;

    @FXML
    private TextField tfDescriptionModif;

    @FXML
    private ImageView notedefraitImage;
    @FXML
    private TextField searchBar;





    @FXML
    public void initialize() {
        if (gridNotesFrais == null) {
            System.out.println("⚠️ gridNotesFrais est NULL !");
        } else {
            System.out.println("✅ gridNotesFrais est bien chargé !");
        }

        loadNotesFrais();
    }




    private void loadNotesFrais() {
        List<Notedefrait> notes = notedefraitService.getAll();
        System.out.println("📌 Nombre de notes récupérées : " + notes.size());

        if (notes.isEmpty()) {
            System.out.println("⚠️ Aucune note trouvée !");
        }

        // Vérifier si gridNotesFrais est bien initialisé avant d'afficher
        if (gridNotesFrais == null) {
            System.out.println("⚠️ gridNotesFrais est NULL, donc on ne charge pas les notes ici.");
            return;
        }

        trierNotesParActivite(notes);
        afficherNotesFrais(notes);
    }






    @FXML
    private void handleSignIn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void afficherProfil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profil.fxml"));
            Parent root = loader.load();  // 💡 Ici le fichier est chargé

            EmployeController profilController = loader.getController();
            System.out.println("⚡ EmployeController chargé : " + profilController);

            // 🛠 Vérifier si currentUser n'est pas null avant de l'envoyer
            if (currentUser != null) {
                profilController.setCurrentUser(currentUser);
            } else {
                System.out.println("⚠️ currentUser est NULL !");
            }

            Stage stage = new Stage();
            stage.setTitle("Mon Profil");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void setCurrentUser(User user) {
        this.currentUser = user;

        if (user != null) {

            if (profileUserName != null) profileUserName.setText(user.getNom());
            if (profileUserPrenom != null) profileUserPrenom.setText(user.getPrenom());
            if (profileUserGmail != null) profileUserGmail.setText(user.getGmail());

            if (currentUserName != null) {
                currentUserName.setText(user.getNom());
            }
            // Charger l'image si elle existe
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                File file = new File(user.getImage());
                if (file.exists() && currentUserImage != null) {
                    Image image = new Image(file.toURI().toString());
                    currentUserImage.setImage(image);
                } else {
                    System.out.println("⚠️ Image introuvable : " + user.getImage());
                }
            } else {
                System.out.println("⚠️ Aucun chemin d'image défini !");
            }
        } else {
            System.out.println("⚠️ currentUser est null !");
        }
    }
    private void afficherAlerte(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alerte");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void afficherNotesFrais(List<Notedefrait> notes) {
        if (gridNotesFrais == null) {
            System.out.println("⚠️ gridNotesFrais est NULL, donc on ne peut pas afficher les notes.");
            return; // On quitte la méthode si gridNotesFrais est null
        }
        // Vider le GridPane avant d'ajouter les nouvelles notes
        gridNotesFrais.getChildren().clear();

        // Initialiser les variables de position dans la grille
        int col = 0, row = 0;

        // Boucle sur toutes les notes de frais récupérées
        for (Notedefrait note : notes) {
            // Récupérer l'utilisateur qui a créé la note
            User utilisateur = userService.getById(note.getUserId()); // Assume a service to get user

            // Créer une carte de note de frais pour chaque note
            HBox noteCard = createNoteCard(note, utilisateur);

            // Ajouter la carte à la grille à la position spécifiée
            gridNotesFrais.add(noteCard, col, row);

            // Passer à la colonne suivante
            col++;

            // Si on a atteint la 2ème colonne, on passe à la ligne suivante
            if (col == 2) {
                col = 0;
                row++;
            }
        }
    }


    private HBox createNoteCard(Notedefrait note, User utilisateur) {
        HBox noteItem = new HBox(10);
        noteItem.setPadding(new Insets(10));
        noteItem.setStyle("-fx-background-color: white; -fx-border-radius: 8px; -fx-border-color: #ddd; -fx-padding: 5px;");
        noteItem.setMinWidth(400);

        // Image de la facture
        ImageView factureImage = new ImageView();
        File file = new File(note.getLienfacture());
        if (file.exists()) {
            factureImage.setImage(new Image(file.toURI().toString(), 40, 40, false, false));
        } else {
            factureImage.setImage(new Image("/img/default.jpg"));
        }
        factureImage.setFitWidth(40);
        factureImage.setFitHeight(40);

        // Image et identifiant de l'utilisateur
        ImageView userImage = new ImageView();
        File userFile = new File(utilisateur.getImage()); // Use getImage() method
        if (userFile.exists()) {
            userImage.setImage(new Image(userFile.toURI().toString(), 40, 40, false, false));
        } else {
            userImage.setImage(new Image("/img/default_user.jpg"));
        }
        userImage.setFitWidth(40);
        userImage.setFitHeight(40);

        VBox noteDetails = new VBox();
        Label activiteLabel = new Label("Activité: " + note.getNomactivite());
        activiteLabel.setStyle("-fx-font-weight: bold;");
        Label userIdLabel = new Label("Utilisateur: " + utilisateur.getIdentifiant()); // Affiche l'identifiant de l'utilisateur
        userIdLabel.setStyle("-fx-font-style: italic;");

        noteDetails.getChildren().addAll(activiteLabel, userIdLabel);

        // Menu pour Modifier/Supprimer
        MenuButton menuButton = new MenuButton("⋮");
        MenuItem modifier = new MenuItem("Modifier");
        MenuItem supprimer = new MenuItem("Supprimer");

        modifier.setOnAction(e -> selectNoteFrais(note));

        supprimer.setOnAction(e -> {
            selectNoteFrais(note); // sélectionne la note
            supprimerNoteFrais();  // puis supprime
        });

        menuButton.getItems().addAll(modifier, supprimer);

        HBox.setHgrow(noteDetails, Priority.ALWAYS);
        noteItem.getChildren().addAll(userImage, factureImage, noteDetails, menuButton);

        return noteItem;
    }
    private void selectNoteFrais(Notedefrait noteFrais) {
        if (noteFrais == null) {
            System.out.println("ERREUR : Note de frais sélectionnée est null !");
            return;
        }
        selectedNoteFrais = noteFrais;

        /*// Mise à jour des champs de texte
        if (tfNomActiviteModif != null) {
            tfNomActiviteModif.setText(noteFrais.getNomactivite());
        }
        if (tfDescriptionModif != null) {
            tfDescriptionModif.setText(noteFrais.getDescription());
        }*/

        // Mise à jour de l'image si elle existe
        if (noteFrais.getLienfacture() != null && !noteFrais.getLienfacture().isEmpty()) {
            Image image = new Image("file:" + noteFrais.getLienfacture());
            notedefraitImage.setImage(image); // Corrected
        }
    }

    private void supprimerNoteFrais() {
        if (selectedNoteFrais == null) {
            afficherAlerte("Veuillez sélectionner une note de frais à supprimer.");
            return;
        }

        // Vérifier que l'utilisateur connecté est bien le propriétaire de la note
        if (currentUser == null || selectedNoteFrais.getUserId() != currentUser.getId()) {
            afficherAlerte("Vous ne pouvez supprimer que vos propres notes de frais !");
            return;
        }

        if (confirmDelete(selectedNoteFrais)) {
            notedefraitService.deleteById(selectedNoteFrais.getId());
            loadNotesFrais();  // Reload notes after deletion
            clearFields();      // Clear fields after deletion
        }
    }


    private boolean confirmDelete(Notedefrait note) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer la note de frais : " + note.getNomactivite() + " ?");
        ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);
        return alert.showAndWait().orElse(noButton) == yesButton;
    }



    // Method to clear the fields
    private void clearFields() {
        if (tfNomActiviteModif != null) {
            tfNomActiviteModif.clear();
        }
        if (tfDescriptionModif != null) {
            tfDescriptionModif.clear();
        }
        if (notedefraitImage != null) {
            notedefraitImage.setImage(null); // Clear the image
        }
    }


    @FXML
    private void handleSearch() {
        String searchText = searchBar.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadNotesFrais(); // Recharger toutes les notes si le champ est vide
            return;
        }

        List<Notedefrait> filteredNotes = notedefraitService.getAll().stream()
                .filter(note -> note.getNomactivite().toLowerCase().contains(searchText) ||
                        userService.getById(note.getUserId()).getIdentifiant().toLowerCase().contains(searchText))
                .toList();

        afficherNotesFrais(filteredNotes);
    }

    private void trierNotesParActivite(List<Notedefrait> notes) {
        // Utilisation de la méthode sort() de la collection pour trier la liste en fonction du nom de l'activité
        notes.sort((note1, note2) -> note1.getNomactivite().compareToIgnoreCase(note2.getNomactivite()));

        // Après avoir trié, vous pouvez rafraîchir l'affichage des notes de frais
        afficherNotesFrais(notes);
    }


    @FXML
    private void afficherDirecteur() {
        try {
            // Load the new FXML file (your second interface with the list)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/directeur.fxml"));
            VBox secondScene = loader.load();

            // Replace the current content with the new content (the list view)
            mainContent.getChildren().clear(); // Clear the current content
            mainContent.getChildren().add(secondScene); // Add the new content

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
