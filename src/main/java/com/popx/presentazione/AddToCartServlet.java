package com.popx.presentazione;

import com.popx.modello.ProdottoBean;
import com.popx.modello.UserBean;
import com.popx.persistenza.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {

    private DataSource DataSourceSingleton;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        ProdottoDAO prodottoDAO = new ProdottoDAOImpl();
        HttpSession session = request.getSession();
        String userEmail = (String) session.getAttribute("userEmail");

        // Controllo per utenti loggati
        if (userEmail != null) {
            UserDAO<UserBean> userDAO = new UserDAOImpl();
            try {
                UserBean userBean = userDAO.getUserByEmail(userEmail);

                if (userBean != null && !"User".equals(userBean.getRole())) {
                    response.setContentType("application/json");
                    response.getWriter().write("{\"success\": false, \"message\": \"Accesso negato: solo gli utenti con il ruolo 'User' possono aggiungere prodotti al carrello.\"}");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Errore interno durante il controllo dei permessi.\"}");
                return;
            }
        }

        // Gestione del carrello per utenti guest e utenti loggati con ruolo 'User'
        List<ProdottoBean> cart = (List<ProdottoBean>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        // Recupero del prodotto
        ProdottoBean prodotto = prodottoDAO.getProdottoById(productId);

        if (prodotto != null) {
            boolean productExists = false;
            for (ProdottoBean cartItem : cart) {
                if (cartItem.getId().equals(prodotto.getId())) {
                    int currentQty = prodottoDAO.getProductQtyInCart(session, cartItem.getId());
                    if ((currentQty + quantity) <= prodotto.getPiecesInStock()) {
                        prodottoDAO.updateProductQtyInCart(session, cartItem.getId(), currentQty + quantity);
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
                cart.add(prodotto);
                prodottoDAO.updateProductQtyInCart(session, prodotto.getId(), quantity);
            }

            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"message\": \"Prodotto aggiunto al carrello!\"}");
        } else {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Errore nell'aggiungere il prodotto al carrello.\"}");
        }
    }
}
