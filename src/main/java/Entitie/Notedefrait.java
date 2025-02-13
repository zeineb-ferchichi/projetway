package Entitie;

public class Notedefrait {



        private int Id;
        private String Nomactivite;
        private String Description;
        private String Lienfacture;


        // Constructeur par défaut
        public Notedefrait( String Nomactivite, String Description, String Lienfacture) {
        }

        // Constructeur avec tous les attributs
        public Notedefrait( int id ,String Nomactivite, String Description , String Lienfacture) {
            this.Id = id;
            this.Nomactivite = Nomactivite;
            this.Description = Description;
            this.Lienfacture = Lienfacture;

        }





        // Getters et Setters
        public int getId() {
            return Id;
        }

        public void setId(int id) {
            this.Id = id;
        }



        public String getNomactivite() {  // Correction du getter
            return Nomactivite;
        }

        public void setNomactivite(String Nomactivite) {  // Correction du setter
            this.Nomactivite = Nomactivite;
        }
        public String getDescription() {
            return Description;
        }
        public void setDescription(String Description) {
            this.Description= Description;
        }

        public String getLienfacture() {
            return Lienfacture;
        }

        public void setLienfacture(String Lienfacture) {
            this.Lienfacture= Lienfacture;
        }

        // Redéfinition de toString pour afficher les infos facilement
        @Override
        public String toString() {
            return "User{id=" + Id + ", Nomactivite='" + Nomactivite + "', Description='" + Description+ "' , Lienfacture='" + Lienfacture + "' }";
        }
    }

