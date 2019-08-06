package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONArray;
import org.json.JSONObject;

@Entity
public class ProductHeader {
    @PrimaryKey
    private int id;
    private String name;
    private String gender;
    private String productType;
    private String color;
    private String colorCode;
    private String image;
    private int outletId;
    private int category;
    private int variantSize;
    private boolean isSynced;
    private String sku;
    private boolean isSizeAlreadyOpened;
    private int totalWarehouseStock;

    public ProductHeader(int id, String name, String gender, String productType, String color,
                         String colorCode, String image, int outletId, int category, int variantSize,
                         boolean isSynced, String sku, boolean isSizeAlreadyOpened, int totalWarehouseStock) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.productType = productType;
        this.color = color;
        this.colorCode = colorCode;
        this.image = image;
        this.outletId = outletId;
        this.category = category;
        this.variantSize = variantSize;
        this.isSynced = isSynced;
        this.sku = sku;
        this.isSizeAlreadyOpened = isSizeAlreadyOpened;
        this.totalWarehouseStock = totalWarehouseStock;
    }

    public ProductHeader(JSONObject jsonObject) {
        JSONObject productJson = jsonObject.optJSONObject(APIStatic.Key.product);
        JSONArray subProductSet = jsonObject.optJSONArray(APIStatic.Key.outletSubproductSet);

        this.id = jsonObject.optInt(APIStatic.Key.id);
        this.name = productJson.optString(APIStatic.Key.name);
        this.gender = productJson.optString(APIStatic.Key.gender_type);
        this.productType = productJson.optString(APIStatic.Key.productType);
        this.color = jsonObject.optString(APIStatic.Key.color);
        this.colorCode = jsonObject.optString(APIStatic.Key.colorCode);
        this.image = jsonObject.optString(APIStatic.Key.image);
        this.outletId = jsonObject.optInt(APIStatic.Key.outlet);
        this.category = productJson.optInt(APIStatic.Key.category);
        this.variantSize = subProductSet.length();
        this.isSynced = false;
        this.sku = productJson.optString(APIStatic.Key.sku);
        this.isSizeAlreadyOpened = false;
        this.totalWarehouseStock = 0;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getVariantSize() {
        return variantSize;
    }

    public void setVariantSize(int variantSize) {
        this.variantSize = variantSize;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public boolean isSizeAlreadyOpened() {
        return isSizeAlreadyOpened;
    }

    public void setSizeAlreadyOpened(boolean sizeAlreadyOpened) {
        isSizeAlreadyOpened = sizeAlreadyOpened;
    }

    public int getTotalWarehouseStock() {
        return totalWarehouseStock;
    }

    public void setTotalWarehouseStock(int totalWarehouseStock) {
        this.totalWarehouseStock = totalWarehouseStock;
    }
}
