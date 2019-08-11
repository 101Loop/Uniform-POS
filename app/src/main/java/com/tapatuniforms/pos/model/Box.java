package com.tapatuniforms.pos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONObject;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("indentId")}, foreignKeys = @ForeignKey(entity = Indent.class,
        parentColumns = "id",
        childColumns = "indentId",
        onDelete = CASCADE))

public class Box {
    @PrimaryKey
    private long id;
    @ColumnInfo(name = "indentId")
    private int indentId;
    private String name;
    private String serialNumber;
    private String dateTime;
    private int numberOfItems;
    private int itemsVerified;
    private String numberOfMaleItems;
    private String numberOfFemaleItems;

    public Box(long id, int indentId, String name, String serialNumber, String dateTime, int numberOfItems,
               int itemsVerified, String numberOfMaleItems, String numberOfFemaleItems) {
        this.id = id;
        this.indentId = indentId;
        this.name = name;
        this.serialNumber = serialNumber;
        this.dateTime = dateTime;
        this.numberOfItems = numberOfItems;
        this.itemsVerified = itemsVerified;
        this.numberOfMaleItems = numberOfMaleItems;
        this.numberOfFemaleItems = numberOfFemaleItems;
    }

    public Box(JSONObject jsonObject) {
        this.id = jsonObject.optInt(APIStatic.Key.id);
        this.indentId = jsonObject.optInt(APIStatic.Key.indent);
        this.name = jsonObject.optString(APIStatic.Key.name);
        this.serialNumber = jsonObject.optString(APIStatic.Key.boxCode);    //might need to change this
        this.dateTime = "";
        this.numberOfItems = jsonObject.optInt(APIStatic.Key.totalItems);
        this.itemsVerified = 0;
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

    public int getIndentId() {
        return indentId;
    }

    public void setIndentId(int indentId) {
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
}
