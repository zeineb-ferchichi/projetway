package gui;

import Entitie.Notedefrait;
import Entitie.User;
import Service.NotedefraitService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Service.UserService;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.Provider;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import org.mindrot.jbcrypt.BCrypt;
import javafx.stage.Window;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;




public class EmployeController {

    @FXML
    private TextField searchNoteField;

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

        if (currentUserName == null) {
            System.out.println("❌ currentUserName est NULL !");
        } else {
            System.out.println("✅ currentUserName chargé !");
        }

        if (currentUserImage == null) {
            System.out.println("❌ currentUserImage est NULL !");
        } else {
            System.out.println("✅ currentUserImage chargé !");
        }
    }



    private void trierNotesParActivite(List<Notedefrait> notes) {
        // Utilisation de la méthode sort() de la collection pour trier la liste en fonction du nom de l'activité
        notes.sort((note1, note2) -> note1.getNomactivite().compareToIgnoreCase(note2.getNomactivite()));

        // Après avoir trié, vous pouvez rafraîchir l'affichage des notes de frais
        afficherNotesFrais(notes);
    }


    @FXML
    private void loadNotesFrais() {
        if (currentUser == null) {
            afficherAlerte("Utilisateur non connecté !");
            return;
        }
        gridNotesFrais.getChildren().clear();
        List<Notedefrait> notes = notedefraitService.getNotesByUserId(currentUser.getId());
        trierNotesParActivite(notes);
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
            afficherAlerte("Erreur : Utilisateur non connecté !");
            return;
        }

        String nomActivite = tfNomActivite.getText().trim();
        String description = tfDescription.getText().trim();

        if (nomActivite.isEmpty() || description.isEmpty() || selectedFacturePath == null) {
            afficherAlerte("Tous les champs sont obligatoires !");
            return;
        }

        // Créez une instance de Notedefrait pour la validation
        Notedefrait nouvelleNote = new Notedefrait(nomActivite, description, selectedFacturePath, currentUser.getId());

        // Validation de la nouvelle note de frais
        if (!Service.NotedefraitService.validateNotedefrait(nouvelleNote)) {
            afficherAlerte("Erreur de validation des données !");
            return;
        }

        // Associer la note à l'utilisateur connecté
        notedefraitService.insert(nouvelleNote);

        loadNotesFrais(); // Rafraîchir uniquement les notes de l'utilisateur
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
        if (selectedNoteFrais == null) {
            afficherAlerte("Veuillez sélectionner une note de frais à modifier.");
            return;
        }

        // Vérifier que l'utilisateur connecté est bien le propriétaire de la note
        if (currentUser == null || selectedNoteFrais.getUserId() != currentUser.getId()) {
            afficherAlerte("Vous ne pouvez modifier que vos propres notes de frais !");
            return;
        }

        // Récupérer les nouvelles valeurs des champs
        String nouveauNom = tfNomActiviteModif.getText().trim();
        String nouvelleDescription = tfDescriptionModif.getText().trim();

        // Vérifier que les champs ne sont pas vides
        if (nouveauNom.isEmpty() || nouvelleDescription.isEmpty()) {
            afficherAlerte("Tous les champs doivent être remplis !");
            return;
        }

        // Créez une instance de Notedefrait pour la validation (sans modifier le lien de facture)
        Notedefrait nouvelleNote = new Notedefrait(nouveauNom, nouvelleDescription, selectedNoteFrais.getLienfacture(), currentUser.getId());

        // Validation de la nouvelle note de frais
        if (!Service.NotedefraitService.validateNotedefrait(nouvelleNote)) {
            afficherAlerte("Erreur de validation des données !");
            return;
        }

        // Mise à jour des valeurs dans l'objet sélectionné (sans toucher au lien de facture)
        selectedNoteFrais.setNomactivite(nouveauNom);
        selectedNoteFrais.setDescription(nouvelleDescription);

        // Appeler le service pour effectuer la mise à jour
        notedefraitService.update(selectedNoteFrais);

        // Rafraîchir la liste des notes affichées
        loadNotesFrais();

        // Nettoyer les champs après modification
        clearFields();
    }






    @FXML
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
        loadNotesFrais(); //
    }


    @FXML
    private void switchToAjout() {
        notesFraisContainer.setVisible(false);
        ajoutNoteFraisPane.setVisible(true);
    }

    private void clearFields() {

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
    private Button signInButton;

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
            System.out.println("✅ Utilisateur mis à jour : " + user.getNom());

            // Vérification et mise à jour des labels
            if (profileUserName != null) profileUserName.setText(user.getNom());
            if (profileUserPrenom != null) profileUserPrenom.setText(user.getPrenom());
            if (profileUserGmail != null) profileUserGmail.setText(user.getGmail());

            if (currentUserName != null) {
                currentUserName.setText(user.getNom());
                System.out.println("✅ currentUserName mis à jour avec : " + user.getNom());
            } else {
                System.out.println("⚠️ currentUserName est null, rechargement forcé...");
                forceReloadUI();
            }

            // Charger l'image si elle existe
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                File file = new File(user.getImage());
                if (file.exists() && currentUserImage != null) {
                    Image image = new Image(file.toURI().toString());
                    currentUserImage.setImage(image);
                    System.out.println("✅ Image de l'utilisateur chargée !");
                } else {
                    System.out.println("⚠️ L'image de l'utilisateur n'existe pas !");
                }
            }
        } else {
            System.out.println("⚠️ currentUser est null !");
        }
    }



    private void forceReloadUI() {
        Platform.runLater(() -> {
            if (currentUserName != null && currentUser != null) {
                currentUserName.setText(currentUser.getNom());
                System.out.println("🔄 UI rechargée avec : " + currentUser.getNom());
            }
        });
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


    @FXML
    private void handleUpdateCurrentUser(ActionEvent event) {
        if (currentUser == null) {
            afficherAlerte("Aucun utilisateur connecté !");
            return;
        }

        // Confirmation de la mise à jour
        if (!confirmUpdate()) {
            return;
        }

        // Mise à jour des informations
        currentUser.setNom(profileUserName.getText());
        currentUser.setPrenom(profileUserPrenom.getText());
        currentUser.setGmail(profileUserGmail.getText());

        String newPassword = profileUserMotdepasse.getText();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            currentUser.setMotdepasse(hashedPassword);
        }

        if (!userService.validateUser(currentUser)) {
            return;
        }

        userService.update(currentUser);

        // Fermer toutes les fenêtres avant d'ouvrir la connexion
        closeAllWindows();

        // Ouvrir la fenêtre de connexion
        openSignInWindow();
    }

    @FXML
    private void handleChangeProfileImage(ActionEvent event) {
        if (currentUser == null) {
            afficherAlerte("Aucun utilisateur connecté !");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une nouvelle image de profil");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null && afficherConfirmation("Confirmer", "Voulez-vous vraiment changer votre image de profil ?")) {
            currentUser.setImage(file.getAbsolutePath());
            userService.update(currentUser);

            // Fermer toutes les fenêtres avant d'ouvrir la connexion
            closeAllWindows();

            // Ouvrir la fenêtre de connexion
            openSignInWindow();
        }
    }

    private void closeAllWindows() {

        List<Stage> openStages = new ArrayList<>();

        for (Window window : Stage.getWindows()) {
            if (window instanceof Stage) {
                openStages.add((Stage) window);
            }
        }

        for (Stage stage : openStages) {
            stage.close();
        }
    }

    private void openSignInWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage signInStage = new Stage();
            signInStage.setScene(scene);
            signInStage.setTitle("Connexion");
            signInStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte("Erreur lors de l'ouverture de la page de connexion.");
        }
    }







    private boolean afficherConfirmation(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType buttonOui = new ButtonType("Oui");
        ButtonType buttonNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonOui, buttonNon);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonOui;
    }

    private void updateCurrentUserDisplay() {
        if (currentUser == null) {
            afficherAlerte("Aucun utilisateur connecté !");
            return;
        }

        // Vérification et mise à jour du nom
        if (currentUserName != null) {
            currentUserName.setText(currentUser.getNom());
        } else {
            System.out.println("Erreur: currentUserName est null !");
        }

        // Vérification et mise à jour de l'image
        if (currentUserImage != null) {
            if (currentUser.getImage() != null && !currentUser.getImage().isEmpty()) {
                currentUserImage.setImage(new Image("file:" + currentUser.getImage()));
            } else {
                System.out.println("Aucune image utilisateur définie.");
            }
        } else {
            System.out.println("Erreur: currentUserImage est null !");
        }
    }






    @FXML
    private void searchNotesByName() {
        if (currentUser == null) {
            afficherAlerte("Utilisateur non connecté !");
            return;
        }

        String searchText = searchNoteField.getText().trim();

        if (isSearchTextEmpty(searchText)) {
            // Si la recherche est vide, afficher toutes les notes de frais
            List<Notedefrait> allNotes = notedefraitService.getNotesByUserId(currentUser.getId());
            afficherNotesFrais(allNotes);
            return;
        }

        // Sinon, effectuer la recherche par nom
        List<Notedefrait> filteredNotes = notedefraitService.searchNotesByName(currentUser.getId(), searchText);

        if (filteredNotes.isEmpty()) {
            afficherAlerte("Aucune note de frais trouvée avec ce nom !");
        } else {
            afficherNotesFrais(filteredNotes);
        }
    }

    private boolean isSearchTextEmpty(String text) {
        return text == null || text.isEmpty();
    }






















}
