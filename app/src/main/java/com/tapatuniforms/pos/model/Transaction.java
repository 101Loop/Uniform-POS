package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String paymentOption;
    private double amount;
    private long orderId;
    private boolean isSynced;

    public Transaction(long id, String paymentOption, double amount, long orderId, boolean isSynced) {
        this.id = id;
        this.paymentOption = paymentOption;
        this.amount = amount;
        this.orderId = orderId;
        this.isSynced = isSynced;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
