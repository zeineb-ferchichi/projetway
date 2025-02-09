package models;
import java.sql.Date;

public class Forum {
    private int id;
    private String titre;
    private String contenu;
    private String imageUrl;
    private Date dateCreation;

    // Constructeurs
    public Forum() {}

    public Forum(int id, String titre, String contenu, String imageUrl, Date dateCreation) {
        this.id = id;
        this.titre = titre;
        this.contenu = contenu;
        this.imageUrl = imageUrl;
        this.dateCreation = dateCreation;
    }

    public Forum(String titre, String contenu, String imageUrl, Date dateCreation) {
        this.titre = titre;
        this.contenu = contenu;
        this.imageUrl = imageUrl;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", contenu='" + contenu + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", dateCreation=" + dateCreation +
                '}';
    }

    public String getImage() {
        return "http://example.com/image.jpg";  // Retournez une valeur de type String
    }

}

