package com.tapatuniforms.pos.model;

public class Activities {
    private String status;
    private String name;
    private String studentId;
    private String date;
    private String address;
    private String motherName;
    private int items;

    public Activities(String status, String name, String studentId, String date, String address, String motherName, int items) {
        this.status = status;
        this.name = name;
        this.studentId = studentId;
        this.date = date;
        this.address = address;
        this.motherName = motherName;
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
}
