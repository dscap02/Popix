package com.popx.presentazione;

import com.google.gson.JsonObject;
import com.popx.modello.ProdottoBean;
import com.popx.persistenza.ProdottoDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/updateProductServlet")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024) // Max file size 2MB
public class UpdateProductServlet extends HttpServlet {

    private final ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();

        try {
            // Recupero e validazione dei dati del prodotto
            String idProduct = request.getParameter("idProduct");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int qty = Integer.parseInt(request.getParameter("qty"));
            String brand = request.getParameter("brand");
            String figure = request.getParameter("figure");
            Part imgPart = request.getPart("img_src");
            String currentImgSrc = request.getParameter("current_img_src");

            // Creazione del prodotto da aggiornare
            ProdottoBean prodotto = new ProdottoBean(idProduct, name, description, price, qty, brand, null, figure);

            if (imgPart != null && imgPart.getSize() > 0) {
                // Se è stata caricata una nuova immagine
                if (imgPart.getContentType().startsWith("image/")) {
                    try (InputStream imgInputStream = imgPart.getInputStream()) {
                        byte[] imgBytes = imgInputStream.readAllBytes();
                        prodotto.setImg(imgBytes);
                    }
                } else {
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "Il file caricato non è un'immagine valida.");
                    response.getWriter().write(jsonResponse.toString());
                    return;
                }
            } else if (currentImgSrc != null && !currentImgSrc.isEmpty()) {
                // Se non è stata caricata una nuova immagine, utilizza quella esistente
                ProdottoBean existingProduct = prodottoDAO.getProdottoById(idProduct);
                if (existingProduct != null) {
                    prodotto.setImg(existingProduct.getImg());
                }
            } else {
                // Nessuna immagine presente
                prodotto.setImg(null);
            }

            // Aggiornamento del prodotto nel database
            boolean isUpdated = prodottoDAO.updateProduct(prodotto);

            if (isUpdated) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Prodotto aggiornato con successo.");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Errore durante l'aggiornamento del prodotto.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Errore interno: " + e.getMessage());
        }

        response.getWriter().write(jsonResponse.toString());
    }
}
