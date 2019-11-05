package com.tapatuniforms.pos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.DatabaseSingleton;

import org.json.JSONObject;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = @Index(value = "outletId"), foreignKeys = @ForeignKey(entity = Outlet.class,
        parentColumns = "id",
        childColumns = "outletId",
        onDelete = CASCADE))

public class Indent {
    @PrimaryKey
    private long id;
    @ColumnInfo(name = "outletId")
    private int outletId;
    private String indent;
    private String price;
    private String numberOfBoxes;
    private String numberOfItems;
    private int warehouseName;
    private boolean isSelected;

    public Indent(long id, int outletId, String indent, String price, String numberOfBoxes, String numberOfItems, int warehouseName, boolean isSelected) {
        this.id = id;
        this.outletId = outletId;
        this.indent = indent;
        this.price = price;
        this.numberOfBoxes = numberOfBoxes;
        this.numberOfItems = numberOfItems;
        this.warehouseName = warehouseName;
        this.isSelected = isSelected;
    }

    public Indent(JSONObject jsonObject) {
        this.id = jsonObject.optInt(APIStatic.Key.id);
        this.indent = jsonObject.optString(APIStatic.Key.indent);
        this.price = jsonObject.optString(APIStatic.Key.price);
        this.numberOfBoxes = jsonObject.optString(APIStatic.Key.numberOfBoxes);
        this.numberOfItems = jsonObject.optString(APIStatic.Key.numberOfItems);
        this.warehouseName = jsonObject.optInt(APIStatic.Key.warehouseName);
        this.isSelected = false;

        JSONObject outletJson = jsonObject.optJSONObject(APIStatic.Key.outlet);
        if (outletJson != null)
            this.outletId = outletJson.optInt(APIStatic.Key.id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
    }

    public String getIndent() {
        return indent;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public int getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(int warehouseName) {
        this.warehouseName = warehouseName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
