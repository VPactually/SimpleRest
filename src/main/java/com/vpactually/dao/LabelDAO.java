package com.vpactually.dao;

import com.vpactually.entities.Label;
import com.vpactually.entities.Task;
import com.vpactually.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class LabelDAO implements DAO<Integer, Label> {

    private static final String FIND_ALL_SQL = "SELECT * FROM labels";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM labels WHERE id = ?";
    private static final String SAVE_SQL = "INSERT INTO labels (name, created_at) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE labels SET name = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM labels WHERE id = ?";
    private static final String FIND_TASKS_BY_LABEL_ID_SQL = """
            SELECT * FROM tasks INNER JOIN  task_labels ON task_labels.task_id = tasks.id WHERE label_id = ?
            """;


    @Override
    public List<Label> findAll() {
        List<Label> labels = new ArrayList<>();
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var label = buildLabel(resultSet);
                labels.add(label);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return labels;
    }

    @Override
    public Optional<Label> findById(Integer id) {
        Label label = null;
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                label = buildLabel(resultSet);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return Optional.ofNullable(label);
    }

    @Override
    public Label save(Label label) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, label.getName());
            preparedStatement.setObject(2, label.getCreatedAt());
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                label.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return label;
    }

    @Override
    public Label update(Label label) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1, label.getName());
            preparedStatement.setObject(2, label.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return label;
    }


    @Override
    public boolean deleteById(Integer id) {
        if (!findTasksByLabelId(id).isEmpty()) {
            return false;
        }
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(DELETE_SQL)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return true;
    }

    public static Set<Task> findTasksByLabelId(Integer id) {
        Set<Task> tasks = new HashSet<>();
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_TASKS_BY_LABEL_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                var task = buildTask(resultSet);
//                tasks.add(task);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return tasks;
    }

    public static Label buildLabel(ResultSet resultSet) throws SQLException {
        var label = new Label();
        label.setId(resultSet.getInt("id"));
        label.setName(resultSet.getString("name"));
        label.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate());
        return label;
    }

}
