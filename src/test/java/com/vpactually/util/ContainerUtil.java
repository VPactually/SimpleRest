package com.vpactually.util;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ContainerUtil {

    private ContainerUtil() {
    }

    public static JdbcDatabaseContainer<?> run(JdbcDatabaseContainer<?> container) throws SQLException {
        container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass")
                .withInitScript("schema.sql");
        container.start();
        ConnectionManager.setConnection(DriverManager.getConnection(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword())
        );
        return container;
    }
}
