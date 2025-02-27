package gui;

import Entitie.Notedefrait;
import Service.NotedefraitService;
import Entitie.User;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DirecteurController {

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

    @FXML
    public void initialize() {

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

        // Demander la confirmation de la mise à jour
        if (!confirmUpdate()) {
            return; // Annule la mise à jour si l'utilisateur ne confirme pas
        }

        // Récupération et mise à jour des informations de l'utilisateur
        String newName = profileUserName.getText();
        currentUser.setNom(newName);
        currentUser.setPrenom(profileUserPrenom.getText());
        currentUser.setGmail(profileUserGmail.getText());

        // Mise à jour du mot de passe uniquement si le champ est rempli
        String newPassword = profileUserMotdepasse.getText();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            currentUser.setMotdepasse(hashedPassword);
        }
        // Sinon, le mot de passe reste inchangé

        // Vérifier que les autres informations utilisateur sont valides
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




}
