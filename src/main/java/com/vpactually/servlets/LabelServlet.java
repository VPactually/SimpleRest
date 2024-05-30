package com.vpactually.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.labels.LabelCreateUpdateDTO;
import com.vpactually.services.LabelService;
import com.vpactually.util.Generated;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.openapitools.jackson.nullable.JsonNullable;

import java.io.IOException;

@WebServlet("/labels/*")
public class LabelServlet extends HttpServlet {


    private LabelService labelService;
    private ObjectMapper om;

    @Generated
    @Override
    public void init() throws ServletException {
        super.init();
        labelService = (LabelService) getServletContext().getAttribute("labelService");
        om = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            resp.getWriter().write(om.writeValueAsString(labelService.findById(Integer.parseInt(pathVariable[2]))
                    .orElseThrow()));
            return;
        }
        var labels = labelService.findAll();
        resp.getWriter().write(om.writeValueAsString(labels));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals("/labels")) {
            var createUpdateDTO = createLabel(req);
            var label = labelService.save(createUpdateDTO);
            resp.getWriter().write(om.writeValueAsString(label));
        } else {
            doPut(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            var id = Integer.parseInt(pathVariable[2]);
            var createUpdateDTO = createLabel(req);
            var label = labelService.update(createUpdateDTO, id);
            resp.getWriter().write(om.writeValueAsString(label));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            var id = Integer.parseInt(pathVariable[2]);
            labelService.deleteById(id);
        }
    }

    @Generated
    private LabelCreateUpdateDTO createLabel(HttpServletRequest req) {
        var createUpdateDTO = new LabelCreateUpdateDTO();
        createUpdateDTO.setName(JsonNullable.of(req.getParameter("name")));
        return createUpdateDTO;
    }
}
