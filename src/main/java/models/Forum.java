package models;

import java.sql.Date;

public class Forum {
    private int idForum; // Modification de "id" en "idForum"
    private String titre;
    private String contenu;
    private String image;
    private Date dateCreation;

    // Constructeurs
    public Forum() {}

    public Forum(int idForum, String titre, String contenu, String image, Date dateCreation) {
        this.idForum = idForum;
        this.titre = titre;
        this.contenu = contenu;
        this.image = image;
        this.dateCreation = dateCreation;
    }

    public Forum(String titre, String contenu, String image, Date dateCreation) {
        this.titre = titre;
        this.contenu = contenu;
        this.image = image;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public int getIdForum() {
        return idForum;
    }

    public void setIdForum(int idForum) {
        this.idForum = idForum;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
                "idForum=" + idForum +
                ", titre='" + titre + '\'' +
                ", contenu='" + contenu + '\'' +
                ", image='" + image + '\'' +
                ", dateCreation=" + dateCreation +
                '}';
    }
}
