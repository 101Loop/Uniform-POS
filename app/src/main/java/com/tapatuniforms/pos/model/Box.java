package com.tapatuniforms.pos.model;

public class Box {
    private long id;
    private String name;
    private String serialNumber;
    private String dateTime;
    private int numberOfItems;
    private int itemsVerified;

    public Box(long id, String name, String serialNumber, String dateTime, int numberOfItems, int itemsVerified) {
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
        this.dateTime = dateTime;
        this.numberOfItems = numberOfItems;
        this.itemsVerified = itemsVerified;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public int getItemsVerified() {
        return itemsVerified;
    }

    public void setItemsVerified(int itemsVerified) {
        this.itemsVerified = itemsVerified;
    }
}
