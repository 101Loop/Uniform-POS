package com.tapatuniforms.pos.model;

public class DashboardItem {
    private int image;
    private String name;
    private int itemId;

    public DashboardItem(int image, String name, int itemId) {
        this.image = image;
        this.name = name;
        this.itemId = itemId;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
