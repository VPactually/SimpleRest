package com.vpactually.dao;

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
public class TaskDAOTests {

    @InjectMocks
    private static TaskDAO TASK_DAO ;

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
        assertThat(TASK_DAO.findAll().toString()).contains(task.getTitle());
        assertThat(TASK_DAO.findAll().toString()).contains(task.getDescription());
    }

    @Test
    void testFindById() {
        assertThat(TASK_DAO.findById(1).get().getId()).isEqualTo(EXISTING_TASK.getId());
    }

    @Test
    void testSave() {
        var savedTask = TASK_DAO.save(ANOTHER_TASK);
        assertThat(ANOTHER_TASK).isEqualTo(savedTask);
    }

    @Test
    void testUpdate() {
        var updatedTask = EXISTING_TASK;
        updatedTask.setTitle("Updated Title");
        updatedTask = TASK_DAO.update(updatedTask);
        var actual = TASK_DAO.findById(updatedTask.getId()).orElseThrow();

        assertThat(actual).isEqualTo(updatedTask);
    }

    @Test
    void testDelete() {
        var task = ANOTHER_TASK;
        TASK_DAO.save(task);
        TASK_DAO.deleteById(task.getId());
        assertThat(TASK_DAO.findAll()).doesNotContain(task);
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
