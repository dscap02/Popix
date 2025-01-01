package com.popx.persistenza;

import com.popx.modello.ProdottoBean;

import java.util.List;

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
}

