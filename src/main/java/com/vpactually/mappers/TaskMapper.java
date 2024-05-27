package com.vpactually.mappers;


import com.vpactually.dao.TaskStatusDAO;
import com.vpactually.dto.tasks.TaskCreateUpdateDTO;
import com.vpactually.dto.tasks.TaskDTO;
import com.vpactually.entities.Label;
import com.vpactually.entities.Task;
import com.vpactually.entities.TaskStatus;
import com.vpactually.entities.User;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class,TaskStatusMapper.class, UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    private static final TaskStatusDAO taskStatusDAO = TaskStatusDAO.getInstance();

    public abstract Task map(TaskCreateUpdateDTO dto);

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "labelsToLabelsIds")
    public abstract TaskDTO map(Task model);


    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "slugToTaskStatus")
    @Mapping(source = "assigneeId", target = "assignee", qualifiedByName = "assigneeIdToUser")
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "labelIdsToLabels")
    public abstract void update(TaskCreateUpdateDTO dto, @MappingTarget Task model);

    @Named("slugToTaskStatus")
    public TaskStatus slugToTaskStatus(String slug) {
        return taskStatusDAO.findBySlug(slug).orElseThrow();
    }

    @Named("labelIdsToLabels")
    public Set<Label> labelIdToLabel(Set<Integer> labelsIds) {
        return labelsIds == null ? null : labelsIds.stream().map(Label::new).collect(Collectors.toSet());
    }

    @Named("assigneeIdToUser")
    public User assigneeIdToUser(Integer id) {
        return id == null ? null : new User(id);
    }

    @Named("labelsToLabelsIds")
    public Set<Integer> labelsToLabelsIds(Set<Label> labels) {
        return labels == null ? null : labels.stream().map(Label::getId).collect(Collectors.toSet());
    }
}
