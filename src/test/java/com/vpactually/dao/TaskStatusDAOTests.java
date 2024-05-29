package com.vpactually.dao;

import com.vpactually.entities.TaskStatus;
import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static com.vpactually.util.DataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TaskStatusDAOTests {

    @InjectMocks
    private static TaskStatusDAO taskStatusDAO;

    @BeforeAll
    public static void startContainer() throws SQLException {
        ContainerUtil.run();
    }

    @AfterAll
    public static void stopContainer() {
        ContainerUtil.stop();
    }

    @Test
    void testFindAll() {
        assertThat(taskStatusDAO.findAll()).contains(EXISTING_STATUS_1);
        assertThat(taskStatusDAO.findAll().toString()).contains("draft", "to_review", "to_be_fixed", "to_publish", "published");
    }

    @Test
    void testFindById() {
        assertThat(taskStatusDAO.findById(1)).contains(EXISTING_STATUS_1);
    }

    @Test
    void testFindBySlug() {
        assertThat(taskStatusDAO.findById(1).get().getId()).isEqualTo(EXISTING_STATUS_1.getId());
    }

    @Test
    void testSave() {
        var savedTaskStatus = taskStatusDAO.save(ANOTHER_STATUS);

        assertThat(ANOTHER_STATUS).isEqualTo(savedTaskStatus);
        assertThat(taskStatusDAO.findAll()).contains(savedTaskStatus);
    }

    @Test
    void testUpdate() {
        var updatedTaskStatus = EXISTING_STATUS_2;
        updatedTaskStatus.setSlug("newSlug");
        TaskStatus update = taskStatusDAO.update(updatedTaskStatus);
        assertThat(taskStatusDAO.findAll()).contains(update);
    }

    @Test
    void testDelete() {
        taskStatusDAO.deleteById(EXISTING_STATUS_1.getId());
        assertThat(taskStatusDAO.findAll()).doesNotContain(EXISTING_STATUS_1);
    }

}
