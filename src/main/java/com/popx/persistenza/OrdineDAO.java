package com.popx.persistenza;

import com.popx.modello.OrdineBean;

import java.util.List;

public interface OrdineDAO {
    void insertOrdine(OrdineBean ordine);
    OrdineBean getOrdineById(int id);
    List<OrdineBean> getAllOrdini();
    List<OrdineBean> getOrdiniByCliente(String clienteEmail);
}

