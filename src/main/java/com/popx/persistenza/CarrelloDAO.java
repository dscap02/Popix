package com.popx.persistenza;

import com.popx.modello.CarrelloBean;
import com.popx.modello.ProdottoBean;

import java.util.List;
import java.util.Map;

public interface CarrelloDAO {
    void salvaCarrello(String clienteEmail, List<ProdottoBean> carrello, Map<String, Integer> quantitaMap);
    Map<String, Integer> recuperaCarrello(String clienteEmail);
    void rimuoviCarrello(String clienteEmail);
}
