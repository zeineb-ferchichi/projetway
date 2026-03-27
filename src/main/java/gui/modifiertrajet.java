package gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.Datasource;

public class modifiertrajet {
    public static boolean updateTrajet(int trajetId, int idVoyage, String typeTransport, String compagnie, String numero, String dateDepart, String dateArrivee, String villeDepart, String villeArrivee, double cout) {
        Connection conn = Datasource.getInstance().getConnection();
        if (conn == null) {
            System.out.println("❌ Database connection failed!");
            return false;
        }

        String query = "UPDATE trajet SET type_transport = ?, compagnie = ?, numero = ?, date_depart = ?, date_arrivee = ?, ville_depart = ?, ville_arrivee = ?, cout = ? WHERE trajet_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, typeTransport);
            pstmt.setString(2, compagnie);
            pstmt.setString(3, numero);
            pstmt.setString(4, dateDepart);
            pstmt.setString(5, dateArrivee);
            pstmt.setString(6, villeDepart);
            pstmt.setString(7, villeArrivee);
            pstmt.setDouble(8, cout);
            pstmt.setInt(9, trajetId);

            // Debugging
            System.out.println("🔹 Executing SQL: " + query);
            System.out.println("🔹 With values: [" + typeTransport + ", " + compagnie + ", " + numero + ", " + dateDepart + ", " + dateArrivee + ", " + villeDepart + ", " + villeArrivee + ", " + cout + ", " + trajetId + "]");

            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("✅ Rows updated: " + rowsUpdated);

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}