package com.vpactually.services;

import com.vpactually.dao.TaskStatusDAO;
import com.vpactually.dto.taskStatuses.TaskStatusCreateUpdateDTO;
import com.vpactually.dto.taskStatuses.TaskStatusDTO;
import com.vpactually.mappers.TaskStatusMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

public class TaskStatusService {
    private static final TaskStatusService INSTANCE = new TaskStatusService();

    private static final TaskStatusDAO taskStatusDAO = TaskStatusDAO.getInstance();
    private static final TaskStatusMapper taskStatusMapper = Mappers.getMapper(TaskStatusMapper.class);

    private TaskStatusService() {
    }

    public static TaskStatusService getInstance() {
        return INSTANCE;
    }

    public List<TaskStatusDTO> findAll() {
        return taskStatusDAO.findAll().stream().map(taskStatusMapper::map).toList();
    }

    public Optional<TaskStatusDTO> findById(Integer id) {
        return taskStatusDAO.findById(id).map(taskStatusMapper::map);
    }

    public TaskStatusDTO update(TaskStatusCreateUpdateDTO taskStatusUpdateDTO, Integer id) {
        var user = taskStatusDAO.findById(id).orElseThrow();
        taskStatusMapper.update(taskStatusUpdateDTO, user);
        taskStatusDAO.save(user);
        return taskStatusMapper.map(user);
    }

    public TaskStatusDTO save(TaskStatusCreateUpdateDTO userCreateDTO) {
        var user = taskStatusMapper.map(userCreateDTO);
        taskStatusDAO.save(user);
        return taskStatusMapper.map(user);
    }

    public void deleteById(Integer id) {
        taskStatusDAO.deleteById(id);
    }
}
