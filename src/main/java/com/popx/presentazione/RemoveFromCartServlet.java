package com.popx.presentazione;

import com.popx.modello.ProdottoBean;
import com.popx.persistenza.ProdottoDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");

        HttpSession session = request.getSession();
        List<ProdottoBean> cart = (List<ProdottoBean>) session.getAttribute("cart");
        ProdottoDAOImpl prodottoDao = new ProdottoDAOImpl();

        // Recupera l'email dell'utente
        String userEmail = (String) session.getAttribute("userEmail");



        try {
            if (productId != null && !productId.isEmpty() && cart != null) {


                // Rimuovi il prodotto dal carrello in sessione
                cart.removeIf(product -> product.getId().equals(productId));
                session.setAttribute("cart", cart);

                // Se l'utente è loggato, rimuovi il prodotto anche dal database
                if (userEmail != null) {
                    prodottoDao.removeProductFromCart(userEmail, productId);
                }

                response.setContentType("application/json");
                response.getWriter().write("{\"success\": true}");
            } else {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid product ID.\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Errore nella rimozione del prodotto.\"}");
        }
    }
}
