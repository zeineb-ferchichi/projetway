package Entitie;

public class User {
    private int Id;
    private String Nom;
    private String Prenom;
    private String Gmail;
    private String Identifiant;
    private String Role;
    private String Motdepasse;
    private String Image;


    public static User currentUser;
    // Constructeur par défaut
    public User(String nom, String prenom, String gmail, String identifiant, String role, String motdepasse, String image) {
        this.Nom = nom;
        this.Prenom = prenom;
        this.Gmail = gmail;
        this.Identifiant = identifiant;
        this.Role = role;
        this.Motdepasse = motdepasse;
        this.Image = image;
    }

    // Constructeur avec tous les attributs
    public User(int id, String nom, String prenom, String gmail, String identifiant, String role, String motdepasse, String image) {
        this.Id = id;
        this.Nom = nom;
        this.Prenom = prenom;
        this.Gmail = gmail;
        this.Identifiant = identifiant;
        this.Role = role;
        this.Motdepasse = motdepasse;
        this.Image = image;
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

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Redéfinition de toString pour afficher les infos facilement
    @Override
    public String toString() {
        return "User{id=" + Id + ", nom='" + Nom + "', prenom='" + Prenom + "', gmail='" + Gmail + "', identifiant='" + Identifiant + "', role='" + Role + "', motdepasse='" + Motdepasse + "', image='" + Image + "'}";
    }
}
