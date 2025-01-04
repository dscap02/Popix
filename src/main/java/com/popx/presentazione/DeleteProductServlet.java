package com.popx.presentazione;

import com.popx.persistenza.ProdottoDAO;
import com.popx.persistenza.ProdottoDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/DeleteProductServlet")
public class DeleteProductServlet extends HttpServlet {

    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        prodottoDAO = new ProdottoDAOImpl();  // Instanza del DAO per interagire con il database
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("id");

        try {
            prodottoDAO.deleteProductById(productId);  // Metodo per eliminare il prodotto
            response.setStatus(HttpServletResponse.SC_OK); // HTTP OK
            response.getWriter().write("{\"success\": true, \"message\": \"Prodotto eliminato con successo.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // HTTP Internal Server Error
            response.getWriter().write("{\"success\": false, \"message\": \"Errore nell'eliminazione del prodotto.\"}");
        }
    }
}
