<%@ page import="com.popx.persistenza.UserDAO" %>
<%@ page import="com.popx.modello.UserBean" %>
<%@ page import="com.popx.persistenza.UserDAOImpl" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/logo-noborderico.png">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles/style-add.css">
<script src="https://kit.fontawesome.com/892069e9ac.js" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>var contextPath = '<%= request.getContextPath()%>'; </script>
<script src="${pageContext.request.contextPath}/scripts/addProd.js"></script>
<title>Admin-Aggiungi</title>
</head>
<body>

<%@include file="/resources/templates/header.jsp"%>
<%
    String email = (String) session.getAttribute("userEmail");

    if(email != null) {
        UserDAO<UserBean> userDAO = new UserDAOImpl();
        UserBean userBean = userDAO.getUserByEmail(email);

        if(!userBean.getRole().equals("Admin")){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
    }
%>

<h2>Aggiungi un prodotto</h2>
<form id="productForm" class="form-horizontal" enctype="multipart/form-data" accept-charset="UTF-8" method="post" action="${pageContext.request.contextPath}/addProductServlet">
    <div class="form-row">
        <div class="form-group">
            <label for="name">Nome</label>
            <input type="text" id="name" name="name" required>
        </div>
        <div class="form-group">
            <label for="idProduct">ID</label>
            <input type="text" id="idProduct" name="idProduct" required>
        </div>
        <div class="form-group">
            <label for="price">Prezzo</label>
            <input type="number" id="price" name="price" step="0.01" required>
        </div>
    </div>
    <div class="form-row">
        <div class="form-group">
            <label for="brand">Brand</label>
            <input type="text" id="brand" name="brand" required>
        </div>
        <div class="form-group">
            <label for="figure">Personaggio</label>
            <input type="text" id="figure" name="figure" required>
        </div>
    </div>
    <div class="form-row">
        <div class="form-group">
            <label for="qty">Quantità</label>
            <input type="number" id="qty" name="qty" required>
        </div>
        <div class="form-group">
            <label for="img_src">Immagine</label>
            <input type="file" id="img_src" name="img_src" accept="image/*" required>
        </div>
    </div>
    <div class="form-row">
        <div class="form-group full-width">
            <label for="description">Descrizione</label>
            <textarea id="description" name="description" rows="4" required></textarea>
        </div>
    </div>
    <div class="form-row">
        <button type="submit">Submit</button>
    </div>
</form>

<%@include file="/resources/templates/footer.jsp"%>

</body>
</html>
