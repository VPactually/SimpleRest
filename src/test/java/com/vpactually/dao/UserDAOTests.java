package com.vpactually.dao;

import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Optional;

import static com.vpactually.util.DataUtil.ADMIN;
import static com.vpactually.util.DataUtil.ANOTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserDAOTests {

    @InjectMocks
    private static UserDAO userDAO;


    @BeforeAll
    public static void startContainer() throws SQLException {
        ContainerUtil.run();
    }

    @AfterAll
    public static void stopContainer() {
        ContainerUtil.stop();
    }

    @Test
    void testFindAll() {
        var admin = ADMIN;
        assertThat(userDAO.findAll().toString()).contains(admin.getName());
        assertThat(userDAO.findAll().toString()).contains(admin.getEmail());
    }

    @Test
    void testFindById() {
        var user = ADMIN;

        assertThat(userDAO.findById(1).get().getName()).isEqualTo(user.getName());
        assertThat(userDAO.findById(1).get().getEmail()).isEqualTo(user.getEmail());
        assertThat(userDAO.findById(666)).isEqualTo(Optional.empty());
    }

    @Test
    void testSave() {
        var savedUser = ANOTHER_USER;
        savedUser = userDAO.save(savedUser);
        assertThat(userDAO.findById(savedUser.getId()).get()).isEqualTo(savedUser);
    }

    @Test
    void testUpdate() {
        var admin = ADMIN;
        admin.setName("newName");
        var updatedUser = userDAO.update(admin);

        assertThat(updatedUser.getName()).isEqualTo(userDAO.findById(updatedUser.getId()).get().getName());
    }

    @Test
    void testDelete() {
        var user = ANOTHER_USER;
        user.setId(666);
        var savedUser = userDAO.save(user);
        userDAO.deleteById(savedUser.getId());

        assertThat(userDAO.findById(savedUser.getId())).isEqualTo(Optional.empty());
    }

}
