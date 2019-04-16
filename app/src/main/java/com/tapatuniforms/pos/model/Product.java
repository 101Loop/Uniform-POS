package com.tapatuniforms.pos.model;

public class Product {
    private int id;
    private String name;
    private int image;
    private String size;
    private double price;

    public Product(int id, String name, int image, String size, double price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.size = size;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
