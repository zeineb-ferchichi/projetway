package gui;

import Entitie.User;
import Service.UserService;
import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.util.Random;

public class ForgetPasswordController {
    @FXML
    private TextField TFCode;
    @FXML
    private PasswordField TFMotDePasse;
    @FXML
    private PasswordField TFConfirmMotDePasse;
    private User user;
    private UserService userService;

    @FXML
    private Button btnValidateCode;
    @FXML
    private Button btnResetPassword;


    public ForgetPasswordController() {
    }

    // Setter for userService
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void validerCode(ActionEvent event) {
        String codeEntre = TFCode.getText().trim();
        System.out.println("Code entered: " + codeEntre); // Debugging print

        if (user != null && user.getCode() != null) {
            System.out.println("Code from user: " + user.getCode()); // Debugging print
            if (codeEntre.equals(user.getCode())) {
                // Code correct, afficher le champ de nouveau mot de passe
                TFMotDePasse.setDisable(false);
                TFConfirmMotDePasse.setDisable(false);
                btnResetPassword.setDisable(false);
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Code incorrect !");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Code utilisateur non trouvé !");
        }
    }


    @FXML
    private void resetPassword(ActionEvent event) {
        String newPassword = TFMotDePasse.getText().trim();
        String confirmPassword = TFConfirmMotDePasse.getText().trim();

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas !");
            return;
        }

        // Generate a random code and ensure it is unique
        String generatedCode;
        do {
            generatedCode = generateRandomCode(); // Generate a random code
        } while (!userService.isUniqueCode(generatedCode, user.getId())); // Pass the user ID

        // Hash the password before updating
        String hashedPassword = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());

        // Update the password and the code in the database
        boolean isUpdated = userService.updatePasswordAndCode(user.getIdentifiant(), hashedPassword, generatedCode);

        if (isUpdated) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mot de passe réinitialisé avec succès !");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la réinitialisation du mot de passe.");
        }

        // Close the current stage (reset password window)
        Stage stage = (Stage) TFCode.getScene().getWindow();
        stage.close();
    }


    private String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generate a 6-digit code
        return String.valueOf(code);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
