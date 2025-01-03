package com.popx.presentazione;

import com.popx.persistenza.ProdottoDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

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

        // Recupera l'email dell'utente
        String userEmail = (String) session.getAttribute("userEmail");

        try {
            // Aggiorna la quantità del prodotto nel carrello in sessione
            prodottoDao.updateProductQtyInCart(session, productId, qty);

            // Se l'utente è loggato, aggiorna il carrello nel database
            if (userEmail != null) {
                prodottoDao.updateCartProductQuantityInDatabase(userEmail, productId, qty);
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Errore nell'aggiornamento del carrello.\"}");
        }
    }
}
