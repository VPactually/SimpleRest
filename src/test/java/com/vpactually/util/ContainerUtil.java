package com.vpactually.util;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.DriverManager;
import java.sql.SQLException;

public final class ContainerUtil {

    public static JdbcDatabaseContainer<?> postgresqlContainer;

    private ContainerUtil() {
    }

    public static JdbcDatabaseContainer<?> run() throws SQLException {
        postgresqlContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass")
                .withInitScript("schema.sql");
        postgresqlContainer.start();
        ConnectionManager.setConnection(DriverManager.getConnection(
                postgresqlContainer.getJdbcUrl(),
                postgresqlContainer.getUsername(),
                postgresqlContainer.getPassword())
        );
        return postgresqlContainer;
    }

    public static void stop() {
        postgresqlContainer.stop();
    }
}
