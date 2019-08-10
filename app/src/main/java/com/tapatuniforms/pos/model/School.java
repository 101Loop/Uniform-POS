package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONObject;

@Entity
public class School {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int apiId;
    private String name;
    private String address;
    private String latitude;
    private String longitude;

    public School(int id, int apiId, String name, String address, String latitude, String longitude) {
        this.id = id;
        this.apiId = apiId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public School(JSONObject jsonObject) {
        JSONObject schoolJSON = jsonObject.optJSONObject(APIStatic.Key.school);

        this.apiId = schoolJSON.optInt(APIStatic.Key.id);
        this.name = schoolJSON.optString(APIStatic.Key.name);
        this.address = schoolJSON.optString(APIStatic.Key.address);
        this.latitude = schoolJSON.optString(APIStatic.Key.latitude);
        this.longitude = schoolJSON.optString(APIStatic.Key.longitude);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
