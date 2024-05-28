package com.vpactually.services;

import com.vpactually.dao.UserDAO;
import com.vpactually.dto.users.UserCreateUpdateDTO;
import com.vpactually.dto.users.UserDTO;
import com.vpactually.mappers.UserMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final UserMapper userMapper;

    public List<UserDTO> findAll() {
        return userDAO.findAll().stream().map(userMapper::map).toList();
    }

    public Optional<UserDTO> findById(Integer id) {
        return userDAO.findById(id).map(userMapper::map);
    }

    public UserDTO update(UserCreateUpdateDTO userUpdateDTO, Integer id) {
        var user = userDAO.findById(id).orElseThrow();
        userMapper.update(userUpdateDTO, user);
        userDAO.update(user);
        return userMapper.map(user);
    }

    public UserDTO save(UserCreateUpdateDTO userCreateUpdateDTO) {
        var user = userMapper.map(userCreateUpdateDTO);
        userDAO.save(user);
        return userMapper.map(user);
    }

    public void deleteById(Integer id) {
        userDAO.deleteById(id);
    }
}
