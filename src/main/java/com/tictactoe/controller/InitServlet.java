package com.tictactoe.controller;

import com.tictactoe.service.Field;
import com.tictactoe.model.Sign;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(value = "/start", name = "InitServlet")
public class InitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        Field field = new Field();
        Map<Integer, Sign> fieldData = field.getField();

        List<Sign> data = field.getFieldData();

        session.setAttribute("field", field);
        session.setAttribute("data", data);

        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
