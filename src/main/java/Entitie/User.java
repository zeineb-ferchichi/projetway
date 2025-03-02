package Entitie;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;


public class User {
    private int Id = 0;
    private String Nom;
    private String Prenom;
    private String Gmail;
    private String Identifiant;
    private String Role;
    private String Motdepasse;
    private String Image;
    private String ban = "false";
    private String code;
    private StringProperty nom = new SimpleStringProperty();
    private StringProperty image = new SimpleStringProperty();

    public static User currentUser;

    // Constructeur par défaut
    public User() {
        this.Id = 0;
        this.ban = "false"; // Valeur par défaut
    }

    // Constructeur avec paramètres
    public User(String nom, String prenom, String gmail, String identifiant, String role, String motdepasse, String image, String code) {
        this(); // Appelle le constructeur par défaut
        this.Nom = nom;
        this.Prenom = prenom;
        this.Gmail = gmail;
        this.Identifiant = identifiant;
        this.Role = role;
        this.Motdepasse = motdepasse;
        this.Image = image;
        this.code = code;
    }

    // Getters et Setters pour ban
    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }

    // Getters et Setters pour les autres attributs
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
    public StringProperty NomProperty() { return nom; }

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
    public StringProperty ImagePathProperty() { return image; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Mise à jour du toString pour afficher l'attribut code
    @Override
    public String toString() {
        return "User{id=" + Id + ", nom='" + Nom + "', prenom='" + Prenom + "', gmail='" + Gmail +
                "', identifiant='" + Identifiant + "', role='" + Role + "', motdepasse='" + Motdepasse +
                "', image='" + Image + "', ban='" + ban + "', code='" + code + "'}";
    }
}
