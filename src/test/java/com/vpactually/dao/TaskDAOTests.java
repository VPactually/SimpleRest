package com.vpactually.dao;

import com.vpactually.util.ContainerUtil;
import com.vpactually.util.FetchType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.SQLException;

import static com.vpactually.util.DataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TaskDAOTests {

    private static final TaskDAO TASK_DAO = TaskDAO.getInstance();

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
        var task = EXISTING_TASK;
        assertThat(TASK_DAO.findAll().toString()).contains(task.getTitle());
        assertThat(TASK_DAO.findAll().toString()).contains(task.getDescription());
    }

    @Test
    void testFindById() {
        assertThat(TASK_DAO.findById(1).get().getTitle()).isEqualTo(EXISTING_TASK.getTitle());
    }

    @Test
    void testSave() {
        var savedTask = TASK_DAO.save(ANOTHER_TASK);
        assertThat(ANOTHER_TASK).isEqualTo(savedTask);
    }

    @Test
    void testUpdate() {
        var updatedTask = ANOTHER_TASK;
        updatedTask.setId(1);
        updatedTask = TASK_DAO.update(updatedTask);
        updatedTask.setFetchType(FetchType.LAZY);
        var actual = TASK_DAO.findById(updatedTask.getId()).get();

        assertThat(actual).isEqualTo(updatedTask);
    }

    @Test
    void testDelete() {
        var task = ANOTHER_TASK;
        TASK_DAO.save(task);
        TASK_DAO.deleteById(task.getId());
        task.setFetchType(FetchType.LAZY);
        assertThat(TASK_DAO.findAll()).doesNotContain(task);

        TASK_DAO.deleteById(EXISTING_TASK.getId());
        assertThat(TASK_DAO.findAll().get(0).getId()).isEqualTo(EXISTING_TASK.getId());
    }

    @Test
    void testSaveUserTasks() {
        var user = ADMIN;
        var tasks = user.getTasks();
        var newTask = ANOTHER_TASK;

        TASK_DAO.save(newTask);

        user.addTask(newTask);

        TASK_DAO.saveUserTasks(tasks, user.getId());
        newTask.setFetchType(FetchType.LAZY);
        assertThat(TASK_DAO.findAll()).contains(newTask);
    }
}
