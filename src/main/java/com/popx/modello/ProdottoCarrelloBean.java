package com.popx.modello;

public class ProdottoCarrelloBean {
    private String clienteEmail;  // Chiave esterna a Carrello
    private String prodottoId;    // Chiave esterna a Prodotto
    private int quantity;
    private float unitaryCost;

    // Costruttori, getter e setter
    public ProdottoCarrelloBean() {}

    public ProdottoCarrelloBean(String clienteEmail, String prodottoId, int quantity, float unitaryCost) {
        this.clienteEmail = clienteEmail;
        this.prodottoId = prodottoId;
        this.quantity = quantity;
        this.unitaryCost = unitaryCost;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public String getProdottoId() {
        return prodottoId;
    }

    public void setProdottoId(String prodottoId) {
        this.prodottoId = prodottoId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUnitaryCost() {
        return unitaryCost;
    }

    public void setUnitaryCost(float unitaryCost) {
        this.unitaryCost = unitaryCost;
    }
}
