package com.vpactually.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static Connection INSTANCE;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getInstance() {
        try {
            if (INSTANCE == null) {
                INSTANCE = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/aston_assignments",
                        "postgres",
                        "postgres");
            }
            return INSTANCE;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection setConnection(Connection connection) {
        INSTANCE = connection;
        return INSTANCE;
    }

}
