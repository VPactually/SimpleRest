package com.vpactually.entities;

import com.vpactually.dao.TaskDAO;
import com.vpactually.util.DependencyContainer;
import com.vpactually.util.FetchType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements BaseEntity{
    private Integer id;
    private String name;
    private String email;
    private String password;
    private LocalDate createdAt;
    private Set<Task> tasks = new HashSet<>();
    private transient FetchType fetchType = FetchType.LAZY;

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String name, String email, String password, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public Set<Task> getTasks() {
        if (fetchType.equals(FetchType.EAGER)) {
            tasks = DependencyContainer.getInstance().getDependency(TaskDAO.class).findUserTasksByUserId(id);
        }
        return tasks;
    }
}
