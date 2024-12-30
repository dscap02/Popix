<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 30/12/2024
  Time: 17:08
  To change this template use File | Settings | File Templates.
--%>
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
        <a href="${pageContext.request.contextPath}#">Prodotti</a>
    </nav>
    <div class="icons">
        <a href="#" class="fas fa-shopping-cart"></a>
        <a href="#" class="fas fa-sign-in-alt"></a>

    </div>
</header>
</body>
</html>
