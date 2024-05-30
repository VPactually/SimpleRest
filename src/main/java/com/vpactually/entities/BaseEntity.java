package com.vpactually.entities;

import com.vpactually.dao.TaskDAO;
import com.vpactually.util.ConnectionManager;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public interface BaseEntity {

    default Set<Task> fetchTasksBase(String sql, Set<Task> tasks, Integer id) {

        if (tasks == null) {
            tasks = new HashSet<>();
        }
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(sql)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var task = TaskDAO.buildTask(resultSet);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return tasks;
    }

}
