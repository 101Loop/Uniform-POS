package com.tapatuniforms.pos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONArray;
import org.json.JSONObject;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("productId")}, foreignKeys = @ForeignKey(entity = ProductHeader.class,
        parentColumns = "id",
        childColumns = "productId",
        onDelete = CASCADE))

public class ProductVariant {
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "productId")
    private int productId;
    private String size;
    private double price;
    private int warehouseStock;
    private int displayStock;
    private int transferOrderCount;
    private boolean isSynced;

    public ProductVariant(int id, int productId, String size, double price, int warehouseStock, int displayStock, int transferOrderCount, boolean isSynced) {
        this.id = id;
        this.productId = productId;
        this.size = size;
        this.price = price;
        this.warehouseStock = warehouseStock;
        this.displayStock = displayStock;
        this.transferOrderCount = transferOrderCount;
        this.isSynced = isSynced;
    }

    public ProductVariant(JSONObject jsonObject, int position) {
        JSONArray subProductSet = jsonObject.optJSONArray(APIStatic.Key.outletSubproductSet);
        JSONObject currentSubProduct = subProductSet.optJSONObject(position);
        JSONObject productJson = jsonObject.optJSONObject(APIStatic.Key.product);

        this.id = currentSubProduct.optInt(APIStatic.Key.id);
        this.productId = productJson.optInt(APIStatic.Key.id);
        this.size = currentSubProduct.optString(APIStatic.Key.size);
        this.price = currentSubProduct.optDouble(APIStatic.Key.price);
        this.warehouseStock = currentSubProduct.optInt(APIStatic.Key.warehouseStock);
        this.displayStock = currentSubProduct.optInt(APIStatic.Key.displayStock);
        this.transferOrderCount = 0;
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

    public int getTransferOrderCount() {
        return transferOrderCount;
    }

    public void setTransferOrderCount(int transferOrderCount) {
        this.transferOrderCount = transferOrderCount;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
