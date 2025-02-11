package services;

import interfaces.GlobalInterface;
import models.Forum;
import util.MyConnection;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.ResultSet;

public class ForumService implements GlobalInterface<Forum> {
    Connection conn;

    public ForumService() {
        this.conn = MyConnection.getInstance().getCnx();
    }

    @Override
    public void add(Forum forum) {
        String SQL = "INSERT INTO forum (titre, contenu, image, datecreation) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, forum.getTitre());
            pstmt.setString(2, forum.getContenu());
            pstmt.setString(3, forum.getImage());
            pstmt.setDate(4, forum.getDateCreation());

            pstmt.executeUpdate();
            System.out.println("Post ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void update(Forum forum) {
        String SQL = "UPDATE forum SET titre = ?, contenu = ?, image = ?, datecreation = ? WHERE idForum = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, forum.getTitre());
            pstmt.setString(2, forum.getContenu());
            pstmt.setString(3, forum.getImage());
            pstmt.setDate(4, forum.getDateCreation());
            pstmt.setInt(5, forum.getIdForum());

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
        String SQL = "SELECT * FROM forum";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                int idForum = rs.getInt("idForum");
                String titre = rs.getString("titre");
                String contenu = rs.getString("contenu");
                String image = rs.getString("image");
                Date dateCreation = rs.getDate("datecreation");

                Forum forum = new Forum(idForum, titre, contenu, image, dateCreation);
                forums.add(forum);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des forums : " + e.getMessage());
        }

        return forums;
    }

    @Override
    public void delete(Forum forum) {
        String SQL = "DELETE FROM forum WHERE idForum = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, forum.getIdForum());

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
