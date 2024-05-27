package com.vpactually.mappers;

import com.vpactually.dto.taskStatuses.TaskStatusCreateUpdateDTO;
import com.vpactually.dto.taskStatuses.TaskStatusDTO;
import com.vpactually.entities.TaskStatus;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {

    public TaskStatusMapper() {}

    public abstract TaskStatus map(TaskStatusCreateUpdateDTO dto);

    public abstract TaskStatusDTO map(TaskStatus model);

    public abstract void update(TaskStatusCreateUpdateDTO dto, @MappingTarget TaskStatus model);

}