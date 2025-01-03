<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/header.css">
</head>
<body>
<header>
    <img src="${pageContext.request.contextPath}/resources/images/logo-noborderico.png" alt="Logo" class="header-photo">
    <input type="checkbox" id="toggler">
    <label for="toggler" class="fas fa-bars"></label>
    <a href="#" class="logo">Pop<span>!</span>x</a>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/jsp/HomePage.jsp">Home</a>
        <a href="${pageContext.request.contextPath}/getProductsServlet">Prodotti</a>
    </nav>
    <div class="icons">
        <a href="${pageContext.request.contextPath}/jsp/Cart.jsp" class="fas fa-shopping-cart"></a>
        <%
            Object role = session.getAttribute("role");
            if (role == null) {
        %>
        <!-- Icona per il login -->
        <a href="${pageContext.request.contextPath}/jsp/Login.jsp" class="fas fa-sign-in-alt"></a>
        <%
        } else {
        %>
        <!-- Icona per l'utente con ruolo -->
        <a href="${pageContext.request.contextPath}#" class="fas fa-user<%=
            role.equals("Gestore") ? "-cog" : role.equals("Admin") ? "-shield" : "" %>"></a>
        <!-- Icona per il logout -->
        <a href="${pageContext.request.contextPath}/logout" class="fas fa-sign-out-alt" title="Logout"></a>
        <%
            }
        %>
    </div>
</header>
</body>
</html>
