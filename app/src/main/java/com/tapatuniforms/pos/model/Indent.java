package com.tapatuniforms.pos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONObject;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = @Index(value = "schoolId"), foreignKeys = @ForeignKey(entity = School.class,
        parentColumns = "id",
        childColumns = "schoolId",
        onDelete = CASCADE))

public class Indent {
    @PrimaryKey
    private long id;
    @ColumnInfo(name = "schoolId")
    private int schoolId;
    private String name;
    private String price;
    private String numberOfBoxes;
    private String numberOfItems;
    private int warehouseName;
    private boolean isSelected;

    public Indent(long id, int schoolId, String name, String price, String numberOfBoxes, String numberOfItems, int warehouseName, boolean isSelected) {
        this.id = id;
        this.schoolId = schoolId;
        this.name = name;
        this.price = price;
        this.numberOfBoxes = numberOfBoxes;
        this.numberOfItems = numberOfItems;
        this.warehouseName = warehouseName;
        this.isSelected = isSelected;
    }

    public Indent(JSONObject jsonObject) {
        JSONObject schoolJSON = jsonObject.optJSONObject(APIStatic.Key.school);

        this.id = jsonObject.optInt(APIStatic.Key.id);

        if (schoolJSON != null)
            this.schoolId = schoolJSON.optInt(APIStatic.Key.id);

        this.name = jsonObject.optString(APIStatic.Key.indent);
        this.price = jsonObject.optString(APIStatic.Key.price);
        this.numberOfBoxes = jsonObject.optString(APIStatic.Key.numberOfBoxes);
        this.numberOfItems = jsonObject.optString(APIStatic.Key.numberOfItems);
        this.warehouseName = jsonObject.optInt(APIStatic.Key.warehouseName);
        this.isSelected = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberOfBoxes() {
        return numberOfBoxes;
    }

    public void setNumberOfBoxes(String numberOfBoxes) {
        this.numberOfBoxes = numberOfBoxes;
    }

    public String getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(String numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(int warehouseName) {
        this.warehouseName = warehouseName;
    }
}
