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

@WebServlet(name = "GetAdminServlet", urlPatterns = {"/getAdminServlet"})
public class GetAdminServlet extends HttpServlet {

    private final ProdottoDAO prodottoDAO = new ProdottoDAOImpl();
    private static final int PRODUCTS_PER_PAGE = 6;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProdottoBean> prodotti = new ArrayList<>(); // Recupera tutti i prodotti

        prodotti = prodottoDAO.getAllProducts();


        if (prodotti != null) {
            int totalProducts = prodotti.size();
            int totalPages = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);
            int currentPage = 1;

            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) currentPage = 1;
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }

            int start = (currentPage - 1) * PRODUCTS_PER_PAGE;
            int end = Math.min(start + PRODUCTS_PER_PAGE, totalProducts);

            List<ProdottoBean> productsForPage = prodotti.subList(start, end);


            // Aggiungere gli attributi alla request per essere usati nella JSP
            request.setAttribute("products", productsForPage);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);

            // Invia la richiesta alla JSP
            request.getRequestDispatcher("/jsp/DashboardAdmin.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il recupero dei prodotti.");
        }
    }
}
