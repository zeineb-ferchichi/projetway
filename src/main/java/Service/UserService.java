package Service;

import Entitie.User;
import util.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    private static Connection cnx;

    public UserService() {
        cnx = DataSource.getInstance().getConnection();
    }

    @Override
    public void insert(User user) {
        String requete = "INSERT INTO user (Nom, Prenom, Gmail, Identifiant, Role, Motdepasse) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getGmail());
            pst.setString(4, user.getIdentifiant());
            pst.setString(5, user.getRole());
            pst.setString(6, user.getMotdepasse());

            pst.executeUpdate();
            System.out.println("User inséré avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du user : " + e.getMessage());
            e.printStackTrace();
        }
    }



    @Override
    public void update(User user) {
        String requete = "UPDATE user SET Nom = ?, Prenom = ?, Gmail = ?, Identifiant = ?, Role = ?, Motdepasse = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getGmail());
            pst.setString(4, user.getIdentifiant());
            pst.setString(5, user.getRole());
            pst.setString(6, user.getMotdepasse());
            pst.setInt(7, user.getId()); // L'ID doit être le dernier paramètre (WHERE id = ?)

            pst.executeUpdate();
            System.out.println("User mis à jour avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @Override
    public List<User> getAll() {
        List<User> listeUsers = new ArrayList<>();
        String requete = "SELECT * FROM user";
        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(requete)) {
            while (rs.next()) {
                User p = new User(
                        rs.getInt("Id"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Gmail"),
                        rs.getString("Identifiant"),
                        rs.getString("Role"),
                        rs.getString("Motdepasse")

                );
                listeUsers.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeUsers;
    }

    @Override
    public User getById(int id) {
        String requete = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("Id"),
                            rs.getString("Nom"),
                            rs.getString("Prenom"),
                            rs.getString("Gmail"),
                            rs.getString("Identifiant"),
                            rs.getString("Role"),
                            rs.getString("Motdepasse")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> readAll() throws SQLException {
        List<User> listeUsers = new ArrayList<>();
        String requete = "SELECT * FROM user";
        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(requete)) {
            while (rs.next()) {
                User p = new User(
                        rs.getInt("Id"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Gmail"),
                        rs.getString("Identifiant"),
                        rs.getString("Role"),
                        rs.getString("Motdepasse")
                );
                listeUsers.add(p);
            }
        }
        return listeUsers;
    }
    @Override
    public void deleteById(int id) {
        String requete = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete , Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, id);
            ResultSet rs = pst.getGeneratedKeys();
            
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Utilisateur avec l'ID " + id + " supprimé avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID " + id + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean validateUser(User user) {
        // Vérifier que le nom est rempli et ne contient que des lettres et espaces
        if (user.getNom() == null || user.getNom().trim().isEmpty()) {
            System.out.println("Erreur : Le nom est obligatoire !");
            return false;
        }
        if (!user.getNom().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            System.out.println("Erreur : Le nom ne doit contenir que des lettres et des espaces !");
            return false;
        }

        // Vérifier que le prénom est rempli et ne contient que des lettres et espaces
        if (user.getPrenom() == null || user.getPrenom().trim().isEmpty()) {
            System.out.println("Erreur : Le prénom est obligatoire !");
            return false;
        }
        if (!user.getPrenom().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            System.out.println("Erreur : Le prénom ne doit contenir que des lettres et des espaces !");
            return false;
        }

        // Vérifier que l'email est valide
        if (user.getGmail() == null || !user.getGmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            System.out.println("Erreur : L'adresse Gmail est invalide !");
            return false;
        }

        // Vérifier que l'identifiant est rempli
        if (user.getIdentifiant() == null || user.getIdentifiant().trim().isEmpty()) {
            System.out.println("Erreur : L'identifiant est obligatoire !");
            return false;
        }

        // Vérifier unicité de l'identifiant
        if (!isUniqueIdentifiant(user.getIdentifiant())) {
            System.out.println("Erreur : Cet identifiant est déjà utilisé !");
            return false;
        }

        // Vérifier que le rôle est valide
        if (user.getRole() == null || !(user.getRole().equalsIgnoreCase("directeur") || user.getRole().equalsIgnoreCase("employe"))) {
            System.out.println("Erreur : Le rôle doit être 'directeur' ou 'employé' !");
            return false;
        }

        // Vérifier que le mot de passe contient au moins 6 caractères
        if (user.getMotdepasse() == null || user.getMotdepasse().length() < 6) {
            System.out.println("Erreur : Le mot de passe doit contenir au moins 6 caractères !");
            return false;
        }

        return true;
    }



    public boolean isUniqueIdentifiant(String identifiant) {
        String requete = "SELECT * FROM user WHERE Identifiant = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, identifiant);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Retourne vrai si l'identifiant n'existe pas
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Par défaut, considérer que l'identifiant n'est pas unique en cas d'erreur
    }



}