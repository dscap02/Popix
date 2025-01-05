package com.popx.modello;

import java.io.Serializable;

public class RigaOrdineBean implements Serializable {
    private int ordineId;
    private String prodottoId;
    private int quantity;
    private float unitaryCost;

    // Costruttori
    public RigaOrdineBean() {}

    public RigaOrdineBean(int ordineId, String prodottoId, int quantity, float unitaryCost) {
        this.ordineId = ordineId;
        this.prodottoId = prodottoId;
        this.quantity = quantity;
        this.unitaryCost = unitaryCost;
    }

    // Getter e Setter
    public int getOrdineId() {
        return ordineId;
    }

    public void setOrdineId(int ordineId) {
        this.ordineId = ordineId;
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

    @Override
    public String toString() {
        return "RigaOrdineBean{" +
                "ordineId=" + ordineId +
                ", prodottoId='" + prodottoId + '\'' +
                ", quantity=" + quantity +
                ", unitaryCost=" + unitaryCost +
                '}';
    }
}
