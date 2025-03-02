package gui;

import Entitie.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.List;

import static Entitie.User.currentUser;

public class signIn {

    @FXML
    private TextField identifiantField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordFieldVisible;
    private boolean passwordVisible = false;
    @FXML
    private Button btnTogglePassword;



    private final UserService userService = new UserService();


    @FXML
    private void handleCreateAccount(ActionEvent event) {
        try {
            // Charger le fichier FXML de Create Account
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Create Account");
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle (optionnel)
            ((Stage) ((Hyperlink) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void togglePasswordVisibility(ActionEvent event) {
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            passwordFieldVisible.setText(passwordField.getText());
            passwordFieldVisible.setVisible(true);
            passwordField.setVisible(false);
            btnTogglePassword.setText("👁");
        } else {
            passwordField.setText(passwordFieldVisible.getText());
            passwordFieldVisible.setVisible(false);
            passwordField.setVisible(true);
            btnTogglePassword.setText("🙈");
        }
    }


    @FXML
    private void handleSignIn(ActionEvent event) {
        // Affichage des valeurs saisies pour le débogage
        System.out.println("Identifiant saisi : '" + identifiantField.getText().trim() + "'");
        System.out.println("Mot de passe saisi : '" + passwordField.getText().trim() + "'");

        String identifiant = identifiantField.getText().trim();
        String password = passwordField.getText().trim();

        if (identifiant.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Échec de connexion", "Veuillez entrer l'identifiant et le mot de passe.");
            return;
        }

        // Fetch all users from the database
        List<User> users = userService.getAll();
        User user = null;

        // Find the user with the provided identifiant
        for (User u : users) {
            if (u.getIdentifiant().equals(identifiant)) {
                user = u;
                break;
            }
        }

        // Maintenant que 'user' est défini, on peut afficher le hash
        if (user != null) {
            System.out.println("Hash du mot de passe en base : " + user.getMotdepasse());
        }

        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Échec de connexion", "Identifiant ou mot de passe invalide.");
            return;
        }

        System.out.println("Ban status: " + user.getBan()); // For debugging
        if ("true".equals(user.getBan())) {
            showAlert(Alert.AlertType.ERROR, "Accès refusé", "Votre compte a été banni. Veuillez contacter l'administrateur.");
            return;
        }

        // Vérifier le mot de passe
        if (BCrypt.checkpw(password, user.getMotdepasse())) {
            // Si la vérification est réussie, vérifier le rôle et ouvrir l'interface correspondante
            ouvrirInterfaceSelonRole(event, user);
        } else {
            showAlert(Alert.AlertType.ERROR, "Échec de connexion", "Identifiant ou mot de passe invalide.");
        }
    }








    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void ouvrirInterfaceSelonRole(ActionEvent event, User user) {
        try {
            if (user == null) {
                System.out.println("Erreur: user est null après connexion !");
                return;
            }

            System.out.println("Utilisateur connecté: " + user.getNom()
                    + " ID: " + user.getIdentifiant()
                    + " Rôle: " + user.getRole());

            FXMLLoader loader;
            Parent root = null; // ✅ Initialise root à null pour éviter une erreur

            // Vérification du rôle
            switch (user.getRole().toLowerCase()) {
                case "admin":
                    loader = new FXMLLoader(getClass().getResource("/admin.fxml"));
                    root = loader.load();
                    AdminController adminController = loader.getController();
                    adminController.setCurrentUser(user);
                    break;

                case "employe":
                    loader = new FXMLLoader(getClass().getResource("/employe.fxml"));
                    root = loader.load();
                    EmployeController employeController = loader.getController();
                    employeController.setCurrentUser(user);
                    break;

            case "directeur":
                loader = new FXMLLoader(getClass().getResource("/directeurr.fxml"));
                root = loader.load();
                DirecteurController directeurController = loader.getController();
                directeurController.setCurrentUser(user);
                break;

                default:
                    showAlert(Alert.AlertType.ERROR, "Accès refusé", "Votre rôle ne permet pas d'accéder à cette application.");
                    return;
            }

            // ✅ Vérifier que root est bien initialisé avant d'afficher la scène
            if (root == null) {
                System.out.println("Erreur : l'interface n'a pas été chargée !");
                return;
            }

            // Affichage de la nouvelle scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface.");
        }
    }












    @FXML
    private void handleForgotPassword(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Réinitialisation du mot de passe");
        dialog.setHeaderText("Entrez votre identifiant ou votre email pour réinitialiser votre mot de passe.");

        dialog.showAndWait().ifPresent(input -> {
            if (input.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un identifiant ou un email.");
                return;
            }

            User user = userService.getUserByIdentifiantOrEmail(input);

            if (user != null) {
                String resetLink = generateResetLink(user);

                // ✅ Utilisation du service EmailService
                userService.sendResetEmail(user, resetLink);


                showAlert(Alert.AlertType.INFORMATION, "Réinitialisation du mot de passe",
                        "Un email vous a été envoyé pour réinitialiser votre mot de passe.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun utilisateur trouvé avec cet identifiant ou email.");
            }
        });
    }


    private String generateResetLink(User user) {
        // Générer un lien de réinitialisation (par exemple, en utilisant un token ou un ID utilisateur)
        // Ici, on génère juste un lien fictif pour l'exemple
        return "https://votreapp.com/reset-password?token=" + user.getIdentifiant();
    }

    private void sendResetEmail(User user, String resetLink) {
        // Utilisez JavaMail ou une autre solution pour envoyer un email à l'utilisateur avec le lien de réinitialisation
        // Exemple d'utilisation avec JavaMail (voir documentation pour l'implémentation complète)
        String to = user.getGmail();
        String subject = "Réinitialisation de votre mot de passe";
        String content = "Cliquez sur le lien suivant pour réinitialiser votre mot de passe : " + resetLink;

        // Code d'envoi d'email ici avec JavaMail ou une autre API
    }






}

