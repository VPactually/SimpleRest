package com.vpactually.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.users.UserCreateUpdateDTO;
import com.vpactually.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.openapitools.jackson.nullable.JsonNullable;

import java.io.IOException;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private UserService userService;
    private ObjectMapper om;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = (UserService) getServletContext().getAttribute("userService");
        om = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            resp.getWriter().write(om.writeValueAsString(userService.findById(Integer.parseInt(pathVariable[2]))
                    .orElseThrow()));
            return;
        }
        var users = userService.findAll();
        resp.getWriter().write(om.writeValueAsString(users));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getRequestURI().equals("/users")) {
            var createUpdateDTO = createUser(req);
            var user = userService.save(createUpdateDTO);
            resp.getWriter().write(om.writeValueAsString(user));
        } else {
            doPut(req, resp);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            var id = Integer.parseInt(pathVariable[2]);
            var createUpdateDTO = createUser(req);
            var user = userService.update(createUpdateDTO, id);
            resp.getWriter().write(om.writeValueAsString(user));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            var id = Integer.parseInt(pathVariable[2]);
            userService.deleteById(id);
        }
    }

    private UserCreateUpdateDTO createUser(HttpServletRequest req) {
        var createUpdateDTO = new UserCreateUpdateDTO();
        createUpdateDTO.setEmail(JsonNullable.of(req.getParameter("email")));
        createUpdateDTO.setName(JsonNullable.of(req.getParameter("name")));
        createUpdateDTO.setPassword(JsonNullable.of(req.getParameter("password")));
        return createUpdateDTO;
    }
}
