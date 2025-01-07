package com.popx.presentazione;

import com.popx.modello.OrdineBean;
import com.popx.persistenza.OrdineDAO;
import com.popx.persistenza.OrdineDAOImpl;
import org.mockito.internal.matchers.Or;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/UpdateOrderStatus")
public class UpdateStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("id"));
        String newStatus = request.getParameter("status");



        OrdineDAO ordineDAO = new OrdineDAOImpl();

        OrdineBean ordineBean = ordineDAO.getOrdineById(orderId);

        ordineBean.setStatus(newStatus);

        try {
            boolean success = ordineDAO.updateStatus(ordineBean);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": " + success + "}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false}");
        }
    }
}

