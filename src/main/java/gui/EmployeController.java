package gui;

import Entitie.Notedefrait;
import Entitie.User;
import Service.NotedefraitService;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Service.UserService;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import org.mindrot.jbcrypt.BCrypt;

public class EmployeController {

    @FXML
    private ImageView notedefraitImage;
    @FXML
    private TextField tfNomActiviteModif;
    @FXML
    private TextArea tfDescriptionModif;

    @FXML
    private GridPane gridNotesFrais;
    @FXML
    private TextField tfNomActivite, tfId;
    @FXML
    private TextArea tfDescription;
    @FXML
    private Button btnParcourirFacture, btnAjouterNote, btnModifierNote, btnSupprimerNote;
    @FXML
    private ImageView currentUserImage;
    @FXML
    private Label currentUserName;
    @FXML
    private VBox ajoutNoteFraisPane, notesFraisContainer, modificationPane;

    private final NotedefraitService notedefraitService = new NotedefraitService();
    private String selectedFacturePath;
    private User currentUser;

    private Notedefrait selectedNoteFrais;

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
    public void initialize() {
        System.out.println("⚡ Initialisation de EmployeController");

        if (profileUserName == null) {
            System.out.println("❌ profileUserName est NULL !");
        } else {
            System.out.println("✅ profileUserName chargé !");
        }

        if (currentUserImage == null) {
            System.out.println("❌ currentUserImage est NULL !");
        } else {
            System.out.println("✅ currentUserImage chargé !");
        }
    }


    @FXML
    private void loadNotesFrais() {
        gridNotesFrais.getChildren().clear();
        List<Notedefrait> notes = notedefraitService.getAll();
        afficherNotesFrais(notes);
    }

    @FXML
    private void voirNotesUser() {
        if (currentUser == null) {
            afficherAlerte("Utilisateur non connecté !");
            return;
        }

        // Récupérer les notes de frais de l'utilisateur connecté
        List<Notedefrait> notesUser = notedefraitService.getNotesByUserId(currentUser.getId());

        // Vérifier si la liste est vide
        if (notesUser.isEmpty()) {
            afficherAlerte("Aucune note de frais trouvée pour cet utilisateur !");
            return;
        }

        // Afficher les notes de frais de l'utilisateur
        afficherNotesFrais(notesUser);
    }



    private void afficherNotesFrais(List<Notedefrait> notes) {
        // Vider le GridPane avant d'ajouter les nouvelles notes
        gridNotesFrais.getChildren().clear();

        // Initialiser les variables de position dans la grille
        int col = 0, row = 0;

        // Boucle sur toutes les notes de frais récupérées
        for (Notedefrait note : notes) {
            // Créer une carte de note de frais pour chaque note
            HBox noteCard = createNoteCard(note);

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


    @FXML
    private void ajouterNoteFrais() {
        if (currentUser == null) {
            afficherAlerte("Veuillez sélectionner un utilisateur avant d'ajouter une note de frais.");
            return;
        }
        int Id = Integer.parseInt(tfId.getText().trim());
        String nomActivite = tfNomActivite.getText().trim();
        String description = tfDescription.getText().trim();

        if (nomActivite.isEmpty() || description.isEmpty() || selectedFacturePath == null) {
            afficherAlerte("Tous les champs sont obligatoires !");
            return;
        }

        Notedefrait nouvelleNote = new Notedefrait(Id,nomActivite, description, selectedFacturePath, currentUser.getId());
        notedefraitService.insert(nouvelleNote);
        loadNotesFrais();
        clearFields();
    }
    private void selectNoteFrais(Notedefrait noteFrais) {
        if (noteFrais == null) {
            System.out.println("ERREUR : Note de frais sélectionnée est null !");
            return;
        }
        selectedNoteFrais = noteFrais;

        // Mise à jour des champs de texte
        if (tfNomActiviteModif != null) {
            tfNomActiviteModif.setText(noteFrais.getNomactivite());
        }
        if (tfDescriptionModif != null) {
            tfDescriptionModif.setText(noteFrais.getDescription());
        }

        // Mise à jour de l'image si elle existe
        if (noteFrais.getLienfacture() != null && !noteFrais.getLienfacture().isEmpty()) {
            Image image = new Image("file:" + noteFrais.getLienfacture());
            notedefraitImage.setImage(image); // Corrected
        }
    }
    @FXML
    private void modifierNoteFrais() {
        System.out.println("Selected Note: " + selectedNoteFrais);
        if (selectedNoteFrais == null) {
            afficherAlerte("Veuillez sélectionner une note de frais à modifier.");
            return;
        }

        // Set the modified values
        selectedNoteFrais.setNomactivite(tfNomActiviteModif.getText());
        selectedNoteFrais.setDescription(tfDescriptionModif.getText());

        // If a new image was selected, update the facture link
        if (selectedFacturePath != null) {
            selectedNoteFrais.setLienfacture(selectedFacturePath);
        }

        // Save the modified note (you will need to update this method based on your service)
        notedefraitService.update(selectedNoteFrais);

        // Reload the list of notes (adjust if you need to refresh the UI)
        loadNotesFrais();

        // Hide the modification pane and clear fields

        clearFields();
    }


    @FXML
    private void supprimerNoteFrais() {
        System.out.println("Selected Note: " + selectedNoteFrais);
        if (selectedNoteFrais == null) {
            afficherAlerte("Veuillez sélectionner une note de frais à supprimer.");
            return;
        }

        if (confirmDelete(selectedNoteFrais)) {
            notedefraitService.deleteById(selectedNoteFrais.getId());
            loadNotesFrais();
            clearFields();
        }
    }

    @FXML
    private void choisirFacture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images/PDF", "*.png", "*.jpg", "*.jpeg", "*.pdf"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            selectedFacturePath = file.getAbsolutePath();
        }
    }



    private HBox createNoteCard(Notedefrait note) {
        HBox noteItem = new HBox(10);
        noteItem.setPadding(new Insets(10));
        noteItem.setStyle("-fx-background-color: white; -fx-border-radius: 8px; -fx-border-color: #ddd; -fx-padding: 5px;");
        noteItem.setMinWidth(400);

        ImageView factureImage = new ImageView();
        File file = new File(note.getLienfacture());
        if (file.exists()) {
            factureImage.setImage(new Image(file.toURI().toString(), 40, 40, false, false));
        } else {
            factureImage.setImage(new Image("/img/default.jpg"));
        }
        factureImage.setFitWidth(40);
        factureImage.setFitHeight(40);

        VBox noteDetails = new VBox();
        Label activiteLabel = new Label("Activité: " + note.getNomactivite());
        activiteLabel.setStyle("-fx-font-weight: bold;");

        noteDetails.getChildren().addAll(activiteLabel);

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
        noteItem.getChildren().addAll(factureImage, noteDetails, menuButton);

        return noteItem;
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

    @FXML
    private void switchToListe() {
        ajoutNoteFraisPane.setVisible(false);
        notesFraisContainer.setVisible(true);
    }

    @FXML
    private void switchToAjout() {
        notesFraisContainer.setVisible(false);
        ajoutNoteFraisPane.setVisible(true);
    }

    private void clearFields() {
        tfId.clear();
        tfNomActivite.clear();
        tfDescription.clear();
        selectedFacturePath = null;
        selectedNoteFrais = null;
        tfNomActiviteModif.clear();
        tfDescriptionModif.clear();
        notedefraitImage.setImage(null);
    }

    private void afficherAlerte(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alerte");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    public void setCurrentUser(User user) {
        this.currentUser = user;

        if (user != null) {

            if (profileUserName != null) profileUserName.setText(user.getNom());
            if (profileUserPrenom != null) profileUserPrenom.setText(user.getPrenom());
            if (profileUserGmail != null) profileUserGmail.setText(user.getGmail());
            if (profileUserMotdepasse != null) profileUserMotdepasse.setText(user.getMotdepasse());
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






    @FXML
    private void handleChangeImage() {
        if (selectedNoteFrais == null) { // Fixed the variable name
            afficherAlerte("Veuillez d'abord sélectionner une note de frais !");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String imagePath = file.getAbsolutePath(); // Stocke le chemin de l'image
            notedefraitImage.setImage(new Image(file.toURI().toString())); // Affiche l'image
            selectedNoteFrais.setLienfacture(imagePath); // Corrected
        }
    }

    @FXML
    private void ajouterNoteFrait(ActionEvent event) throws IOException {

        // Charger le fichier FXML de la nouvelle interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutNoteFrais.fxml"));
        Parent root = loader.load();
        EmployeController controller = loader.getController();
        controller.setCurrentUser(currentUser);

        // Obtenir la scène actuelle et la remplacer par la nouvelle
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




    private void populateCurrentUserProfile() {
        if (currentUser == null) {
            return;
        }
        if (profileUserName != null) {
            profileUserName.setText(currentUser.getNom());
        }
        if (profileUserPrenom != null) {
            profileUserPrenom.setText(currentUser.getPrenom());
        }
        if (profileUserGmail != null) {
            profileUserGmail.setText(currentUser.getGmail());
        }
        if (profileUserMotdepasse != null) {
            profileUserMotdepasse.setText(currentUser.getIdentifiant());
        }
    }



    private boolean confirmUpdate() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de mise à jour");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir mettre à jour votre profil ?");
        ButtonType okButton = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(okButton, cancelButton);

        return confirmationAlert.showAndWait().orElse(cancelButton) == okButton;
    }


    // Méthode pour enregistrer les modifications du profil de l'utilisateur connecté
    @FXML
    private void handleUpdateCurrentUser(ActionEvent event) {
        if (currentUser == null) {
            afficherAlerte("Aucun utilisateur connecté !");
            return;
        }

        // Demander la confirmation de la mise à jour
        if (!confirmUpdate()) {
            return; // Annule la mise à jour si l'utilisateur ne confirme pas
        }

        // Récupération et mise à jour des informations de l'utilisateur
        String newName = profileUserName.getText();
        currentUser.setNom(newName);
        currentUser.setPrenom(profileUserPrenom.getText());
        currentUser.setGmail(profileUserGmail.getText());

        // Mise à jour du mot de passe uniquement s'il est renseigné
        String newPassword = profileUserMotdepasse.getText();
        if (!newPassword.isEmpty()) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            currentUser.setMotdepasse(hashedPassword);
        }

        // Appel à la méthode de validation
        if (!userService.validateUser(currentUser)) {
            return;
        }

        // Mise à jour de l'utilisateur en base
        userService.update(currentUser);

        // Actualiser l'affichage du label avec le nouveau nom
        if (currentUserName != null) {
            currentUserName.setText(newName);
        }

        // Actualiser l'affichage de l'image si besoin
        if (currentUserImage != null && currentUser.getImage() != null && !currentUser.getImage().isEmpty()) {
            currentUserImage.setImage(new Image("file:" + currentUser.getImage()));
        }
    }








}
