package com.popx.presentazione;

import com.popx.modello.ProdottoBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");

        HttpSession session = request.getSession();
        List<ProdottoBean> cart = (List<ProdottoBean>) session.getAttribute("cart");

        if (productId != null && !productId.isEmpty() && cart != null) {
            cart.removeIf(product -> product.getId().equals(productId));
            session.setAttribute("cart", cart);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
        } else {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid product ID.\"}");
        }
    }
}
