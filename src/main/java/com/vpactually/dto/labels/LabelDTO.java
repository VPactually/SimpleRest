package com.vpactually.dto.labels;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LabelDTO {
    private Integer id;
    private String name;
}
