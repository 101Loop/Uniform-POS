package com.tapatuniforms.pos.model;

public class Transaction {
    private String paymentOptionName;
    private String paymentOption;
    private double amount;

    public Transaction(String paymentOptionName, String paymentOption, double amount) {
        this.paymentOptionName = paymentOptionName;
        this.paymentOption = paymentOption;
        this.amount = amount;
    }

    public String getPaymentOptionName() {
        return paymentOptionName;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public double getAmount() {
        return amount;
    }
}
