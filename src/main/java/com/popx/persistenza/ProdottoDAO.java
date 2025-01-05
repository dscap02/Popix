package com.popx.persistenza;

import com.popx.modello.ProdottoBean;

import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpSession;

public interface ProdottoDAO {


        // Salva un prodotto
        boolean saveProdotto(ProdottoBean prodotto);

        // Recupera prodotto per id
        ProdottoBean getProdottoById(String id);

        List<ProdottoBean> getAllProducts();
        // Recupera prodotti per brand
        List<ProdottoBean> getProdottiByBrand(String brand);

        // Recupera prodotto per brand e ordine di prezzo
        List<ProdottoBean> getProdottiByBrandAndPrice(String brand, boolean ascending);

        // Recupera prodotto per ordine di prezzo
        List<ProdottoBean> getProdottiSortedByPrice(boolean ascending);

        byte[] getProductImageById(String id);

        List<ProdottoBean> getRandomProducts(int limit) throws SQLException;

        void updateProductQtyInCart(HttpSession session, String productId, int qty);
        int getProductQtyInCart(HttpSession session, String productId);

        void saveCartToDatabase(String userEmail, List<ProdottoBean> cart) throws SQLException;

        List<ProdottoBean> getCartByUserEmail(String userEmail) throws SQLException;

        void updateCartProductQuantityInDatabase(String userEmail, String productId, int qty) throws SQLException;

        void removeProductFromCart(String userEmail, String productId) throws SQLException;

        void deleteProductById(String id) throws SQLException;

        boolean updateProduct(ProdottoBean prodottoBean);


        void updateStock(String productId, int quantity) throws SQLException;
}



