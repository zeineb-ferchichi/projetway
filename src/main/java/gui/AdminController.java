package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import Entitie.User;
import Service.UserService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private VBox userListContainer;


    @FXML
    private VBox adminListContainer;

    @FXML
    private ImageView userImage;
    @FXML
    private TextField userName, userPrenom, userGmail, userIdentifiant, userRole;
    @FXML
    private PasswordField userMotdepasse;
    @FXML
    private ImageView currentUserImage;
    @FXML
    private Label currentUserName;

    private final UserService userService = new UserService();
    private User selectedUser = null;
    private User currentUser; // L'utilisateur connecté

    @FXML
    public void setCurrentUser(User user) {
        this.currentUser = user;

        if (user != null) {
            System.out.println("AdminController - Current User Set: " + user.getId());
            if (currentUserImage != null && user.getImage() != null && !user.getImage().isEmpty()) {
                currentUserImage.setImage(new Image("file:" + user.getImage()));
            }
            if (currentUserName != null) {
                currentUserName.setText(user.getNom());
            }
        } else {
            System.out.println("AdminController - currentUser est toujours null !");
        }
        // Par défaut, affiche la liste des utilisateurs
        afficherUsers();
    }

    @FXML
    private void handleSignIn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (userListContainer == null) {
            System.out.println("❌ userListContainer est NULL !");
        } else {
            System.out.println("✅ userListContainer est bien initialisé !");
            userListContainer.setVisible(true); // Vérifie si l'affichage fonctionne
        }
        // S'assurer que le conteneur admin est aussi initialisé
        if (adminListContainer != null) {
            adminListContainer.setVisible(false);
        }
    }

    // Méthode pour afficher la liste des utilisateurs (non-admin)
    public void afficherUsers() {
        userListContainer.getChildren().clear();
        List<User> users = userService.getAll();

        for (User user : users) {
            // Ici, on considère que le rôle "admin" identifie un administrateur.
            // On n'affiche que les utilisateurs non-admin
            if (user.getRole().equalsIgnoreCase("admin")) {
                continue;
            }
            if (currentUser != null && user.getId() == currentUser.getId()) {
                continue;
            }
            userListContainer.getChildren().add(createUserCard(user));
        }
    }

    // Méthode pour afficher la liste des administrateurs
    public void afficherAdmins() {
        adminListContainer.getChildren().clear();
        List<User> users = userService.getAll();

        for (User user : users) {
            // Affiche uniquement les utilisateurs dont le rôle est "admin"
            if (!user.getRole().equalsIgnoreCase("admin")) {
                continue;
            }
            // Suppression de la condition qui excluait l'admin connecté
            adminListContainer.getChildren().add(createUserCard(user));
        }
    }


    // Méthode générique pour créer une carte utilisateur (peut être réutilisée pour les admins)
    private HBox createUserCard(User user) {
        HBox userItem = new HBox(10);
        userItem.setPadding(new Insets(10));
        userItem.setStyle("-fx-background-color: white; -fx-border-radius: 8px; -fx-border-color: #ddd; -fx-padding: 5px;");
        userItem.setMinWidth(400);

        // Avatar de l'utilisateur
        ImageView avatar = new ImageView();
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            avatar.setImage(new Image("file:" + user.getImage(), 40, 40, false, false));
        }
        avatar.setFitWidth(40);
        avatar.setFitHeight(40);

        // Détails de l'utilisateur
        VBox userDetails = new VBox();
        Label nameLabel = new Label(user.getNom());
        nameLabel.setStyle("-fx-font-weight: bold;");
        Label companyLabel = new Label(user.getGmail());

        userDetails.getChildren().addAll(nameLabel, companyLabel);

        // Menu "3 points" pour afficher plus d'options
        MenuButton menuButton = new MenuButton("⋮");
        menuButton.getItems().add(new MenuItem("Voir Détails"));
        menuButton.getItems().add(new MenuItem("Modifier"));
        menuButton.getItems().add(new MenuItem("Supprimer"));

        menuButton.getItems().get(0).setOnAction(e -> showUserDetails(user));
        menuButton.getItems().get(1).setOnAction(e -> {
            selectUser(user);  // Sélectionne l'utilisateur pour modification
        });
        menuButton.getItems().get(2).setOnAction(e -> {
            if (confirmDelete(user)) {  // Demande confirmation avant suppression
                handleDeleteUser();
            }
        });

        HBox.setHgrow(userDetails, Priority.ALWAYS);
        userItem.getChildren().addAll(avatar, userDetails, menuButton);

        return userItem;
    }

    private void showUserDetails(User user) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'utilisateur");

        // Par exemple, on affiche en entête : Nom + Prénom
        alert.setHeaderText(user.getNom() + " " + user.getPrenom());

        // On prépare le contenu avec toutes les infos
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(user.getId()).append("\n")
                .append("Nom: ").append(user.getNom()).append("\n")
                .append("Prénom: ").append(user.getPrenom()).append("\n")
                .append("Email: ").append(user.getGmail()).append("\n")
                .append("Identifiant: ").append(user.getIdentifiant()).append("\n")
                .append("Rôle: ").append(user.getRole()).append("\n")
                .append("Mot de passe: ").append(user.getMotdepasse()).append("\n")
                .append("Image: ").append(user.getImage());

        // Assigne le texte au contenu de l'alerte
        alert.setContentText(sb.toString());

        alert.showAndWait();
    }


    private void selectUser(User user) {
        if (user == null) {
            System.out.println("ERREUR : Utilisateur sélectionné est null !");
            return;
        }
        selectedUser = user;
        if (userName != null) {
            userName.setText(user.getNom());
        }
        if (userPrenom != null) {
            userPrenom.setText(user.getPrenom());
        }
        if (userGmail != null) {
            userGmail.setText(user.getGmail());
        }
        if (userIdentifiant != null) {
            userIdentifiant.setText(user.getIdentifiant());
        }
        if (userRole != null) {
            userRole.setText(user.getRole());
        }
        if (userMotdepasse != null) {
            userMotdepasse.setText(user.getMotdepasse());
        }
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Image image = new Image("file:" + user.getImage());
            userImage.setImage(image);
        }
    }

    @FXML
    private void handleEditUser() {
        if (selectedUser == null) {
            System.out.println("Aucun utilisateur sélectionné !");
            return;
        }
        selectedUser.setNom(userName.getText());
        selectedUser.setPrenom(userPrenom.getText());
        selectedUser.setGmail(userGmail.getText());
        selectedUser.setIdentifiant(userIdentifiant.getText());
        selectedUser.setRole(userRole.getText());
        selectedUser.setMotdepasse(userMotdepasse.getText());

        // Vérifier l'unicité de l'identifiant (en excluant l'utilisateur en cours)
        if (!userService.isUniqueIdentifiant(selectedUser.getIdentifiant(), selectedUser.getId())) {
            showAlert("Cet identifiant est déjà utilisé par un autre utilisateur !");
            return;
        }
        if (userImage.getImage() != null) {
            selectedUser.setImage(userImage.getImage().getUrl().replace("file:", ""));
        }

        userService.update(selectedUser);
        // Rafraîchir la liste affichée
        if (selectedUser.getRole().equalsIgnoreCase("admin")) {
            afficherAdmins();
        } else {
            afficherUsers();
        }
        showAlert("Utilisateur mis à jour avec succès !");
    }

    @FXML
    private void handleChangeImage() {
        if (selectedUser == null) {
            showAlert("Veuillez d'abord sélectionner un utilisateur !");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image de profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String imagePath = file.toURI().toString();
            userImage.setImage(new Image(imagePath));
            selectedUser.setImage(file.getAbsolutePath());
        }
    }

    private boolean confirmDelete(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer " + user.getNom() + " ?");
        ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);
        return alert.showAndWait().orElse(noButton) == yesButton;
    }

    @FXML
    private void handleDeleteUser() {
        if (selectedUser == null) {
            showAlert("Veuillez sélectionner un utilisateur !");
            return;
        }
        if (currentUser != null && selectedUser.getId() == currentUser.getId()) {
            showAlert("Vous ne pouvez pas vous supprimer vous-même !");
            return;
        }
        if (confirmDelete(selectedUser)) {
            userService.deleteById(selectedUser.getId());
            // Rafraîchir la liste affichée en fonction du rôle de l'utilisateur supprimé
            if (selectedUser.getRole().equalsIgnoreCase("admin")) {
                afficherAdmins();
            } else {
                afficherUsers();
            }
            showAlert("Utilisateur supprimé avec succès !");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    // Méthode appelée lors du clic sur le lien "listeUser"
    @FXML
    private void showUserList(ActionEvent event) {
        userListContainer.setVisible(true);
        adminListContainer.setVisible(false);
        afficherUsers();
    }

    // Méthode appelée lors du clic sur le lien "listeAdmin"
    @FXML
    private void showAdminList(ActionEvent event) {
        adminListContainer.setVisible(true);
        userListContainer.setVisible(false);
        afficherAdmins();
    }
    @FXML
    public void setUser(User user) {
        this.currentUser = user;

        // Handle user data in the profile (e.g., setting the user information in the UI)
        if (currentUser != null) {
            // Example of displaying user info
            System.out.println("User set: " + user.getNom());
        }
    }

}
