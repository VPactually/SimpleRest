package com.vpactually.services;

import com.vpactually.dao.TaskDAO;
import com.vpactually.dto.tasks.TaskCreateUpdateDTO;
import com.vpactually.dto.tasks.TaskDTO;
import com.vpactually.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TaskService {

    private final TaskDAO TASK_DAO;
    private final TaskMapper mapper;

    public List<TaskDTO> findAll() {
        return TASK_DAO.findAll().stream().map(mapper::map).toList();
    }

    public Optional<TaskDTO> findById(Integer id) {
        var task = TASK_DAO.findById(id).orElseThrow();
        var mappedTask = mapper.map(task);
        return Optional.of(mappedTask);
    }

    public TaskDTO save(TaskCreateUpdateDTO taskCreateDTO) {
        var task = mapper.map(taskCreateDTO);
        TASK_DAO.save(task);
        return mapper.map(task);
    }

    public TaskDTO update(TaskCreateUpdateDTO taskUpdateDTO, Integer id) {
        var task = TASK_DAO.findById(id).orElseThrow();
        mapper.update(taskUpdateDTO, task);
        TASK_DAO.update(task);
        return mapper.map(task);

    }

    public void deleteById(Integer id) {
        TASK_DAO.deleteById(id);
    }
}
