package services;

import interfaces.GlobalInterface;
import models.Message;
import util.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageService implements GlobalInterface<Message> {
    Connection conn;

    public MessageService() {
        this.conn = MyConnection.getInstance().getCnx();
    }

    @Override
    public void add(Message message) {
        String SQL = "INSERT INTO message (contenu, dateEnvoi, idforum) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, message.getContenu());
            pstmt.setDate(2, message.getDateEnvoi());

            if (message.getIdforum() != 0) {
                pstmt.setInt(3, message.getIdforum());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            pstmt.executeUpdate();
            System.out.println("Message ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void update(Message message) {
        boolean hasIdForum = (message.getIdforum() != 0);
        String SQL = hasIdForum
                ? "UPDATE message SET contenu = ?, dateEnvoi = ?, idforum = ? WHERE idmessage = ?"
                : "UPDATE message SET contenu = ?, dateEnvoi = ? WHERE idmessage = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, message.getContenu());
            pstmt.setDate(2, message.getDateEnvoi());

            if (hasIdForum) {
                pstmt.setInt(3, message.getIdforum());
                pstmt.setInt(4, message.getIdmessage());
            } else {
                pstmt.setInt(3, message.getIdmessage());
            }

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Message mis à jour avec succès !");
            } else {
                System.out.println("Aucun message trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        String SQL = "SELECT * FROM message";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            while (rs.next()) {
                int idmessage = rs.getInt("idmessage");
                String contenu = rs.getString("contenu");
                Date dateEnvoi = rs.getDate("dateEnvoi");
                int idforum = rs.getInt("idforum");

                Message message = new Message(idmessage, contenu, dateEnvoi, idforum);
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des messages : " + e.getMessage());
        }

        return messages;
    }

    @Override
    public void delete(Message message) {
        String SQL = "DELETE FROM message WHERE idmessage = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, message.getIdmessage());

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Message supprimé avec succès !");
            } else {
                System.out.println("Aucun message trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }
}
