package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Order {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long apiId;
    private String custName;
    private String custMobile;
    private String custEmail;
    private String invoiceNo;
    private double orderTotal;
    private double discount;
    private boolean isSynced;

    public Order(long id, long apiId, String custName, String custMobile, String custEmail,
                 String invoiceNo, double orderTotal, double discount, boolean isSynced) {
        this.id = id;
        this.apiId = apiId;
        this.custName = custName;
        this.custMobile = custMobile;
        this.custEmail = custEmail;
        this.invoiceNo = invoiceNo;
        this.orderTotal = orderTotal;
        this.discount = discount;
        this.isSynced = isSynced;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
