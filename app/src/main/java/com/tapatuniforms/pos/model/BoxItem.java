package com.tapatuniforms.pos.model;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONObject;

//no status, name, items in shelf, last status
public class BoxItem {
    private int id;
    private String status;
    private String name;
    private int product;
    private int numberOfItems;
    private int numberOfScannedItems;
    private int numberOfShelfItems;
    private int boxId;
    private String lastStatus;

    public BoxItem(JSONObject jsonObject) {
        this.id = jsonObject.optInt(APIStatic.Key.id);
        this.status = "";
        this.name = "";
        this.product = jsonObject.optInt(APIStatic.Key.product);
        this.numberOfItems = jsonObject.optInt(APIStatic.Key.numberOfItems);
        this.numberOfScannedItems = jsonObject.optInt(APIStatic.Key.numberOfScannedItems);
        this.numberOfShelfItems = 0;
        this.boxId = jsonObject.optInt(APIStatic.Key.box);
        this.lastStatus = "";
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

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public int getNumberOfScannedItems() {
        return numberOfScannedItems;
    }

    public void setNumberOfScannedItems(int numberOfScannedItems) {
        this.numberOfScannedItems = numberOfScannedItems;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public int getNumberOfShelfItems() {
        return numberOfShelfItems;
    }

    public void setNumberOfShelfItems(int numberOfShelfItems) {
        this.numberOfShelfItems = numberOfShelfItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }
}
