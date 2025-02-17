package services;

import models.Forum;
import util.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ForumService {
    private Connection connection;

    public ForumService() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    // Ajouter un forum
    public void add(Forum forum) {
        String query = "INSERT INTO forum (titre, contenu, image, dateCreation) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, forum.getTitre());
            pst.setString(2, forum.getContenu());
            pst.setString(3, forum.getImage());
            pst.setDate(4, forum.getDateCreation());
            pst.executeUpdate();
            System.out.println("Forum ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du forum : " + e.getMessage());
        }
    }

    // Mettre à jour un forum
    // Mettre à jour un forum
    public boolean update(Forum forum) {
        String query = "UPDATE forum SET titre=?, contenu=?, image=?, dateCreation=? WHERE idForum=?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, forum.getTitre());
            pst.setString(2, forum.getContenu());
            pst.setString(3, forum.getImage());
            pst.setDate(4, forum.getDateCreation());
            pst.setInt(5, forum.getIdForum());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Forum mis à jour avec succès !");
                return true; // Retourne true si la mise à jour réussit
            } else {
                System.out.println("Aucun forum trouvé avec cet ID.");
                return false; // Retourne false si aucune ligne n'a été mise à jour
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du forum : " + e.getMessage());
            return false; // Retourne false en cas d'erreur
        }
    }


    // Supprimer un forum
    public void delete(Forum forum) {
        String query = "DELETE FROM forum WHERE idForum=?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, forum.getIdForum());

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Forum supprimé avec succès !");
            } else {
                System.out.println("Aucun forum trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du forum : " + e.getMessage());
        }
    }

    // Récupérer tous les forums
    public List<Forum> getAll() {
        List<Forum> forums = new ArrayList<>();
        String query = "SELECT * FROM forum";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Forum forum = new Forum(
                        rs.getInt("idForum"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getString("image"),
                        rs.getDate("dateCreation")
                );
                forums.add(forum);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des forums : " + e.getMessage());
        }
        return forums;
    }

    // Récupérer le dernier forum ajouté
    public Forum getLastForum() {
        String query = "SELECT * FROM forum ORDER BY idForum DESC LIMIT 1";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return new Forum(
                        rs.getInt("idForum"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getString("image"),
                        rs.getDate("dateCreation")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du dernier forum : " + e.getMessage());
        }
        return null;
    }

    // Method to get Forum by ID
    public Forum getById(int id) {
        Forum forum = null;
        String query = "SELECT * FROM forum WHERE idForum = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    forum = new Forum(
                            rs.getInt("idForum"),
                            rs.getString("titre"),
                            rs.getString("contenu"),
                            rs.getString("image"),
                            rs.getDate("dateCreation")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du forum : " + e.getMessage());
        }
        return forum;
    }
}