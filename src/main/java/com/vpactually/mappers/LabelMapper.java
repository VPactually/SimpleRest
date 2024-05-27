package com.vpactually.mappers;

import com.vpactually.dto.labels.LabelCreateUpdateDTO;
import com.vpactually.dto.labels.LabelDTO;
import com.vpactually.entities.Label;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LabelMapper {

    public LabelMapper() {}

    public abstract Label map(LabelCreateUpdateDTO dto);

    public abstract LabelDTO map(Label model);

    public abstract void update(LabelCreateUpdateDTO dto, @MappingTarget Label model);

}