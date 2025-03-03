
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datasource {
    private String url="jdbc:mysql://localhost:3306/projetip1";
    private String username="root";
    private String password="";
    private Connection conn;
    private static Datasource instance;
    private Datasource() {
        try {
            conn = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Datasource getInstance(){
        if(instance==null){
            instance=new Datasource();}
        return instance;
    }
    public Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("🔄 Réouverture de la connexion...");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetip1", "root", "");
            } else {
                System.out.println("✅ Connexion active !");
            }
        } catch (SQLException e) {
            System.err.println("❌ Impossible de rouvrir la connexion : " + e.getMessage());
        }
        return conn;
    }

}