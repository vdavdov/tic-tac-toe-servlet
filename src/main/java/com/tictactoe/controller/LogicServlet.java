package com.tictactoe.controller;

import com.tictactoe.service.Field;
import com.tictactoe.model.Sign;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/logic", name = "LogicServlet")
public class LogicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        Field field = extractField(session);
        int index = getSelectedIndex(request);

        Sign currentSign = field.getField().get(index);
        if (currentSign != Sign.EMPTY) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
            requestDispatcher.forward(request, response);
            return;
        }

        field.getField().put(index, Sign.CROSS);
        if (checkWin(response, session, field)) {
            return;
        }

        int emptyFieldIndex = field.getEmptyFieldIndex();

        if (emptyFieldIndex >= 0) {
            field.getField().put(emptyFieldIndex, Sign.NOUGHT);
        } else {
            session.setAttribute("draw", true);
            List<Sign> data = field.getFieldData();
            session.setAttribute("data", data);

            response.sendRedirect("/index.jsp");
            return;
        }
        if (checkWin(response, session, field)) {
            return;
        }

        List<Sign> data = field.getFieldData();
        session.setAttribute("data", data);
        session.setAttribute("field", field);


        response.sendRedirect("/index.jsp");
    }
    private int getSelectedIndex(HttpServletRequest request) {
        String click = request.getParameter("click");
        boolean isNumeric = click.chars().allMatch(Character::isDigit);
        return isNumeric ? Integer.parseInt(click) : 0;
    }

    private Field extractField(HttpSession session) {
        Object field = session.getAttribute("field");
        if (Field.class != field.getClass()) {
            session.invalidate();
            throw new RuntimeException("This session is broken, try one more time!");
        }
        return (Field) field;
    }

    private boolean checkWin(HttpServletResponse response, HttpSession session, Field field) throws IOException {
        Sign winner = field.checkWin();
        if (Sign.CROSS == winner || Sign.NOUGHT == winner) {
            session.setAttribute("winner", winner);
            List<Sign> data = field.getFieldData();
            session.setAttribute("data", data);

            response.sendRedirect("/index.jsp");
            return true;
        }
        return false;
    }
}
