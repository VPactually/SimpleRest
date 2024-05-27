package com.vpactually.dto.labels;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class LabelCreateUpdateDTO {
    private JsonNullable<String> name;
}
