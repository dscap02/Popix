package com.popx.presentazione;

import com.popx.modello.*;
import com.popx.persistenza.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CheckoutServlet", value = "/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    private CarrelloDAO carrelloDAO = new CarrelloDAOImpl();
    private OrdineDAO ordineDAO = new OrdineDAOImpl();
    private RigaOrdineDAO rigaOrdineDAO = new RigaOrdineDAOImpl();
    private ProdottoDAO prodottoDAO = new ProdottoDAOImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userEmail = (String) request.getSession().getAttribute("userEmail");

        if (userEmail == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/html");
            response.getWriter().write("<script>alert('Utente non autenticato');</script>");
            return;
        }

        HttpSession session = request.getSession();
        List<ProdottoBean> prodottoBeans = (List<ProdottoBean>) session.getAttribute("cart");

        if (prodottoBeans == null || prodottoBeans.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/html");
            response.getWriter().write("<script>alert('Il carrello è vuoto.');</script>");
            return;
        }

        double subtotal = 0;
        for (ProdottoBean prodotto : prodottoBeans) {
            subtotal += prodotto.getQty() * prodotto.getCost();
        }

        OrdineBean ordine = new OrdineBean((float) subtotal, userEmail, new Date(System.currentTimeMillis()));

        try {
            ordineDAO.insertOrdine(ordine);
            int ordineId = ordine.getId();

            for (ProdottoBean prodotto : prodottoBeans) {
                RigaOrdineBean rigaOrdine = new RigaOrdineBean(
                        ordineId,
                        prodotto.getId(),
                        prodotto.getQty(),
                        (float) prodotto.getCost()
                );
                rigaOrdineDAO.addRigaOrdine(rigaOrdine);

                // Aggiorna lo stock utilizzando il DAO
                int newQty = prodotto.getPiecesInStock() - prodotto.getQty();
                prodottoDAO.updateStock(prodotto.getId(), newQty);
            }

            carrelloDAO.clearCartByUserEmail(userEmail);
            session.setAttribute("cart", null);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/html");
            String contextPath = request.getContextPath();
            response.getWriter().write("<script>alert('Ordine completato con successo!');setTimeout(function(){ window.location.href = '" + contextPath + "/jsp/HomePage.jsp'; }, 1500);</script>");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/html");
            response.getWriter().write("<script>alert('Errore interno nel completamento dell'ordine.');</script>");
        }
    }
}
