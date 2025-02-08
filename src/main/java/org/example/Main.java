package org.example;

import util.MyConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        Connection connection = MyConnection.getInstance().getCnx();
        if (connection != null) {
            System.out.println(" Test réussi : connexion établie !");
        } else {
            System.out.println(" Échec de la connexion !");
        }

    }
}
