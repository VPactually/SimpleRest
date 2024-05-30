package com.vpactually.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static Connection instance;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getInstance() {
        try {
            if (instance == null) {
                instance = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/aston_assignments",
                        "postgres",
                        "postgres");
            }
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection setConnection(Connection connection) {
        instance = connection;
        return instance;
    }

}
