package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int apiId;
    private String name;
    private String sku;
    private String image;
    private double price;
    private String size;
    private int category;
    private int outlet;
    private String color;
    private String gender;
    private String colorCode;
    private String productType;

    public Product(int id, int apiId, String name, String sku, String image, double price,
                   String size, int category, int outlet, String color) {
        this.id = id;
        this.apiId = apiId;
        this.name = name;
        this.sku = sku;
        this.image = image;
        this.price = price;
        this.size = size;
        this.category = category;
        this.outlet = outlet;
        this.color = color;
    }

    @Ignore
    public Product(JSONObject object) throws JSONException {
        JSONObject productObject = object.getJSONObject(APIStatic.Key.product);

        this.id = 0;
        this.apiId = object.optInt(APIStatic.Key.id);
        this.name = productObject.optString(APIStatic.Key.name);
        this.sku = productObject.optString(APIStatic.Key.sku);
        this.image = object.optString(APIStatic.Key.image);
        this.price = object.optDouble(APIStatic.Key.price);
        this.size = object.optString(APIStatic.Key.size);
        this.category = productObject.optInt(APIStatic.Key.category);
        this.outlet = object.optInt(APIStatic.Key.outlet);
        this.color = object.optString(APIStatic.Key.color);
        this.gender = productObject.optString(APIStatic.Key.gender_type);
        this.colorCode = object.optString(APIStatic.Key.colorCode);
        this.productType = productObject.optString(APIStatic.Key.productType);
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getOutlet() {
        return outlet;
    }

    public void setOutlet(int outlet) {
        this.outlet = outlet;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
