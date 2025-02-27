package Service;

import Entitie.Notedefrait;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotedefraitService implements IService<Notedefrait> {
    private static Connection cnx;

    public NotedefraitService() {
        cnx = DataSource.getInstance().getConnection();
    }
    @Override
    public void insert(Notedefrait notedefrait) {
        String query = "INSERT INTO Notedefrait (nomActivite, description, lienFacture, user_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setString(1, notedefrait.getNomactivite());
            ps.setString(2, notedefrait.getDescription());
            ps.setString(3, notedefrait.getLienfacture());
            ps.setInt(4, notedefrait.getUserId());
            ps.executeUpdate();
            System.out.println("Note de frais ajoutée avec succès pour l'utilisateur ID " + notedefrait.getUserId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Notedefrait notedefrait) {
        String query = "UPDATE Notedefrait SET nomActivite= ?, description= ?, lienFacture = ?, user_id = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, notedefrait.getNomactivite());
            pst.setString(2, notedefrait.getDescription());
            pst.setString(3, notedefrait.getLienfacture());
            pst.setInt(4, notedefrait.getUserId());
            pst.setInt(5, notedefrait.getId());

            pst.executeUpdate();
            System.out.println("Note de frais mise à jour avec succès pour l'utilisateur ID " + notedefrait.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Notedefrait> getAll() {
        List<Notedefrait> notes = new ArrayList<>();
        String query = "SELECT n.id, n.nomActivite, n.description, n.lienFacture, u.id AS userId, u.nom, u.prenom " +
                "FROM Notedefrait n JOIN User u ON n.user_id = u.id";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Notedefrait notedefrait = new Notedefrait(
                        rs.getInt("id"),
                        rs.getString("nomActivite"),
                        rs.getString("description"),
                        rs.getString("lienFacture"),
                        rs.getInt("userId")
                );
                System.out.println("Note de frais de " + rs.getString("nom") + " " + rs.getString("prenom") + " : " + notedefrait);
                notes.add(notedefrait);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }

    @Override
    public Notedefrait getById(int id) {
        String query = "SELECT * FROM Notedefrait WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Notedefrait(
                            rs.getInt("id"),
                            rs.getString("nomActivite"),
                            rs.getString("description"),
                            rs.getString("lienFacture"),
                            rs.getInt("user_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        String query = "DELETE FROM Notedefrait WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Note de frais avec l'ID " + id + " supprimée avec succès !");
            } else {
                System.out.println("Aucune note de frais trouvée avec l'ID " + id + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static boolean validateNotedefrait(Notedefrait notedefrait) {
        // Vérifier que le nom de l'activité est rempli et respecte la contrainte
        if (notedefrait.getNomactivite() == null || notedefrait.getNomactivite().trim().isEmpty()) {
            System.out.println("Erreur : Le nom de l'activité est obligatoire !");
            return false;
        }
        if (!notedefrait.getNomactivite().matches("^[a-zA-ZÀ-ÿ\\s.,]+$")) {
            System.out.println("Erreur : Le nom de l'activité ne doit contenir que des lettres, espaces, points et virgules !");
            return false;
        }

        // Vérifier que la description est remplie et respecte la contrainte
        if (notedefrait.getDescription() == null || notedefrait.getDescription().trim().isEmpty()) {
            System.out.println("Erreur : La description est obligatoire !");
            return false;
        }
        if (!notedefrait.getDescription().matches("^[a-zA-ZÀ-ÿ\\s.,]+$")) {
            System.out.println("Erreur : La description ne doit contenir que des lettres, espaces, points et virgules !");
            return false;
        }

        // Vérifier que le lien de facture est valide
        if (notedefrait.getLienfacture() == null || notedefrait.getLienfacture().trim().isEmpty()) {
            System.out.println("Erreur : Le lien de facture est obligatoire !");
            return false;
        }

        // Vérifier unicité du lien de facture
        if (!isUniqueLienFacture(notedefrait.getLienfacture())) {
            System.out.println("Erreur : Ce lien de facture existe déjà !");
            return false;
        }

        // Vérifier si c'est une URL ou un chemin de fichier valide
        if (notedefrait.getLienfacture().matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")) {
            return true;
        }
        if (notedefrait.getLienfacture().matches("^[a-zA-Z]:\\\\[^:*?\"<>|]+\\.(jpg|jpeg|png|gif|pdf)$")) {
            return true;
        }
        if (notedefrait.getLienfacture().matches("^/[^:*?\"<>|]+\\.(jpg|jpeg|png|gif|pdf)$")) {
            return true;
        }

        System.out.println("Erreur : Le lien de facture doit être une URL valide ou un chemin d'image.");
        return false;
    }


    public static boolean isUniqueLienFacture(String lienfacture) {
        String requete = "SELECT COUNT(*) FROM notedefrait WHERE lienfacture = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, lienfacture);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Retourne vrai si aucun lien n'existe
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // En cas d'erreur, mieux vaut considérer que le lien existe déjà
    }


    public List<Notedefrait> getNotesByUserId(int userId) {
        List<Notedefrait> notes = new ArrayList<>();
        String query = "SELECT * FROM Notedefrait WHERE user_id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Notedefrait notedefrait = new Notedefrait(
                            rs.getInt("id"),
                            rs.getString("nomActivite"),
                            rs.getString("description"),
                            rs.getString("lienFacture"),
                            rs.getInt("user_id")
                    );
                    notes.add(notedefrait);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }

    public List<Notedefrait> searchNotesByName(int userId, String searchText) {
        List<Notedefrait> filteredNotes = new ArrayList<>();
        String query = "SELECT * FROM notedefrait WHERE user_id = ? AND LOWER(nomactivite) LIKE LOWER(?)";

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, "%" + searchText + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notedefrait note = new Notedefrait(
                        rs.getInt("id"),
                        rs.getString("nomactivite"),
                        rs.getString("description"),
                        rs.getString("lienfacture"),
                        rs.getInt("user_id")
                );
                filteredNotes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filteredNotes;
    }









}

