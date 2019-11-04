package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONObject;

@Entity
public class School {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String address;
    private int city;

    public School(int id, String name, String address, int city) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
    }

    public School(JSONObject jsonObject) {
        this.id = jsonObject.optInt(APIStatic.Key.id);
        this.name = jsonObject.optString(APIStatic.Key.name);
        this.address = jsonObject.optString(APIStatic.Key.address);
        this.city = jsonObject.optInt(APIStatic.Key.city);
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

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }
}
