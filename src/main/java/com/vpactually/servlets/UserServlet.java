package com.vpactually.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.users.UserCreateUpdateDTO;
import com.vpactually.mappers.MyObjectMapper;
import com.vpactually.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.openapitools.jackson.nullable.JsonNullable;

import java.io.IOException;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private static final UserService userService = UserService.getInstance();
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
