package services;

import interfaces.GlobalInterface;
import models.Forum;
import util.MyConnection;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Connection;
import  java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.sql.ResultSet;

public class ForumService implements GlobalInterface<Forum> {
    Connection conn;

    public ForumService() {
        this.conn = MyConnection.getInstance().getCnx();
    }

    @Override
    public void add(Forum forum) {
        String SQL = "INSERT INTO forum (titre, contenu, image, datecreation) VALUES ('" +
                forum.getTitre() + "', '" + forum.getContenu() + "', '" +
                forum.getImage() + "', '" + forum.getDateCreation() + "')";

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
            System.out.println("Post ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }


    @Override
    public void update(Forum forum) {
        // Définition de la requête SQL pour la mise à jour d'un forum
        String SQL = "UPDATE forum SET titre = ?, contenu = ?, image = ?, datecreation = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            // Associez les valeurs du forum aux paramètres de la requête
            pstmt.setString(1, forum.getTitre());
            pstmt.setString(2, forum.getContenu());
            pstmt.setString(3, forum.getImage());  // Utilise la méthode getImage() de Forum pour l'URL de l'image
            pstmt.setDate(4, forum.getDateCreation());
            pstmt.setInt(5, forum.getId());  // Utilise l'ID pour identifier le forum à mettre à jour

            // Exécuter la mise à jour
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Forum mis à jour avec succès !");
            } else {
                System.out.println("Aucun forum trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }


    @Override
    public List<Forum> getAll() {
        List<Forum> forums = new ArrayList<>();
        String SQL = "SELECT * FROM forum";  // La requête SQL pour obtenir tous les forums

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            // Parcourir les résultats de la requête
            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String contenu = rs.getString("contenu");
                String image = rs.getString("image");
                Date dateCreation = rs.getDate("datecreation");

                // Créer un objet Forum avec les données récupérées et l'ajouter à la liste
                Forum forum = new Forum(id, titre, contenu, image, dateCreation);
                forums.add(forum);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des forums : " + e.getMessage());
        }

        return forums;  // Retourner la liste des forums
    }


    @Override
    public void delete(Forum forum) {
        // Définition de la requête SQL pour la suppression d'un forum
        String SQL = "DELETE FROM forum WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            // Associer l'ID du forum à supprimer
            pstmt.setInt(1, forum.getId());

            // Exécuter la suppression
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Forum supprimé avec succès !");
            } else {
                System.out.println("Aucun forum trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

}
