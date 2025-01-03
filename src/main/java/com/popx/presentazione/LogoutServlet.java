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
import java.sql.SQLException;
import java.util.List;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private ProdottoDAO prodottoDAO = new ProdottoDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<ProdottoBean> cart = (List<ProdottoBean>) session.getAttribute("cart");

        if (cart != null) {
            String userEmail = (String) session.getAttribute("userEmail");

            if (userEmail != null) {
                try {
                    prodottoDAO.saveCartToDatabase(userEmail, cart);
                    session.removeAttribute("cart");
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Errore durante il salvataggio del carrello.");
                    return;
                }
            }
        }

        session.invalidate();  // Invalidare la sessione e quindi il logout
        response.sendRedirect(request.getContextPath() + "/jsp/HomePage.jsp");  // Redirige alla HomePage

    }
}
