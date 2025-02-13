
package Entitie;

public class User {
    private int Id;
    private String Nom;
    private String Prenom;
    private String Gmail;
    private String Identifiant;
    private String Role;
    private String Motdepasse;

    // Constructeur par défaut
    public User( String nom, String prenom, String Gmail, String identifiant, String role, String motdepasse) {
    }

    // Constructeur avec tous les attributs
    public User( int id ,String nom, String prenom , String gmail, String identifiant, String role, String motdepasse) {
        this.Id = id;
        this.Nom = nom;
        this.Prenom = prenom;
        this.Gmail = gmail;
        this.Identifiant = identifiant;
        this.Role = role;
        this.Motdepasse = motdepasse;
    }





    // Getters et Setters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        this.Prenom = prenom;
    }

    public String getNom() {  // Correction du getter
        return Nom;
    }

    public void setNom(String nom) {  // Correction du setter
        this.Nom = nom;
    }
    public String getGmail() {
        return Gmail;
    }
    public void setGmail(String gmail) {
        this.Gmail = gmail;
    }

    public String getIdentifiant() {
        return Identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.Identifiant = identifiant;
    }
    public String getRole() {
        return Role;
    }
    public void setRole(String role) {
        this.Role = role;
    }
    public String getMotdepasse() {
        return Motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.Motdepasse = motdepasse;
    }
    // Redéfinition de toString pour afficher les infos facilement
    @Override
    public String toString() {
        return "User{id=" + Id + ", nom='" + Nom + "', prenom='" + Prenom + "' , gmail='" + Gmail + "' , identifiant='" + Identifiant + "' , role='" + Role + "' , motdepasse='" + Motdepasse+ "'}";
    }
}