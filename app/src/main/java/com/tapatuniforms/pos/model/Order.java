package com.tapatuniforms.pos.model;

import java.util.ArrayList;
import java.util.Date;

public class Order {
    private int id;
    private String custName;
    private String custMobile;
    private String custEmail;
    private String invoiceNo;
    private double orderTotal;
    private Date date;
    private ArrayList<SubOrder> subOrderList;

    public Order(int id, String custName, String custMobile, String custEmail, String invoiceNo, double orderTotal, Date date, ArrayList<SubOrder> subOrderList) {
        this.id = id;
        this.custName = custName;
        this.custMobile = custMobile;
        this.custEmail = custEmail;
        this.invoiceNo = invoiceNo;
        this.orderTotal = orderTotal;
        this.date = date;
        this.subOrderList = subOrderList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<SubOrder> getSubOrderList() {
        return subOrderList;
    }

    public void setSubOrderList(ArrayList<SubOrder> subOrderList) {
        this.subOrderList = subOrderList;
    }
}
