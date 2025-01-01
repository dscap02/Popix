package com.popx.presentazione;

import com.popx.modello.ProdottoBean;
import com.popx.persistenza.ProdottoDAO;
import com.popx.persistenza.ProdottoDAOImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetProductsServlet", urlPatterns = {"/getProductsServlet"})
public class GetProductsServlet extends HttpServlet {

    private final ProdottoDAO prodottoDAO = new ProdottoDAOImpl();
    private static final int PRODUCTS_PER_PAGE = 6;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String category = request.getParameter("category");
        String price = request.getParameter("price");
        String pageParam = request.getParameter("page");

        // Impostare la pagina corrente
        int currentPage = 1;
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        List<ProdottoBean> prodotti = new ArrayList<>();
        try {
            // Applicare i filtri di categoria e prezzo
            if (category != null && !category.isEmpty()) {
                if (price != null && !price.isEmpty()) {
                    boolean ascending = "low".equalsIgnoreCase(price);
                    prodotti = prodottoDAO.getProdottiByBrandAndPrice(category, ascending);
                } else {
                    prodotti = prodottoDAO.getProdottiByBrand(category);
                }
            } else if (price != null && !price.isEmpty()) {
                boolean ascending = "low".equalsIgnoreCase(price);
                prodotti = prodottoDAO.getProdottiSortedByPrice(ascending);
            } else {
                prodotti = prodottoDAO.getAllProducts(); // Se non ci sono filtri, recupera tutti i prodotti
            }

            // Paginazione
            int totalProducts = prodotti.size();
            int totalPages = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);

            // Assicurarsi che la pagina corrente non superi il numero massimo di pagine
            if (currentPage > totalPages) {
                currentPage = totalPages;
            }

            // Calcolare i prodotti da mostrare per la pagina corrente
            int start = (currentPage - 1) * PRODUCTS_PER_PAGE;
            int end = Math.min(start + PRODUCTS_PER_PAGE, totalProducts);

            List<ProdottoBean> productsForPage = prodotti.subList(start, end);

            // Aggiungere gli attributi alla request per essere usati nella JSP
            request.setAttribute("products", productsForPage);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);

            // Invia la richiesta alla JSP
            request.getRequestDispatcher("/jsp/Catalog.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il recupero dei prodotti.");
        }
    }
}