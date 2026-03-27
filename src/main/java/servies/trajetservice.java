package servies;
import entities.trajet;
import util.Datasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrajetService implements service<trajet> {

    @Override
    public void add(trajet t) {
        String sql = "INSERT INTO trajet (id_voyage, type_transport, compagnie, cout, ville_depart, ville_arrivee) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Datasource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, t.getId_voyage());
            pstmt.setString(2, t.getType_transport());
            pstmt.setString(3, t.getCompagnie());
            pstmt.setDouble(4, t.getCout());
            pstmt.setString(5, t.getVille_depart());
            pstmt.setString(6, t.getVille_arrivee());

            pstmt.executeUpdate();
            System.out.println("✅ Trajet ajouté !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du trajet : " + e.getMessage());
        }
    }

    @Override
    public void update(trajet t) {
        String sql = "UPDATE trajet SET type_transport=?, compagnie=?, cout=?, ville_depart=?, ville_arrivee=? WHERE id_trajet=?";

        try (Connection conn = Datasource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, t.getType_transport());
            pstmt.setString(2, t.getCompagnie());
            pstmt.setDouble(3, t.getCout());
            pstmt.setString(4, t.getVille_depart());
            pstmt.setString(5, t.getVille_arrivee());
            pstmt.setInt(6, t.getId_trajet());

            pstmt.executeUpdate();
            System.out.println("✅ Trajet mis à jour !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour du trajet : " + e.getMessage());
        }
    }

    @Override
    public void delete(int idTrajet) {
        String sql = "DELETE FROM trajet WHERE id_trajet=?";

        try (Connection conn = Datasource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idTrajet);
            pstmt.executeUpdate();
            System.out.println("✅ Trajet supprimé !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression du trajet : " + e.getMessage());
        }
    }

    @Override
    public List<trajet> getAll() {
        List<trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet";

        try (Connection conn = Datasource.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                trajets.add(new trajet(
                        rs.getInt("id_trajet"),
                        rs.getInt("id_voyage"),
                        rs.getString("type_transport"),
                        rs.getString("compagnie"),
                        rs.getDouble("cout"),
                        rs.getString("ville_depart"),
                        rs.getString("ville_arrivee")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des trajets : " + e.getMessage());
        }
        return trajets;
    }
}
