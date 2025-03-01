package util;

import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.time.LocalDate;

public class InputValidation {

    public static boolean validateTextField(TextField textField, String fieldName) {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", fieldName + " ne peut pas être vide.");
            return false;
        }
        if (!text.matches("[a-zA-ZÀ-ÿ\\s]+")) { // Permet uniquement les lettres et espaces
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", fieldName + " ne doit contenir que des lettres.");
            return false;
        }
        return true;
    }

    public static boolean validateNumericField(TextField textField, String fieldName) {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", fieldName + " ne peut pas être vide.");
            return false;
        }
        if (!text.matches("\\d+")) { // Permet uniquement les nombres
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", fieldName + " doit être un nombre valide.");
            return false;
        }
        return true;
    }

    public static boolean validateDateField(DatePicker datePicker, String fieldName) {
        if (datePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", fieldName + " ne peut pas être vide.");
            return false;
        }
        return true;
    }

    public static boolean validateDateOrder(DatePicker startDate, DatePicker endDate) {
        if (startDate.getValue() != null && endDate.getValue() != null) {
            if (startDate.getValue().isAfter(endDate.getValue())) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "La date de début doit être avant la date de fin.");
                return false;
            }
        }
        return true;
    }
    public static boolean validateNomRapport(TextField textField) {
        String text = textField.getText().trim();

        if (text.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom du rapport ne peut pas être vide.");
            return false;
        }

        if (!text.matches("^[a-zA-ZÀ-ÿ\\s]+$")) { // Seuls les lettres et espaces sont autorisés
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom du rapport ne doit contenir que des lettres.");
            return false;
        }

        return true;
    }

    private static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
