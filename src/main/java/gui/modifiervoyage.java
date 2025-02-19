package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.Datasource;

public class modifiervoyage {

    @FXML
    private TextField voyageIdField;  // Used to identify the voyage (optional if ID is auto-increment)
    @FXML
    private TextField destinationField;
    @FXML
    private TextField departureDateField;
    @FXML
    private TextField dateRetourField;

    public static boolean updateVoyage(int idvoyage, String newDestination, String newDepartureDate, String newDateRetour) {
        Connection conn = Datasource.getInstance().getConnection();
        if (conn == null) {
            System.out.println("❌ Database connection failed!");
            return false;
        }

        String query = "UPDATE voyage SET destination = ?, date_depart = ?, date_retour = ? WHERE idvoyage = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newDestination);
            pstmt.setString(2, newDepartureDate);
            pstmt.setString(3, newDateRetour);
            pstmt.setInt(4, idvoyage);

            // Debugging
            System.out.println("🔹 Executing SQL: " + query);
            System.out.println("🔹 With values: [" + newDestination + ", " + newDepartureDate + ", " + newDateRetour + ", " + idvoyage + "]");

            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("✅ Rows updated: " + rowsUpdated);

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }}
