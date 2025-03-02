package Service;

import Entitie.User;
import javafx.scene.control.Alert;
import util.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class UserService implements IService<User> {

    private static Connection cnx;

    public UserService() {
        cnx = DataSource.getInstance().getConnection();
    }


    @Override
    public void insert(User user) {
        if (!validateUser(user)) {
            return;
        }
        String requete = "INSERT INTO user (Nom, Prenom, Gmail, Identifiant, Role, Motdepasse, Image, Code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getGmail());
            pst.setString(4, user.getIdentifiant());
            pst.setString(5, user.getRole());
            pst.setString(6, user.getMotdepasse());
            pst.setString(7, user.getImage());
            pst.setString(8, user.getCode()); // Ajout du code
            pst.executeUpdate();
            System.out.println("User inséré avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(User user) {
        if (!validateUser(user)) {
            return;
        }
        String requete = "UPDATE user SET Nom = ?, Prenom = ?, Gmail = ?, Identifiant = ?, Role = ?, Motdepasse = ?, Image = ?, ban = ?, Code = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getGmail());
            pst.setString(4, user.getIdentifiant());
            pst.setString(5, user.getRole());
            pst.setString(6, user.getMotdepasse());
            pst.setString(7, user.getImage());
            pst.setString(8, user.getBan());
            pst.setString(9, user.getCode()); // Ajout du code
            pst.setInt(10, user.getId());
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
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Gmail"),
                        rs.getString("Identifiant"),
                        rs.getString("Role"),
                        rs.getString("Motdepasse"),
                        rs.getString("Image"),
                        rs.getString("Code")


                );
                p.setId(rs.getInt("id"));
                p.setBan(rs.getString("ban"));
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
                            rs.getString("Nom"),
                            rs.getString("Prenom"),
                            rs.getString("Gmail"),
                            rs.getString("Identifiant"),
                            rs.getString("Role"),
                            rs.getString("Motdepasse"),
                            rs.getString("Image"),
                            rs.getString("Code")
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

                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Gmail"),
                        rs.getString("Identifiant"),
                        rs.getString("Role"),
                        rs.getString("Motdepasse"),
                        rs.getString("Image"),
                        rs.getString("Code")
                );
                listeUsers.add(p);
            }
        }
        return listeUsers;
    }
    @Override
    public void deleteById(int id) {
        String requete = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, id);
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
            afficherAlerte("Erreur de validation", "Le nom est obligatoire !");
            return false;
        }
        if (!user.getNom().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            afficherAlerte("Erreur de validation", "Le nom ne doit contenir que des lettres et des espaces !");
            return false;
        }

        // Vérifier que le prénom est rempli et ne contient que des lettres et espaces
        if (user.getPrenom() == null || user.getPrenom().trim().isEmpty()) {
            afficherAlerte("Erreur de validation", "Le prénom est obligatoire !");
            return false;
        }
        if (!user.getPrenom().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            afficherAlerte("Erreur de validation", "Le prénom ne doit contenir que des lettres et des espaces !");
            return false;
        }

        // Vérifier que l'email est valide
        if (user.getGmail() == null || !user.getGmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            afficherAlerte("Erreur de validation", "L'adresse email est invalide !");
            return false;
        }

        // Vérifier que l'identifiant est rempli
        if (user.getIdentifiant() == null || user.getIdentifiant().trim().isEmpty()) {
            afficherAlerte("Erreur de validation", "L'identifiant est obligatoire !");
            return false;
        }

        if (!isUniqueIdentifiant(user.getIdentifiant(), user.getId())) {
            afficherAlerte("Erreur de validation", "Cet identifiant est déjà utilisé !");
            return false;
        }

        if (!isUniqueCode(user.getCode() , user.getId())) {
            afficherAlerte("Erreur de validation", "Cet code est déjà utilisé !");
            return false;
        }


        // Vérifier que le rôle est valide
        if (user.getRole() == null || !(user.getRole().equalsIgnoreCase("directeur") || user.getRole().equalsIgnoreCase("employe") || user.getRole().equalsIgnoreCase("admin"))) {
            afficherAlerte("Erreur de validation", "Le rôle doit être 'directeur' ou 'employe' ou 'admin'  !");
            return false;
        }

        // Vérifier que le mot de passe contient au moins 6 caractères
        if (user.getMotdepasse() == null || user.getMotdepasse().length() < 6) {
            afficherAlerte("Erreur de validation", "Le mot de passe doit contenir au moins 6 caractères !");
            return false;
        }

        // Vérifier que l'image est valide
        if (user.getImage() == null || !user.getImage().matches(".*\\.(jpg|jpeg|png|gif)$")) {
            afficherAlerte("Erreur de validation", "L'image doit être au format JPG, JPEG, PNG ou GIF !");
            return false;
        }

        return true;
    }


    public boolean isUniqueIdentifiant(String identifiant, int userId) {
        String requete = "SELECT COUNT(*) FROM user WHERE Identifiant = ? AND Id != ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, identifiant);
            pst.setInt(2, userId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Retourne vrai si aucun autre utilisateur n'a cet identifiant
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // En cas d'erreur, considère que l'identifiant n'est pas unique
    }


    public boolean isUniqueCode(String generatedCode, int userId) {
        String sql = "SELECT COUNT(*) FROM user WHERE code = ? AND Id != ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, generatedCode);
            stmt.setInt(2, userId);  // Exclude the user with the given ID
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // If no rows are returned, the code is unique
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if there's an error or the code isn't unique
    }







    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean validateCredentials(String identifiant, String password) {
        String requete = "SELECT COUNT(*) FROM user WHERE Identifiant = ? AND MotDePasse = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, identifiant);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retourne vrai si un compte existe avec ces identifiants
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // En cas d'erreur, retour faux
    }


    public User getUserByIdentifiant(String identifiant) {
        String requete = "SELECT * FROM user WHERE Identifiant = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, identifiant);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("Nom"),
                            rs.getString("Prenom"),
                            rs.getString("Gmail"),
                            rs.getString("Identifiant"),
                            rs.getString("Role"),
                            rs.getString("Motdepasse"),
                            rs.getString("Image"),
                            rs.getString("Code")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }






    public void banUser(String Identifiant) {
        String requete = "UPDATE user SET ban = ? WHERE Identifiant = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, "true");
            pst.setString(2, Identifiant);
            pst.executeUpdate();
            System.out.println("Utilisateur banni avec succès !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public User getUserByIdentifiantOrEmail(String identifierOrEmail) {
        String query = "SELECT Identifiant, Gmail, Code FROM user WHERE Identifiant = ? OR Gmail = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, identifierOrEmail);
            stmt.setString(2, identifierOrEmail);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setIdentifiant(rs.getString("identifiant"));
                user.setGmail(rs.getString("gmail"));
                user.setCode(rs.getString("code")); // Set the reset code
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }





    public void sendResetEmail(String to, String subject, String content) {
        final String fromEmail = "mouhamarzoukk70@gmail.com";
        final String appPassword = "yuha qqwo qoun yvrq"; // Assure-toi que ce mot de passe est valide

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587"); // Port 587 pour STARTTLS
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Active STARTTLS

        // Création d'une session authentifiée
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("Email envoyé avec succès à : " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public boolean updatePasswordAndCode(String userIdentifiant, String newPassword, String randomCode) {
        String sql = "UPDATE user SET MotDePasse = ?, Code = ? WHERE Identifiant = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, newPassword); // Set the hashed password
            stmt.setString(2, randomCode);  // Set the new random code
            stmt.setString(3, userIdentifiant); // Set the user identifier

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Return true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurs
        }
    }











}