package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONObject;

@Entity
public class School {
    @PrimaryKey
    private int id;
    private String name;
    private String address;
    private String city;
    private String state;

    public School(int id, String name, String address, String city, String state) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
    }

    public School(JSONObject jsonObject) {
        this.id = jsonObject.optInt(APIStatic.Key.id);
        this.name = jsonObject.optString(APIStatic.Key.name);
        this.address = jsonObject.optString(APIStatic.Key.address);
        this.city = jsonObject.optString(APIStatic.Key.city);
        this.state = jsonObject.optString(APIStatic.Key.state);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
