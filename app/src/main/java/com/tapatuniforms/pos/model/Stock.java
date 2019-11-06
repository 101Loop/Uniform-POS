package com.tapatuniforms.pos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONException;
import org.json.JSONObject;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("variantId")}, foreignKeys = @ForeignKey(entity = ProductVariant.class,
        parentColumns = "id",
        childColumns = "variantId",
        onDelete = CASCADE))
public class Stock {
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "variantId")
    private int variantId;
    private int display;
    private int warehouse;

    public Stock(int id, int variantId, int display, int warehouse) {
        this.id = id;
        this.variantId = variantId;
        this.display = display;
        this.warehouse = warehouse;
    }

    @Ignore
    public Stock(JSONObject jsonObject) {
        this.variantId = jsonObject.optInt(APIStatic.Key.id);
        this.id = this.variantId;
        this.display = jsonObject.optInt(APIStatic.Key.displayStock);
        this.warehouse = jsonObject.optInt(APIStatic.Key.warehouseStock);
    }

    @Ignore
    public Stock(int variantId, int display, int warehouse) {
        this.variantId = variantId;
        this.id = variantId;
        this.display = display;
        this.warehouse = warehouse;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(APIStatic.Key.displayStock, this.getDisplay());
            jsonObject.put(APIStatic.Key.warehouseStock, this.getWarehouse());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public int getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(int warehouse) {
        this.warehouse = warehouse;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }
}
