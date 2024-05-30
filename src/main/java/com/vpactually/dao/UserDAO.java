package com.vpactually.dao;

import com.vpactually.entities.Task;
import com.vpactually.entities.User;
import com.vpactually.util.ConnectionManager;
import com.vpactually.util.FetchType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class UserDAO implements DAO<Integer, User> {

    private static final String FIND_ALL_SQL = "SELECT * FROM users";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String SAVE_SQL = "INSERT INTO users (name, email, password, created_at) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM users WHERE id = ?";
    private static final String SAVE_USER_TASKS_SQL = """
            INSERT INTO user_tasks VALUES (?, ?)
            """;

    private static final String DELETE_USER_TASKS_SQL = """
            DELETE FROM user_tasks WHERE user_id = ?
            """;

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var user = buildUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return users;
    }

    public Optional<User> findById(Integer id, FetchType fetchType) {
        User user = null;
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = buildUser(resultSet);
//                user.fetchTasks();
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return findById(id, FetchType.LAZY);
    }

    @Override
    public User save(User user) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, user.getName());
            preparedStatement.setObject(2, user.getEmail());
            preparedStatement.setObject(3, user.getPassword());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.executeUpdate();
            saveUserTasks(user.getTasks(), user.getId());
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
                user.setCreatedAt(LocalDate.now());
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return user;
    }

    @Override
    public User update(User user) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1, user.getName());
            preparedStatement.setObject(2, user.getEmail());
            preparedStatement.setObject(3, user.getPassword());
            preparedStatement.setObject(4, user.getId());
            preparedStatement.executeUpdate();
            saveUserTasks(user.getTasks(), user.getId());
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return user;
    }

    @Override
    public boolean deleteById(Integer id) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(DELETE_SQL)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.fillInStackTrace();
            return false;
        }
    }

    public static User buildUser(ResultSet resultSet) throws SQLException {
        var user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
        return user;
    }

    public static User buildUserByJoinTables(ResultSet resultSet) throws SQLException {
        var user = new User();
        var id = resultSet.getInt(11);
        user.setId(id);
        var name = resultSet.getString(12);
        user.setName(name);
        var email = resultSet.getString(13);
        user.setEmail(email);
        var password = resultSet.getString(14);
        user.setPassword(password);
        var date = resultSet.getDate(15).toLocalDate();
        user.setCreatedAt(date);
        return user;
    }

    public void saveUserTasks(Set<Task> tasks, Integer userId) {
        if (tasks == null) {
            return;
        }
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(DELETE_USER_TASKS_SQL)) {
            preparedStatement.setObject(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_USER_TASKS_SQL)) {
            for (var task : tasks) {
                preparedStatement.setObject(1, userId);
                preparedStatement.setObject(2, task.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

}
