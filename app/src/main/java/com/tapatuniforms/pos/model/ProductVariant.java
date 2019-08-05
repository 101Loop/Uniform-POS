package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONArray;
import org.json.JSONObject;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = ProductHeader.class,
        parentColumns = "id",
        childColumns = "productId",
        onDelete = CASCADE))

public class ProductVariant {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int productId;
    private String size;
    private double price;
    private int warehouseStock;
    private int displayStock;

    public ProductVariant(int id, int productId, String size, double price, int warehouseStock, int displayStock) {
        this.id = id;
        this.productId = productId;
        this.size = size;
        this.price = price;
        this.warehouseStock = warehouseStock;
        this.displayStock = displayStock;
    }

    public ProductVariant(JSONObject jsonObject, int position) {
        JSONArray subProductSet = jsonObject.optJSONArray(APIStatic.Key.outletSubproductSet);
        JSONObject currentSubProduct = subProductSet.optJSONObject(position);

        this.productId = jsonObject.optInt(APIStatic.Key.id);
        this.size = currentSubProduct.optString(APIStatic.Key.size);
        this.price = currentSubProduct.optDouble(APIStatic.Key.price);
        this.warehouseStock = currentSubProduct.optInt(APIStatic.Key.warehouseStock);
        this.displayStock = currentSubProduct.optInt(APIStatic.Key.displayStock);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getWarehouseStock() {
        return warehouseStock;
    }

    public void setWarehouseStock(int warehouseStock) {
        this.warehouseStock = warehouseStock;
    }

    public int getDisplayStock() {
        return displayStock;
    }

    public void setDisplayStock(int displayStock) {
        this.displayStock = displayStock;
    }
}
