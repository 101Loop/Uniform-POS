package com.tapatuniforms.pos.model;

public class CartItem {
    private int id;
    private int quantity;
    private Product product;
    private int position;

    public CartItem(int id, int quantity, Product product, int position) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.position = position;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
