package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONObject;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int apiId;
    private String name;

    public Category(int id, int apiId, String name) {
        this.id = id;
        this.apiId = apiId;
        this.name = name;
    }

    @Ignore
    public Category(JSONObject object) {
        this.id = 0;
        this.apiId = object.optInt(APIStatic.Key.id);
        this.name = object.optString(APIStatic.Key.name);
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
}
