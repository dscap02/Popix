package com.popx.presentazione;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalida la sessione corrente
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }

        // Reindirizza alla homepage o alla pagina di login
        response.sendRedirect(request.getContextPath() + "/jsp/HomePage.jsp");
    }
}

