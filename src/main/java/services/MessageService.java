package services;

import interfaces.GlobalInterface;
import models.Message;
import util.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageService implements GlobalInterface<Message> {
    private Connection conn;

    // Constructeur de la classe qui initialise la connexion
    public MessageService() {
        this.conn = MyConnection.getInstance().getCnx();
    }

    @Override
    public void add(Message message) {
        // Vérification de l'ID du forum avant d'ajouter le message
        if (message.getIdforum() == 0) {
            System.out.println("Erreur: L'ID du forum ne peut pas être nul ou 0.");
            return;  // Ne pas ajouter le message si l'ID du forum est invalide.
        }

        String SQL = "INSERT INTO message (contenu, dateEnvoi, idforum) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, message.getContenu());
            pstmt.setDate(2, message.getDateEnvoi());
            pstmt.setInt(3, message.getIdforum());  // Assurez-vous de définir l'ID du forum.

            pstmt.executeUpdate();
            System.out.println("Message ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du message : " + e.getMessage());
        }
    }

    @Override
    public void update(Message message) {
        // Vérification de l'ID du message avant de mettre à jour
        if (message.getIdmessage() == 0) {
            System.out.println("Erreur: L'ID du message ne peut pas être nul ou 0.");
            return;  // Ne pas mettre à jour si l'ID du message est invalide.
        }

        String SQL = "UPDATE message SET contenu = ?, dateEnvoi = ?, idforum = ? WHERE idmessage = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, message.getContenu());
            pstmt.setDate(2, message.getDateEnvoi());
            pstmt.setInt(3, message.getIdforum());  // Utilise l'ID du forum tel quel.
            pstmt.setInt(4, message.getIdmessage());  // Utilise l'ID du message pour identifier le message à mettre à jour.

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Message mis à jour avec succès !");
            } else {
                System.out.println("Aucun message trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du message : " + e.getMessage());
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
        // Vérification avant de supprimer un message
        if (message.getIdmessage() == 0) {
            System.out.println("Erreur: L'ID du message ne peut pas être nul ou 0.");
            return;  // Ne pas supprimer le message si l'ID est invalide.
        }

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
            System.out.println("Erreur lors de la suppression du message : " + e.getMessage());
        }
    }

    // Méthode pour récupérer un message par ID
    public Message getById(int id) {
        Message message = null;
        try {
            String query = "SELECT * FROM message WHERE idmessage = ?";
            PreparedStatement stmt = conn.prepareStatement(query);  // Utilisation de 'conn' ici
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                message = new Message(
                        rs.getInt("idmessage"),
                        rs.getString("contenu"),
                        rs.getDate("dateEnvoi"),  // Assurez-vous que le nom de colonne correspond à votre schéma DB
                        rs.getInt("idforum")  // Assurez-vous que le nom de colonne correspond à votre schéma DB
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }
    public List<Message> getAllByForumId(int forumId) {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM message WHERE idforum = ?";

        if (conn == null) {
            System.out.println("Erreur : Connexion à la base de données non initialisée.");
            return messages;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, forumId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Message msg = new Message();
                    msg.setIdmessage(rs.getInt("idmessage"));
                    msg.setContenu(rs.getString("contenu"));
                    msg.setDateEnvoi(rs.getDate("dateEnvoi"));
                    msg.setIdforum(rs.getInt("idforum"));

                    messages.add(msg);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des messages : " + e.getMessage());
            e.printStackTrace();
        }

        return messages;
    }

}
