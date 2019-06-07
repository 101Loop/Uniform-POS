package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SubOrder {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String productCode;
    private double price;
    private int quantity;
    private int orderId;
    private double gst;
    private double sgst;
    private double igst;
    private boolean isSynced;

    public SubOrder(int id, String name, String productCode, double price, int quantity,
                    int orderId, double gst, double sgst, double igst, boolean isSynced) {
        this.id = id;
        this.name = name;
        this.productCode = productCode;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
        this.gst = gst;
        this.sgst = sgst;
        this.igst = igst;
        this.isSynced = isSynced;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
