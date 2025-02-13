package Service;

import Entitie.Notedefrait;

import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotedefraitService implements IService<Notedefrait> {





        private static Connection cnx;

        public NotedefraitService() {
            cnx = DataSource.getInstance().getConnection();
        }

        @Override
        public void insert(Notedefrait notedefrait) {
            String requete = "INSERT INTO notedefrait (Nomactivite, Description, Lienfacture) VALUES (?, ?, ? )";

            try (PreparedStatement pst = cnx.prepareStatement(requete)) {
                pst.setString(1, notedefrait.getNomactivite());
                pst.setString(2, notedefrait.getDescription());
                pst.setString(3, notedefrait.getLienfacture());


                pst.executeUpdate();
                System.out.println("note de frait inséré avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'insertion du note de frait : " + e.getMessage());
                e.printStackTrace();
            }
        }



        @Override
        public void update(Notedefrait notedefrait) {
            String requete = "UPDATE notedefrait SET Nomactivite= ?, Description= ?, Lienfacture = ? WHERE id = ?";
            try (PreparedStatement pst = cnx.prepareStatement(requete)) {
                pst.setString(1, notedefrait.getNomactivite());
                pst.setString(2, notedefrait.getDescription());
                pst.setString(3, notedefrait.getLienfacture());
                pst.setInt(4, notedefrait.getId()); // L'ID doit être le dernier paramètre (WHERE id = ?)

                pst.executeUpdate();
                System.out.println("Note de frait mis à jour avec succès !");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }




        @Override
        public List<Notedefrait> getAll() {
            List<Notedefrait> listeNotedefrait = new ArrayList<>();
            String requete = "SELECT * FROM notedefrait";
            try (Statement ste = cnx.createStatement();
                 ResultSet rs = ste.executeQuery(requete)) {
                while (rs.next()) {
                    Notedefrait p = new Notedefrait(
                            rs.getInt("Id"),
                            rs.getString("Nomactivite"),
                            rs.getString("Description"),
                            rs.getString("Lienfacture")


                    );
                    listeNotedefrait.add(p);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listeNotedefrait;
        }

        @Override
        public Notedefrait getById(int id) {
            String requete = "SELECT * FROM notedefrait WHERE id = ?";
            try (PreparedStatement pst = cnx.prepareStatement(requete)) {
                pst.setInt(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        return new Notedefrait(
                                rs.getInt("Id"),
                                rs.getString("Nomactivite"),
                                rs.getString("Description"),
                                rs.getString("Lienfacture")
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public List<Notedefrait> readAll() throws SQLException {
            List<Notedefrait> listeNotedefrait = new ArrayList<>();
            String requete = "SELECT * FROM notedefrait";
            try (Statement ste = cnx.createStatement();
                 ResultSet rs = ste.executeQuery(requete)) {
                while (rs.next()) {
                    Notedefrait p = new Notedefrait(
                            rs.getInt("Id"),
                            rs.getString("Nomactivite"),
                            rs.getString("Description"),
                            rs.getString("Lienfacture")
                    );
                    listeNotedefrait.add(p);
                }
            }
            return listeNotedefrait;
        }
        @Override
        public void deleteById(int id) {
            String requete = "DELETE FROM notedefrait WHERE id = ?";
            try (PreparedStatement pst = cnx.prepareStatement(requete , Statement.RETURN_GENERATED_KEYS)) {
                pst.setInt(1, id);
                ResultSet rs = pst.getGeneratedKeys();

                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Note de frait avec l'ID " + id + " supprimé avec succès !");
                } else {
                    System.out.println("Aucun Note de frait trouvé avec l'ID " + id + ".");
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
        String requete = "SELECT * FROM notedefrait WHERE lienfacture = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, lienfacture);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Retourne vrai si le lien n'existe pas
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




}

