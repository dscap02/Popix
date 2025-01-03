package com.popx.presentazione;

import com.popx.modello.ProdottoBean;
import com.popx.persistenza.ProdottoDAO;
import com.popx.persistenza.ProdottoDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");

        ProdottoDAO prodottoDAO = new ProdottoDAOImpl();

        int quantity = Integer.parseInt(request.getParameter("quantity"));
        HttpSession session = request.getSession();
        List<ProdottoBean> cart = (List<ProdottoBean>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        ProdottoBean prodotto = prodottoDAO.getProdottoById(productId);

        if (prodotto != null) {
            boolean productExists = false;
            for (ProdottoBean cartItem : cart) {
                if (cartItem.getId().equals(prodotto.getId())) {
                    if ((prodottoDAO.getProductQtyInCart(session,cartItem.getId())+quantity) <= prodotto.getPiecesInStock()) {
                        prodottoDAO.updateProductQtyInCart(session, cartItem.getId(), quantity+ prodottoDAO.getProductQtyInCart(session, cartItem.getId()));  // Utilizza il metodo dedicato
                        productExists = true;
                        break;
                    } else {
                        response.setContentType("application/json");
                        response.getWriter().write("{\"success\": false, \"message\": \"Quantità non disponibile nel magazzino.\"}");
                        return;
                    }
                }
            }
            if (!productExists) {
                cart.add(prodotto);  // Imposta la quantità direttamente nel carrello per un nuovo prodotto
                prodottoDAO.updateProductQtyInCart(session, prodotto.getId(), quantity);  // Usa il metodo dedicato per aggiungere al carrello
            }

            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"message\": \"Prodotto aggiunto al carrello!\"}");
        } else {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Errore nell'aggiungere il prodotto al carrello.\"}");
        }

    }
}
