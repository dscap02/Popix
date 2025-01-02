package com.popx.modello;

import java.io.Serializable;
import java.util.Arrays;

public class ProdottoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private double cost;
    private int piecesInStock;
    private String brand;
    private byte[] img;
    private String figure;

    private int qty;

    // Constructors

    public ProdottoBean() {
    }

    public ProdottoBean(String id, String name, String description, double cost, int piecesInStock, String brand, byte[] img, String figure) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.piecesInStock = piecesInStock;
        this.brand = brand;
        this.img = img;
        this.figure = figure;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getPiecesInStock() {
        return piecesInStock;
    }

    public void setPiecesInStock(int piecesInStock) {
        this.piecesInStock = piecesInStock;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "ProdottoBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", piecesInStock=" + piecesInStock +
                ", brand='" + brand + '\'' +
                ", img=" + Arrays.toString(img) +
                ", figure='" + figure + '\'' +
                '}';
    }
}
