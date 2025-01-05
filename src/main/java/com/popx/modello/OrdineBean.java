package com.popx.modello;

import java.sql.Date;

public class OrdineBean {
    private int id;
    private float subtotal;
    private String customerEmail;
    private String status;
    private Date dataOrdine;

    // Costruttori
    public OrdineBean() {}

    public OrdineBean(int id, float subtotal, String customerEmail, String status, Date dataOrdine) {
        this.id = id;
        this.subtotal = subtotal;
        this.customerEmail = customerEmail;
        this.status = status;
        this.dataOrdine = dataOrdine;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(Date dataOrdine) {
        this.dataOrdine = dataOrdine;
    }
}

