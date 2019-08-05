package com.tapatuniforms.pos.model;

//position, size and price were added later due to API modification
public class CartItem {
    private int id;
    private int quantity;
    private ProductHeader productHeader;
    private String size;
    private double price;

    public CartItem(int id, int quantity, ProductHeader productHeader, String size, double price) {
        this.id = id;
        this.quantity = quantity;
        this.productHeader = productHeader;
        this.size = size;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductHeader getProductHeader() {
        return productHeader;
    }

    public void setProductHeader(ProductHeader productHeader) {
        this.productHeader = productHeader;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
