package com.vpactually.repositories;

import com.vpactually.entities.TaskStatus;
import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static com.vpactually.util.DataUtil.ANOTHER_STATUS;
import static com.vpactually.util.DataUtil.EXISTING_STATUS_1;
import static com.vpactually.util.DataUtil.EXISTING_STATUS_2;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TaskStatusRepositoryTests {

    @InjectMocks
    private static TaskStatusRepository taskStatusRepository;

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
        assertThat(taskStatusRepository.findAll()).contains(EXISTING_STATUS_1);
        assertThat(taskStatusRepository.findAll().toString())
                .contains("draft", "to_review", "to_be_fixed", "to_publish", "published");
    }

    @Test
    void testFindById() {
        assertThat(taskStatusRepository.findById(1)).contains(EXISTING_STATUS_1);
    }

    @Test
    void testFindBySlug() {
        assertThat(taskStatusRepository.findById(1).get().getId()).isEqualTo(EXISTING_STATUS_1.getId());
    }

    @Test
    void testSave() {
        var savedTaskStatus = taskStatusRepository.save(ANOTHER_STATUS);

        assertThat(ANOTHER_STATUS).isEqualTo(savedTaskStatus);
        assertThat(taskStatusRepository.findAll()).contains(savedTaskStatus);
    }

    @Test
    void testUpdate() {
        var updatedTaskStatus = EXISTING_STATUS_2;
        updatedTaskStatus.setSlug("newSlug");
        TaskStatus update = taskStatusRepository.update(updatedTaskStatus);
        assertThat(taskStatusRepository.findAll()).contains(update);
    }

    @Test
    void testDelete() {
        taskStatusRepository.deleteById(EXISTING_STATUS_1.getId());
        assertThat(taskStatusRepository.findAll()).doesNotContain(EXISTING_STATUS_1);
    }

}
