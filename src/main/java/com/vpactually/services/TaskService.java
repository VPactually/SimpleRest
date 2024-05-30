package com.vpactually.services;

import com.vpactually.repositories.TaskRepository;
import com.vpactually.dto.tasks.TaskCreateDTO;
import com.vpactually.dto.tasks.TaskDTO;
import com.vpactually.dto.tasks.TaskUpdateDTO;
import com.vpactually.mappers.TaskMapper;
import com.vpactually.util.FetchType;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository TASK_DAO;
    private final TaskMapper mapper;

    public List<TaskDTO> findAll() {
        return TASK_DAO.findAll().stream().map(mapper::map).toList();
    }

    public Optional<TaskDTO> findById(Integer id) {
        var task = TASK_DAO.findById(id, FetchType.LAZY).orElseThrow();
        var mappedTask = mapper.map(task);
        return Optional.of(mappedTask);
    }

    public TaskDTO save(TaskCreateDTO taskCreateDTO) {
        var task = mapper.map(taskCreateDTO);
        TASK_DAO.save(task);
        return mapper.map(task);
    }

    public TaskDTO update(TaskUpdateDTO taskUpdateDTO, Integer id) {
        var task = TASK_DAO.findById(id, FetchType.LAZY).orElseThrow();
        mapper.update(taskUpdateDTO, task);
        TASK_DAO.update(task);
        return mapper.map(task);

    }

    public void deleteById(Integer id) {
        TASK_DAO.deleteById(id);
    }
}
