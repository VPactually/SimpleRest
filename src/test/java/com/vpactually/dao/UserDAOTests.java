package com.vpactually.dao;

import com.vpactually.entities.Task;
import com.vpactually.util.ContainerUtil;
import com.vpactually.util.DependencyContainer;
import com.vpactually.util.FetchType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static com.vpactually.util.DataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UserDAOTests {
    private static final UserDAO userDAO = DependencyContainer.getInstance().getDependency(UserDAO.class);
    private static JdbcDatabaseContainer<?> postgresqlContainer;

    @BeforeAll
    public static void startContainer() throws SQLException {
        postgresqlContainer = ContainerUtil.run(postgresqlContainer);
    }

    @AfterAll
    public static void stopContainer() {
        postgresqlContainer.stop();
    }

    @Test
    void testFindAll() {
        var admin = ADMIN;
        assertThat(userDAO.findAll().toString()).contains(admin.getName());
        assertThat(userDAO.findAll().toString()).contains(admin.getEmail());
    }

    @Test
    void testFindById() {
        var user = ADMIN;

        assertThat(userDAO.findById(1).get().getName()).isEqualTo(user.getName());
        assertThat(userDAO.findById(1).get().getEmail()).isEqualTo(user.getEmail());
        assertThat(userDAO.findById(666)).isEqualTo(Optional.empty());
    }

    @Test
    void testSave() {
        var savedUser = ANOTHER_USER;
        var newTask1 = new Task(2, "Task 1", "Description 1", LocalDate.now(),
                EXISTING_STATUS_1, ADMIN, Set.of(EXISTING_LABEL_1));
        var newTask2 = new Task(3, "Task 2", "Description 2", LocalDate.now(),
                EXISTING_STATUS_1, ADMIN, Set.of(EXISTING_LABEL_1));

        savedUser.setFetchType(FetchType.EAGER);
        savedUser.getTasks().add(newTask1);
        savedUser.getTasks().add(newTask2);
        savedUser = userDAO.save(savedUser);

        assertThat(ANOTHER_USER).isEqualTo(savedUser);
        assertThat(userDAO.findById(savedUser.getId()).get().getTasks()).isEqualTo(savedUser.getTasks());

    }

    @Test
    void testUpdate() {
        var user = ADMIN;
        user.setName("newName");
        var updatedUser = userDAO.update(user);

        assertThat(updatedUser.getName()).isEqualTo(userDAO.findById(updatedUser.getId()).get().getName());
    }

    @Test
    void testDelete() {
        var user = ANOTHER_USER;
        user.setId(666);
        var savedUser = userDAO.save(user);
        userDAO.deleteById(savedUser.getId());

        assertThat(userDAO.findById(savedUser.getId())).isEqualTo(Optional.empty());
    }

}
