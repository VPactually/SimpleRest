package com.vpactually.services;

import com.vpactually.dao.TaskDAO;
import com.vpactually.dto.tasks.TaskCreateUpdateDTO;
import com.vpactually.dto.tasks.TaskDTO;
import com.vpactually.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TaskService {

    private final TaskDAO TASK_DAO;
    private final TaskMapper mapper;

    public List<TaskDTO> getAll() {
        return TASK_DAO.findAll().stream().map(mapper::map).toList();
    }

    public TaskDTO create(TaskCreateUpdateDTO taskCreateDTO) {
        var task = mapper.map(taskCreateDTO);
        TASK_DAO.save(task);
        return mapper.map(task);
    }

    public TaskDTO findById(Integer id) {
        return mapper.map(TASK_DAO.findById(id).orElseThrow());
    }

    public TaskDTO update(TaskCreateUpdateDTO taskUpdateDTO, Integer id) {
        var task = TASK_DAO.findById(id).orElseThrow();
        mapper.update(taskUpdateDTO, task);
        TASK_DAO.save(task);
        return mapper.map(task);

    }

    public void delete(Integer id) {
        TASK_DAO.deleteById(id);
    }
}
