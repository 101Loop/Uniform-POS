package com.tapatuniforms.pos.model;

public class Product {
    private int id;
    private String name;
    private int image;
    private String type;
    private String gender;
    private String size;
    private double price;

    public Product(int id, String name, int image, String type, String gender, String size, double price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
        this.gender = gender;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
