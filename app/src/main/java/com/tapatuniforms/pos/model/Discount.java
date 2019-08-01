package com.tapatuniforms.pos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONObject;

@Entity
public class Discount {
    @PrimaryKey
    private int id;
    private int productQuantity;
    private String discountType;
    private int value;

    public Discount(int id, int productQuantity, String discountType, int value) {
        this.id = id;
        this.productQuantity = productQuantity;
        this.discountType = discountType;
        this.value = value;
    }

    public Discount(JSONObject jsonObject) {
        this.id = jsonObject.optInt(APIStatic.Key.id);
        this.productQuantity = jsonObject.optInt(APIStatic.Key.productQuantity);
        this.discountType = jsonObject.optString(APIStatic.Key.discountType);
        this.value = jsonObject.optInt(APIStatic.Key.value);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
