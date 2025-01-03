package com.popx.modello;

import java.util.List;

public class CarrelloBean {
    private String clienteEmail;
    private List<ProdottoCarrelloBean> prodottiCarrello;  // Relazione con ProdottiCarrello

    // Costruttori, getter e setter
    public CarrelloBean() {}

    public CarrelloBean(String clienteEmail, List<ProdottoCarrelloBean> prodottiCarrello) {
        this.clienteEmail = clienteEmail;
        this.prodottiCarrello = prodottiCarrello;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public List<ProdottoCarrelloBean> getProdottiCarrello() {
        return prodottiCarrello;
    }

    public void setProdottiCarrello(List<ProdottoCarrelloBean> prodottiCarrello) {
        this.prodottiCarrello = prodottiCarrello;
    }
}
