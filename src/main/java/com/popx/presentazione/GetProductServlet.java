package com.popx.presentazione;

import com.popx.modello.ProdottoBean;
import com.popx.persistenza.ProdottoDAO;
import com.popx.persistenza.ProdottoDAOImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "GetProductServlet", urlPatterns = {"/getProduct"})
public class GetProductServlet extends HttpServlet {

    private final ProdottoDAO prodottoDAO = new ProdottoDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("id");

        if (productId == null || productId.isEmpty()) {
            // Se manca l'ID prodotto, restituisce un errore 400 Bad Request
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID prodotto non fornito.");
            return;
        }

        try {
            ProdottoBean prodotto = prodottoDAO.getProdottoById(productId);

            if (prodotto == null) {
                // Se il prodotto non esiste, restituisce un errore 404 Not Found
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prodotto non trovato.");
                return;
            }

            // Imposta il prodotto come attributo della richiesta e lo invia alla JSP
            request.setAttribute("prod", prodotto);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/product.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            // Gestisce eventuali errori interni
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il recupero del prodotto.");
        }
    }
}
