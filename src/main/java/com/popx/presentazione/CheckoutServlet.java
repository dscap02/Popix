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

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    private CarrelloDAO carrelloDAO = new CarrelloDAOImpl();
    private OrdineDAO ordineDAO = new OrdineDAOImpl();
    private RigaOrdineDAO rigaOrdineDAO = new RigaOrdineDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userEmail = (String) request.getSession().getAttribute("userEmail");

        if (userEmail == null) {
            // Utente non autenticato
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Utente non autenticato\"}");
            return;
        }

        HttpSession session = request.getSession();
        List<ProdottoBean> prodottoBeans = (List<ProdottoBean>) session.getAttribute("cart");

        if (prodottoBeans == null || prodottoBeans.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Il carrello è vuoto.\"}");
            return;
        }

        double subtotal = 0;
        for (ProdottoBean prodotto : prodottoBeans) {
            subtotal += prodotto.getQty() * prodotto.getCost();
        }

        OrdineBean ordine = new OrdineBean((float) subtotal, userEmail, new Date(System.currentTimeMillis()));

        ordineDAO.insertOrdine(ordine);

        // Recupera l'ID dell'ordine auto-generato
        int ordineId = ordine.getId();

        List<RigaOrdineBean> righeOrdine = new ArrayList<>();
        for (ProdottoBean prodotto : prodottoBeans) {
            RigaOrdineBean rigaOrdine = new RigaOrdineBean(
                    ordineId,
                    prodotto.getId(),
                    prodotto.getQty(),
                    (float) prodotto.getCost()
            );
            righeOrdine.add(rigaOrdine);
        }

        // Salva le righe dell'ordine
        for (RigaOrdineBean riga : righeOrdine) {
            rigaOrdineDAO.addRigaOrdine(riga);
        }

        // Svuota il carrello per l'utente
        carrelloDAO.clearCartByUserEmail(userEmail);

        // Invalidare la sessione del carrello
        session.setAttribute("cart", null);

        // Risposta positiva JSON
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\": \"Ordine completato con successo!\"}");
    }
}
