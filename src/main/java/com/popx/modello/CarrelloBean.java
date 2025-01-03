package com.popx.modello;


import java.util.Map;

public class CarrelloBean {
    private String id;
    private String clienteEmail;
    private Map<String, Integer> prodotti; // Prodotto ID e Quantità

    public CarrelloBean() {}

    public CarrelloBean(String id, String clienteEmail, Map<String, Integer> prodotti) {
        this.id = id;
        this.clienteEmail = clienteEmail;
        this.prodotti = prodotti;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public Map<String, Integer> getProdotti() {
        return prodotti;
    }

    public void setProdotti(Map<String, Integer> prodotti) {
        this.prodotti = prodotti;
    }
}

