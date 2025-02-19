package tn.esprit.services;

import tn.esprit.models.Reservation;
import tn.esprit.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IService<Reservation> {
    private final Connection conn;

    public ReservationService() {
        this.conn = DBConnection.getInstance().getConn();
    }

    @Override
    public void add(Reservation reservation) {
        String SQL = "INSERT INTO Reservation (clientName, dateDebut, dateFin, hebergement_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, reservation.getClientName());
            pstmt.setDate(2, new java.sql.Date(reservation.getDateDebut().getTime()));
            pstmt.setDate(3, new java.sql.Date(reservation.getDateFin().getTime()));
            pstmt.setInt(4, reservation.getHebergementId());

            pstmt.executeUpdate();
            System.out.println("Reservation ajoutée avec succès !");

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réservation : " + e.getMessage());
        }
    }

    @Override
    public void update(Reservation reservation) {
        String SQL = "UPDATE Reservation SET clientName = ?, dateDebut = ?, dateFin = ?, hebergement_id = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, reservation.getClientName());
            pstmt.setDate(2, new java.sql.Date(reservation.getDateDebut().getTime()));
            pstmt.setDate(3, new java.sql.Date(reservation.getDateFin().getTime()));
            pstmt.setInt(4, reservation.getHebergementId());
            pstmt.setInt(5, reservation.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Réservation mise à jour avec succès !");
            } else {
                System.out.println("Aucune réservation trouvée avec l'ID donné.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la réservation : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM Reservation WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, id);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Réservation supprimée avec succès !");
            } else {
                System.out.println("Aucune réservation trouvée avec l'ID donné.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }

    @Override
    public List<Reservation> getAll() {
        String SQL = "SELECT * FROM Reservation";
        List<Reservation> reservations = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setClientName(rs.getString("clientName"));
                r.setDateDebut(rs.getDate("dateDebut"));
                r.setDateFin(rs.getDate("dateFin"));
                r.setHebergementId(rs.getInt("hebergement_id"));

                reservations.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }
        return reservations;
    }
}
