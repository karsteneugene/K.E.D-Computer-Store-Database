package com.example.computerstoredb.Models;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataAccessor {
    public Connection connection;

    public Connection getConnection() {
        String databaseName = "computer_store";
        String databaseUser = "root";
        String databasePassword = "root";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, databaseUser, databasePassword);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
