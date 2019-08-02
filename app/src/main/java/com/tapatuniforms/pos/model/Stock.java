package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Stock {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int productId;
    private ArrayList<String> displayStockList;
    private ArrayList<String> warehouseStockList;

    public Stock(int productId, ArrayList<String> displayStockList, ArrayList<String> warehouseStockList) {
        this.productId = productId;
        this.displayStockList = displayStockList;
        this.warehouseStockList = warehouseStockList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public ArrayList<String> getDisplayStockList() {
        return displayStockList;
    }

    public void setDisplayStockList(ArrayList<String> displayStockList) {
        this.displayStockList = displayStockList;
    }

    public ArrayList<String> getWarehouseStockList() {
        return warehouseStockList;
    }

    public void setWarehouseStockList(ArrayList<String> warehouseStockList) {
        this.warehouseStockList = warehouseStockList;
    }
}
