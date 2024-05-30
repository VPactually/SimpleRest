package com.vpactually.mappers;


import com.vpactually.dto.tasks.TaskCreateDTO;
import com.vpactually.dto.tasks.TaskDTO;
import com.vpactually.dto.tasks.TaskUpdateDTO;
import com.vpactually.entities.Label;
import com.vpactually.entities.Task;
import com.vpactually.entities.TaskStatus;
import com.vpactually.entities.User;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class, TaskStatusMapper.class, UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Mapping(source = "assigneeId", target = "assignee", qualifiedByName = "assigneeIdToUser")
    @Mapping(source = "taskLabelIds", target = "labels", qualifiedByName = "labelIdsToLabels")
    @Mapping(source = "statusId", target = "taskStatus", qualifiedByName = "taskStatusIdToTaskStatus")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "labelsToLabelsIds")
    public abstract TaskDTO map(Task model);


    @Mapping(source = "statusId", target = "taskStatus.id")
    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(target = "labels", expression = "java(updateLabels(dto.getTaskLabelIds(), model.getLabels()))")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    @Named("taskStatusIdToTaskStatus")
    public TaskStatus taskStatusIdToTaskStatus(Integer id) {
        return id  == null ? null : new TaskStatus(id, new HashSet<>());
    }

    @Named("assigneeIdToUser")
    public User assigneeIdToUser(Integer id) {
        return id == null ? null : new User(id, new HashSet<>());
    }

    @Named("labelIdsToLabels")
    public Set<Label> labelIdsToLabels(Set<Integer> labelsIds) {
        return labelsIds == null ? null : labelsIds.stream().map(Label::new).collect(Collectors.toSet());
    }

    @Named("labelsToLabelsIds")
    public Set<Integer> labelsToLabelsIds(Set<Label> labels) {
        return labels == null ? null : labels.stream().map(Label::getId).collect(Collectors.toSet());
    }


    public Set<Label> updateLabels(JsonNullable<Set<Integer>> taskLabelIds, Set<Label> labels) {
        var setOfTaskLabelIds = JsonNullableMapper.unwrap(taskLabelIds);
        if (setOfTaskLabelIds == null || setOfTaskLabelIds.isEmpty() )  {
            return labels;
        } else {
            labels = new HashSet<>();
        }
        for (var id :  setOfTaskLabelIds) {
            labels.add(new Label(id));
        }
        return labels;
    }

}
