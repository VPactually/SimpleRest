package com.vpactually.dao;

import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.SQLException;

import static com.vpactually.util.DataUtil.ANOTHER_STATUS;
import static com.vpactually.util.DataUtil.EXISTING_STATUS;
import static org.assertj.core.api.Assertions.assertThat;

public class TaskStatusDAOTests {

    private static final TaskStatusDAO taskStatusDAO = TaskStatusDAO.getInstance();

    private static JdbcDatabaseContainer<?> postgresqlContainer;

    @BeforeEach
    public void startContainer() throws SQLException {
        postgresqlContainer = ContainerUtil.run(postgresqlContainer);
    }

    @AfterAll
    public static void stopContainer() {
        postgresqlContainer.stop();
    }

    @Test
    void testFindAll() {
        assertThat(taskStatusDAO.findAll()).contains(EXISTING_STATUS);
        assertThat(taskStatusDAO.findAll().toString()).contains("draft", "to_review", "to_be_fixed", "to_publish", "published");
    }

    @Test
    void testFindById() {
        assertThat(taskStatusDAO.findById(1)).contains(EXISTING_STATUS);
    }

    @Test
    void testSave() {
        var savedTaskStatus = taskStatusDAO.save(ANOTHER_STATUS);

        assertThat(ANOTHER_STATUS).isEqualTo(savedTaskStatus);
        assertThat(taskStatusDAO.findAll()).contains(savedTaskStatus);
    }

    @Test
    void testUpdate() {
        var updatedTaskStatus = ANOTHER_STATUS;
        updatedTaskStatus.setId(1);
        taskStatusDAO.update(updatedTaskStatus);
        assertThat(taskStatusDAO.findById(1)).contains(updatedTaskStatus);
        assertThat(taskStatusDAO.findAll()).contains(updatedTaskStatus);
    }

    @Test
    void testDelete() {
        taskStatusDAO.deleteById(EXISTING_STATUS.getId());
        assertThat(taskStatusDAO.findAll()).doesNotContain(EXISTING_STATUS);
    }


}
