package com.vpactually.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dao.UserDAO;
import com.vpactually.dto.users.UserCreateUpdateDTO;
import com.vpactually.mappers.MyObjectMapper;
import com.vpactually.mappers.UserMapper;
import com.vpactually.services.UserService;
import com.vpactually.util.DependencyContainer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.openapitools.jackson.nullable.JsonNullable;

import java.io.IOException;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private static final UserService userService = new UserService(
            DependencyContainer.getInstance().getDependency(UserDAO.class),
            DependencyContainer.getInstance().getDependency(UserMapper.class));
    private final ObjectMapper om = MyObjectMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var users = userService.findAll();
        resp.getWriter().write(om.writeValueAsString(users));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = new UserCreateUpdateDTO();
        user.setPassword(JsonNullable.of(req.getParameter("password")));
        user.setEmail(JsonNullable.of(req.getParameter("email")));
        user.setName(JsonNullable.of(req.getParameter("name")));
        userService.save(user);
        resp.getWriter().write(om.writeValueAsString(user));

    }
}
