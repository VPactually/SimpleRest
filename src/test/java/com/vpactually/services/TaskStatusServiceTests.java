package com.vpactually.services;

import com.vpactually.repositories.TaskStatusRepository;
import com.vpactually.dto.taskStatuses.TaskStatusCreateUpdateDTO;
import com.vpactually.dto.taskStatuses.TaskStatusDTO;
import com.vpactually.mappers.TaskStatusMapper;
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

import static com.vpactually.util.DataUtil.EXISTING_STATUS_1;
import static com.vpactually.util.DataUtil.EXISTING_STATUS_2;
import static com.vpactually.util.DataUtil.ANOTHER_STATUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskStatusServiceTests {
    @Mock
    private TaskStatusRepository taskStatusDAO;

    @Mock
    private TaskStatusMapper taskStatusMapper;

    @InjectMocks
    private TaskStatusService taskStatusService;

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
        var taskStatuses = List.of(EXISTING_STATUS_1, EXISTING_STATUS_2);
        var taskStatusDTOs = List.of(
                new TaskStatusDTO(EXISTING_STATUS_1.getId(), EXISTING_STATUS_1.getName(),
                        EXISTING_STATUS_1.getSlug()),
                new TaskStatusDTO(EXISTING_STATUS_2.getId(), EXISTING_STATUS_2.getName(),
                        EXISTING_STATUS_2.getSlug())
        );

        when(taskStatusDAO.findAll()).thenReturn(taskStatuses);
        when(taskStatusMapper.map(taskStatuses.get(0))).thenReturn(taskStatusDTOs.get(0));
        when(taskStatusMapper.map(taskStatuses.get(1))).thenReturn(taskStatusDTOs.get(1));

        var actual = taskStatusService.findAll();

        assertThat(actual).isEqualTo(taskStatusDTOs);
        verify(taskStatusDAO).findAll();
        verify(taskStatusMapper).map(taskStatuses.get(0));
        verify(taskStatusMapper).map(taskStatuses.get(1));
    }

    @Test
    public void testFindById() {
        when(taskStatusDAO.findById(EXISTING_STATUS_1.getId())).thenReturn(Optional.of(EXISTING_STATUS_1));
        when(taskStatusMapper.map(EXISTING_STATUS_1)).thenReturn(
                new TaskStatusDTO(EXISTING_STATUS_1.getId(), EXISTING_STATUS_1.getName(),
                        EXISTING_STATUS_1.getSlug()));

        var actual = taskStatusService.findById(EXISTING_STATUS_1.getId()).get();

        assertThat(actual.toString()).isEqualTo(new TaskStatusDTO(EXISTING_STATUS_1.getId(),
                EXISTING_STATUS_1.getName(), EXISTING_STATUS_1.getSlug()).toString());
        verify(taskStatusDAO).findById(EXISTING_STATUS_1.getId());
        verify(taskStatusMapper).map(EXISTING_STATUS_1);
    }

    @Test
    public void testSave() {
        var statusCreateUpdateDTO = new TaskStatusCreateUpdateDTO(
                JsonNullable.of(ANOTHER_STATUS.getName()),
                JsonNullable.of(ANOTHER_STATUS.getSlug()));
        var status = ANOTHER_STATUS;
        status.setId(6);

        when(taskStatusMapper.map(statusCreateUpdateDTO)).thenReturn(ANOTHER_STATUS);
        when(taskStatusDAO.save(ANOTHER_STATUS)).thenReturn(ANOTHER_STATUS);
        when(taskStatusMapper.map(ANOTHER_STATUS)).thenReturn(new TaskStatusDTO(ANOTHER_STATUS.getId(),
                ANOTHER_STATUS.getName(), ANOTHER_STATUS.getSlug()));

        var actual = taskStatusService.save(statusCreateUpdateDTO);

        assertThat(actual.toString()).isEqualTo(new TaskStatusDTO(ANOTHER_STATUS.getId(),
                ANOTHER_STATUS.getName(), ANOTHER_STATUS.getSlug()).toString());
        verify(taskStatusDAO).save(ANOTHER_STATUS);
        verify(taskStatusMapper).map(statusCreateUpdateDTO);
        verify(taskStatusMapper).map(ANOTHER_STATUS);

    }

    @Test
    public void testUpdate() {
        var taskStatusUpdateDTO = new TaskStatusCreateUpdateDTO(JsonNullable.of("newName"), null);
        var updatedStatus = EXISTING_STATUS_1;
        var updatedDDTO = new TaskStatusDTO(
                updatedStatus.getId(), updatedStatus.getName(), updatedStatus.getSlug());

        when(taskStatusDAO.findById(EXISTING_STATUS_1.getId())).thenReturn(Optional.of(EXISTING_STATUS_1));
        when(taskStatusDAO.update(updatedStatus)).thenReturn(updatedStatus);
        when(taskStatusMapper.map(updatedStatus)).thenReturn(updatedDDTO);

        TaskStatusDTO result = taskStatusService.update(taskStatusUpdateDTO, EXISTING_STATUS_1.getId());

        assertThat(result.toString()).isEqualTo(updatedDDTO.toString());
        verify(taskStatusDAO).findById(1);
        verify(taskStatusDAO).update(updatedStatus);
        verify(taskStatusMapper).map(updatedStatus);
    }

    @Test
    public void testDeleteById() {
        when(taskStatusDAO.deleteById(EXISTING_STATUS_1.getId())).thenReturn(true);

        taskStatusService.deleteById(EXISTING_STATUS_1.getId());

        verify(taskStatusDAO).deleteById(EXISTING_STATUS_1.getId());
    }
}
