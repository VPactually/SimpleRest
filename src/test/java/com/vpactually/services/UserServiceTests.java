package com.vpactually.services;

import com.vpactually.dao.UserDAO;
import com.vpactually.dto.users.UserCreateUpdateDTO;
import com.vpactually.dto.users.UserDTO;
import com.vpactually.mappers.UserMapper;
import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.vpactually.util.DataUtil.ADMIN;
import static com.vpactually.util.DataUtil.ANOTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeAll
    public static void startContainer() throws SQLException {
        ContainerUtil.run();
    }

    @AfterAll
    public static void stopContainer() {
        ContainerUtil.stop();
    }

    @Test
    public void testFindAll() {
        var adminDto = new UserDTO(ADMIN.getId(), ADMIN.getName(), ADMIN.getEmail(), ADMIN.getCreatedAt());
        when(userDAO.findAll()).thenReturn(List.of(ADMIN));
        when(userMapper.map(ADMIN)).thenReturn(adminDto);

        var result = userService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).toString()).isEqualTo(adminDto.toString());
        verify(userDAO).findAll();
        verify(userMapper).map(ADMIN);
    }

    @Test
    public void testFindById() {
        var adminDto = new UserDTO(ADMIN.getId(), ADMIN.getName(), ADMIN.getEmail(), ADMIN.getCreatedAt());
        when(userDAO.findById(ADMIN.getId())).thenReturn(Optional.of(ADMIN));
        when(userMapper.map(ADMIN)).thenReturn(adminDto);

        var result = userService.findById(ADMIN.getId());

        assertThat(result.get().toString()).isEqualTo(adminDto.toString());
        verify(userDAO).findById(ADMIN.getId());
        verify(userMapper).map(ADMIN);
    }

    @Test
    public void testSave() {
        var userDto = new UserDTO(2, ANOTHER_USER.getName(), ANOTHER_USER.getEmail(), ANOTHER_USER.getCreatedAt());
        var userCreateUpdateDto = new UserCreateUpdateDTO(JsonNullable.of(ANOTHER_USER.getName()),
                JsonNullable.of(ANOTHER_USER.getEmail()), JsonNullable.of(ANOTHER_USER.getPassword()));
        var savedUser = ANOTHER_USER;
        savedUser.setId(2);

        when(userMapper.map(userCreateUpdateDto)).thenReturn(ANOTHER_USER);
        when(userDAO.save(ANOTHER_USER)).thenReturn(savedUser);
        when(userMapper.map(ANOTHER_USER)).thenReturn(userDto);

        var result = userService.save(userCreateUpdateDto);

        assertThat(result.toString()).isEqualTo(userDto.toString());
        verify(userMapper).map(userCreateUpdateDto);
        verify(userDAO).save(ANOTHER_USER);
        verify(userMapper).map(ANOTHER_USER);
    }

    @Test
    public void testUpdate() {
        var userCreateUpdateDTO = new UserCreateUpdateDTO(JsonNullable.of("NewName"), null, null);
        var updatedUser = ADMIN;
        var updatedDto = new UserDTO(
                updatedUser.getId(), updatedUser.getEmail(), updatedUser.getName(), updatedUser.getCreatedAt());

        when(userDAO.findById(ADMIN.getId())).thenReturn(Optional.of(ADMIN));
        when(userDAO.update(updatedUser)).thenReturn(updatedUser);
        when(userMapper.map(updatedUser)).thenReturn(updatedDto);

        var result = userService.update(userCreateUpdateDTO, ADMIN.getId());

        assertThat(result.toString()).isEqualTo(updatedDto.toString());
        verify(userDAO).findById(ADMIN.getId());
        verify(userDAO).update(ADMIN);
        verify(userMapper).map(updatedUser);
    }

    @Test
    public void testDeleteByIdById() {
        when(userDAO.deleteById(ADMIN.getId())).thenReturn(true);
        userService.deleteById(ADMIN.getId());
        verify(userDAO).deleteById(ADMIN.getId());
    }
}
