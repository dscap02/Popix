package com.popx.presentazione;

import com.popx.persistenza.ProdottoDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");
        int qty;

        try {
            qty = Integer.parseInt(request.getParameter("qty"));
        } catch (NumberFormatException e) {
            qty = 1; // Quantità predefinita
        }

        HttpSession session = request.getSession();
        ProdottoDAOImpl prodottoDao = new ProdottoDAOImpl();

        try {
            prodottoDao.updateProductQtyInCart(session, productId, qty);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Errore nell'aggiornamento del carrello.\"}");
        }
    }
}
