package com.vpactually.dto.users;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String name;
    private String email;
    private LocalDate createdAt;
}
