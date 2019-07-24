package com.tapatuniforms.pos.model;

public class Indent {
    private long id;
    private String name;
    private String dateTime;
    private String dispatchPerson;
    private int numberOfBoxes;
    private double boxValue;
    private int numberOfItems;
    private boolean isSelected;
    private Box box;
    private String shippingFrom;

    public Indent(long id, String name, String dateTime, String dispatchPerson, int numberOfBoxes, double boxValue, int numberOfItems, Box box, String shippingFrom) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.dispatchPerson = dispatchPerson;
        this.numberOfBoxes = numberOfBoxes;
        this.boxValue = boxValue;
        this.numberOfItems = numberOfItems;
        this.isSelected = false;
        this.box = box;
        this.shippingFrom = shippingFrom;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDispatchPerson() {
        return dispatchPerson;
    }

    public void setDispatchPerson(String dispatchPerson) {
        this.dispatchPerson = dispatchPerson;
    }

    public int getNumberOfBoxes() {
        return numberOfBoxes;
    }

    public void setNumberOfBoxes(int numberOfBoxes) {
        this.numberOfBoxes = numberOfBoxes;
    }

    public double getBoxValue() {
        return boxValue;
    }

    public void setBoxValue(double boxValue) {
        this.boxValue = boxValue;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public String getShippingFrom() {
        return shippingFrom;
    }

    public void setShippingFrom(String shippingFrom) {
        this.shippingFrom = shippingFrom;
    }
}
