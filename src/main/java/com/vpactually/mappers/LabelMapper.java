package com.vpactually.mappers;

import com.vpactually.dto.labels.LabelCreateUpdateDTO;
import com.vpactually.dto.labels.LabelDTO;
import com.vpactually.entities.Label;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LabelMapper {

    public LabelMapper() {
    }

    public abstract Label map(LabelCreateUpdateDTO dto);

    public abstract LabelDTO map(Label model);

    public abstract List<LabelDTO> map(List<Label> models);

    public abstract void update(LabelCreateUpdateDTO dto, @MappingTarget Label model);

}
