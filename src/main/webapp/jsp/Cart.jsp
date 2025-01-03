<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.popx.modello.ProdottoBean" %>
<%@ page import="com.popx.persistenza.ProdottoDAOImpl" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/logo-noborderico.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles/style-cart.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>var contextPath = '<%= request.getContextPath()%>'; </script>
    <script src="https://kit.fontawesome.com/892069e9ac.js" crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/scripts/updateCart.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/removeItem.js"></script>
    <title>Carrello</title>
</head>
<body>

<%@include file="/resources/templates/header.jsp"%>

<main>
    <div class="container">
        <section class="cart">
            <h2>Il tuo carrello</h2>
            <div class="cart-items">
                <%
                    ProdottoDAOImpl prodottoDao = new ProdottoDAOImpl();
                    List<ProdottoBean> cart = (List<ProdottoBean>) session.getAttribute("cart");
                    if (cart != null && !cart.isEmpty()) {
                        for (ProdottoBean product : cart) {
                %>
                <div class="cart-item">
                    <img src="<%= request.getContextPath() %>/getPictureServlet?id=<%= product.getId()%>" alt="Product image">
                    <div class="item-details">
                        <% System.out.println("Sono nel carrello e id è "+product.getId());%>
                        <h3><%= product.getName() %></h3>
                        <p>Disponibilità: <%= product.getPiecesInStock() %></p>
                        <div class="quantity-control">
                            <input type="number" class="quantity-input" value="<%= prodottoDao.getProductQtyInCart(session, product.getId()) %>"
                                   min="1" max="<%= product.getPiecesInStock() %>">
                            <button class="update-qty" data-id="<%= product.getId() %>">Aggiorna</button>
                        </div>
                        <p>Prezzo: $<%= product.getCost() %></p>
                    </div>
                    <button class="remove-item" data-id="<%= product.getId() %>"><i class="fas fa-trash-alt"></i></button>
                </div>
                <%
                    }
                } else {
                %>
                <p>Il tuo carrello è vuoto.</p>
                <%
                    }
                %>
            </div>
            <div class="cart-summary">
                <h3>Riepilogo</h3>
                <div class="summary-details">
                    <%
                        double sum = 0;
                        String formattedSum = "0.00";
                        if(cart != null) {
                            for (ProdottoBean product : cart) {
                                sum += product.getCost() * prodottoDao.getProductQtyInCart(session, product.getId());
                            }
                            formattedSum = String.format("%.2f", sum);
                        }
                    %>
                    <p id="sum"> Totale: <%= formattedSum %> </p>
                </div>
                <button class="checkout-btn" id="checkout">CHECKOUT</button>
            </div>
        </section>
    </div>
</main>

<%@include file="/resources/templates/footer.jsp"%>
</body>
</html>