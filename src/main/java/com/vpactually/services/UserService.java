package com.vpactually.services;

import com.vpactually.dao.UserDAO;
import com.vpactually.dto.users.UserCreateUpdateDTO;
import com.vpactually.dto.users.UserDTO;
import com.vpactually.mappers.UserMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

public class UserService {

    private static final UserService INSTANCE = new UserService();

    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private UserService() {
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public List<UserDTO> findAll() {
        return userDAO.findAll().stream().map(userMapper::map).toList();
    }

    public Optional<UserDTO> findById(Integer id) {
        return userDAO.findById(id).map(userMapper::map);
    }

    public UserDTO update(UserCreateUpdateDTO userUpdateDTO, Integer id) {
        var user = userDAO.findById(id).orElseThrow();
        userMapper.update(userUpdateDTO, user);
        userDAO.save(user);
        return userMapper.map(user);
    }

    public UserDTO save(UserCreateUpdateDTO userCreateUpdateDTO) {
        var user = userMapper.map(userCreateUpdateDTO);
        userDAO.save(user);
        return userMapper.map(user);
    }

    public void delete(Integer id) {
        userDAO.deleteById(id);
    }
}
