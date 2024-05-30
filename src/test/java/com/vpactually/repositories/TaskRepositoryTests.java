package com.vpactually.repositories;

import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static com.vpactually.util.DataUtil.ANOTHER_TASK;
import static com.vpactually.util.DataUtil.EXISTING_TASK;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TaskRepositoryTests {

    @InjectMocks
    private static TaskRepository taskRepository;

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
        var task = EXISTING_TASK;
        assertThat(taskRepository.findAll().toString()).contains(task.getTitle());
        assertThat(taskRepository.findAll().toString()).contains(task.getDescription());
    }

    @Test
    void testFindById() {
        assertThat(taskRepository.findById(1).get().getId()).isEqualTo(EXISTING_TASK.getId());
    }

    @Test
    void testSave() {
        var savedTask = taskRepository.save(ANOTHER_TASK);
        assertThat(ANOTHER_TASK).isEqualTo(savedTask);
    }

    @Test
    void testUpdate() {
        var updatedTask = EXISTING_TASK;
        updatedTask.setTitle("Updated Title");
        updatedTask = taskRepository.update(updatedTask);
        var actual = taskRepository.findById(updatedTask.getId()).orElseThrow();

        assertThat(actual).isEqualTo(updatedTask);
    }

    @Test
    void testDelete() {
        var task = ANOTHER_TASK;
        taskRepository.save(task);
        taskRepository.deleteById(task.getId());
        assertThat(taskRepository.findAll()).doesNotContain(task);
    }

//    @Test
//    void testSaveUserTasks() {
//        var user = ADMIN;
//        var tasks = user.getTasks();
//        var newTask = ANOTHER_TASK;
//
//        TASK_DAO.save(newTask);
//
//        user.getTasks().add(newTask);
//
//        TASK_DAO.saveUserTasks(tasks, user.getId());
//        newTask.setFetchType(FetchType.LAZY);
//        assertThat(TASK_DAO.findAll()).contains(newTask);
//    }
}
