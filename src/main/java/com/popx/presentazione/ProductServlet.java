package com.popx.presentazione;

import com.google.gson.JsonObject;
import com.popx.modello.ProdottoBean;
import com.popx.persistenza.DataSourceSingleton;
import com.popx.persistenza.ProdottoDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/addProductServlet")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024) // Max file size 2MB
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl();

    public ProductServlet() {
        this.prodottoDAO = new ProdottoDAOImpl(DataSourceSingleton.getInstance()); // Usa il DataSource dal singleton
    }


        @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();

        try {
            // Retrieve and validate form data
            String id = request.getParameter("idProduct");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String costStr = request.getParameter("price");
            String piecesInStockStr = request.getParameter("qty");
            String brand = request.getParameter("brand");
            String figure = request.getParameter("figure");
            Part imgPart = request.getPart("img_src");

            // Validate required fields
            if (id == null || id.isEmpty() ||
                    name == null || name.isEmpty() ||
                    costStr == null || costStr.isEmpty() ||
                    piecesInStockStr == null || piecesInStockStr.isEmpty()) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Campi obbligatori mancanti.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            double cost = Double.parseDouble(costStr);
            int piecesInStock = Integer.parseInt(piecesInStockStr);

            byte[] imgBytes = null;
            if (imgPart != null && imgPart.getContentType().startsWith("image/")) {
                try (InputStream imgInputStream = imgPart.getInputStream()) {
                    imgBytes = imgInputStream.readAllBytes();
                }
            } else if (imgPart != null) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Il file caricato non è un'immagine valida.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            // Create and save product
            ProdottoBean prodotto = new ProdottoBean(id, name, description, cost, piecesInStock, brand, imgBytes, figure);
            boolean isSaved = prodottoDAO.saveProdotto(prodotto);

            if (isSaved) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Prodotto aggiunto con successo.");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Errore durante il salvataggio del prodotto.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Errore interno: " + e.getMessage());
        }

        response.getWriter().write(jsonResponse.toString());
    }
}
