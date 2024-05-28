package com.vpactually.entities;

import com.vpactually.dao.TaskDAO;
import com.vpactually.util.ConnectionManager;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class User implements BaseEntity{
    private Integer id;
    private String name;
    private String email;
    private String password;
    private LocalDate createdAt;
    private Set<Task> tasks = new HashSet<>();

    private static final String FIND_USER_TASKS_BY_USER_ID_SQL = "SELECT * FROM tasks WHERE user_id = ?";

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

    public void fetchTasks() {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_USER_TASKS_BY_USER_ID_SQL)){
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var task = TaskDAO.buildTask(resultSet);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public Set<Task> getTasks() {
        if (tasks.isEmpty()) {
            fetchTasks();
        }
        return tasks;
    }

}
