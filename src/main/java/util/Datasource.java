
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datasource {
    private String url="jdbc:mysql://localhost:3306/projetip1";
    private String username="root";
    private String password="";
    private Connection connection;
    private static Datasource instance;
    private Datasource() {
        try {
            connection = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Datasource getInstance(){
        if(instance==null){
            instance=new Datasource();}
        return instance;
    }
    public Connection getConnection(){
        return connection;
    }
}