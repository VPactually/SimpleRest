package com.vpactually.services;

import com.vpactually.repositories.TaskStatusRepository;
import com.vpactually.dto.taskStatuses.TaskStatusCreateUpdateDTO;
import com.vpactually.dto.taskStatuses.TaskStatusDTO;
import com.vpactually.mappers.TaskStatusMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TaskStatusService {

    private final TaskStatusRepository taskStatusDAO ;
    private final TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> findAll() {
        return taskStatusDAO.findAll().stream().map(taskStatusMapper::map).toList();
    }

    public Optional<TaskStatusDTO> findById(Integer id) {
        return taskStatusDAO.findById(id).map(taskStatusMapper::map);
    }

    public TaskStatusDTO update(TaskStatusCreateUpdateDTO taskStatusUpdateDTO, Integer id) {
        var status = taskStatusDAO.findById(id).orElseThrow();
        taskStatusMapper.update(taskStatusUpdateDTO, status);
        taskStatusDAO.update(status);
        return taskStatusMapper.map(status);
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
