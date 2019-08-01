package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONObject;

@Entity
public class Box {
    @PrimaryKey
    private long id;
    private String name;
    private String serialNumber;
    private String dateTime;
    private int numberOfItems;
    private int itemsVerified;
    private String indentId;
    private String numberOfMaleItems;
    private String numberOfFemaleItems;
    private String indentName;

    public Box(long id, String name, String serialNumber, String dateTime, int numberOfItems,
               int itemsVerified, String indentId, String numberOfMaleItems, String numberOfFemaleItems, String indentName) {
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
        this.dateTime = dateTime;
        this.numberOfItems = numberOfItems;
        this.itemsVerified = itemsVerified;
        this.indentId = indentId;
        this.numberOfMaleItems = numberOfMaleItems;
        this.numberOfFemaleItems = numberOfFemaleItems;
        this.indentName = indentName;
    }

    public Box(JSONObject jsonObject) {
        this.id = jsonObject.optInt(APIStatic.Key.id);
        this.name = jsonObject.optString(APIStatic.Key.name);
        this.serialNumber = jsonObject.optString(APIStatic.Key.boxCode);    //might need to change this
        this.dateTime = "";
        this.numberOfItems = jsonObject.optInt(APIStatic.Key.totalItems);
        this.itemsVerified = 0;
        this.indentId = jsonObject.optString(APIStatic.Key.indent);
        this.numberOfMaleItems = jsonObject.optString(APIStatic.Key.numberOfMaleItems);
        this.numberOfFemaleItems = jsonObject.optString(APIStatic.Key.numberOfFemaleItems);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public int getItemsVerified() {
        return itemsVerified;
    }

    public void setItemsVerified(int itemsVerified) {
        this.itemsVerified = itemsVerified;
    }

    public String getIndentId() {
        return indentId;
    }

    public void setIndentId(String indentId) {
        this.indentId = indentId;
    }

    public String getNumberOfMaleItems() {
        return numberOfMaleItems;
    }

    public void setNumberOfMaleItems(String numberOfMaleItems) {
        this.numberOfMaleItems = numberOfMaleItems;
    }

    public String getNumberOfFemaleItems() {
        return numberOfFemaleItems;
    }

    public void setNumberOfFemaleItems(String numberOfFemaleItems) {
        this.numberOfFemaleItems = numberOfFemaleItems;
    }

    public String getIndentName() {
        return indentName;
    }

    public void setIndentName(String indentName) {
        this.indentName = indentName;
    }
}
