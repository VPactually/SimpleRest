package com.vpactually.services;

import com.vpactually.dao.TaskDAO;
import com.vpactually.dto.tasks.TaskCreateUpdateDTO;
import com.vpactually.dto.tasks.TaskDTO;
import com.vpactually.entities.Label;
import com.vpactually.mappers.TaskMapper;
import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.vpactually.util.DataUtil.ANOTHER_TASK;
import static com.vpactually.util.DataUtil.EXISTING_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    @Mock
    private TaskDAO taskDAO;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @BeforeAll
    public static void startContainer() throws SQLException {
        ContainerUtil.run();
    }

    @AfterAll
    public static void stopContainer() {
        ContainerUtil.stop();
    }

    @Test
    public void testFindAll() {
        var existingTask = List.of(EXISTING_TASK);
        var taskDto = new TaskDTO(EXISTING_TASK.getId(), EXISTING_TASK.getTitle(), EXISTING_TASK.getDescription(),
                EXISTING_TASK.getTaskStatus().getName(), EXISTING_TASK.getAssignee().getId(),
                EXISTING_TASK.getCreatedAt().toString(),
                EXISTING_TASK.getLabels().stream().map(Label::getId).collect(Collectors.toSet()));

        when(taskDAO.findAll()).thenReturn(existingTask);
        when(taskMapper.map(existingTask.get(0))).thenReturn(taskDto);

        var actual = taskService.findAll();

        assertThat(actual.toString()).isEqualTo(List.of(taskDto).toString());
        verify(taskDAO).findAll();
        verify(taskMapper).map(existingTask.get(0));
    }

    @Test
    public void testFindById() {
        var existingTask = EXISTING_TASK;
        var taskDto = new TaskDTO(EXISTING_TASK.getId(), EXISTING_TASK.getTitle(), EXISTING_TASK.getDescription(),
                EXISTING_TASK.getTaskStatus().getName(), EXISTING_TASK.getAssignee().getId(),
                EXISTING_TASK.getCreatedAt().toString(),
                EXISTING_TASK.getLabels().stream().map(Label::getId).collect(Collectors.toSet()));

        when(taskDAO.findById(existingTask.getId())).thenReturn(java.util.Optional.of(existingTask));
        when(taskMapper.map(existingTask)).thenReturn(taskDto);

        var actual = taskService.findById(existingTask.getId());

        assertThat(actual.toString()).isEqualTo(taskDto.toString());
        verify(taskDAO).findById(existingTask.getId());
        verify(taskMapper).map(existingTask);
    }

    @Test
    public void testSave() {
        var taskCreateUpdateDTO = new TaskCreateUpdateDTO(JsonNullable.of(ANOTHER_TASK.getTitle()),
                JsonNullable.of(ANOTHER_TASK.getDescription()), JsonNullable.of(ANOTHER_TASK.getTaskStatus().getName()),
                JsonNullable.of(ANOTHER_TASK.getAssignee().getId()),
                JsonNullable.of(ANOTHER_TASK.getLabels().stream().map(Label::getId).collect(Collectors.toSet())));
        var task = ANOTHER_TASK;
        task.setId(2);

        when(taskMapper.map(taskCreateUpdateDTO)).thenReturn(ANOTHER_TASK);
        when(taskDAO.save(ANOTHER_TASK)).thenReturn(ANOTHER_TASK);
        when(taskMapper.map(ANOTHER_TASK)).thenReturn(new TaskDTO(ANOTHER_TASK.getId(), ANOTHER_TASK.getTitle(), ANOTHER_TASK.getDescription(),
                ANOTHER_TASK.getTaskStatus().getName(), ANOTHER_TASK.getAssignee().getId(),
                ANOTHER_TASK.getCreatedAt().toString(),
                ANOTHER_TASK.getLabels().stream().map(Label::getId).collect(Collectors.toSet())));

        var actual = taskService.save(taskCreateUpdateDTO);

        assertThat(actual.toString()).isEqualTo(new TaskDTO(ANOTHER_TASK.getId(), ANOTHER_TASK.getTitle(), ANOTHER_TASK.getDescription(),
                ANOTHER_TASK.getTaskStatus().getName(), ANOTHER_TASK.getAssignee().getId(),
                ANOTHER_TASK.getCreatedAt().toString(),
                ANOTHER_TASK.getLabels().stream().map(Label::getId).collect(Collectors.toSet())).toString());
        verify(taskMapper).map(taskCreateUpdateDTO);
        verify(taskDAO).save(ANOTHER_TASK);
        verify(taskMapper).map(ANOTHER_TASK);
    }

    @Test
    public void testUpdate() {
        var taskCreateUpdateDTO = new TaskCreateUpdateDTO(JsonNullable.of("new title"), null, null, null, null);
        var task = EXISTING_TASK;
        var updatedTask = new TaskDTO(task.getId(), task.getTitle(), task.getDescription(),
                task.getTaskStatus().getName(), task.getAssignee().getId(), task.getCreatedAt().toString(),
                task.getLabels().stream().map(Label::getId).collect(Collectors.toSet()));


        when(taskDAO.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskDAO.update(task)).thenReturn(task);
        when(taskMapper.map(task)).thenReturn(updatedTask);

        var actual = taskService.update(taskCreateUpdateDTO, task.getId());

        assertThat(actual.toString()).isEqualTo(updatedTask.toString());
        verify(taskDAO).findById(task.getId());
        verify(taskDAO).update(task);
        verify(taskMapper).map(task);
    }

    @Test
    public void testDeleteById() {
        when(taskDAO.deleteById(EXISTING_TASK.getId())).thenReturn(true);
        taskService.deleteById(EXISTING_TASK.getId());
        verify(taskDAO).deleteById(EXISTING_TASK.getId());
    }
}
