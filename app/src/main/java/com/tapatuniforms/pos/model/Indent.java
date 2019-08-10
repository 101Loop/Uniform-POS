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
    private String shippingFrom;
    private String shippingFromLat;
    private String shippingFromLong;
    private String shippedOn;
    private String receivedOn;
    private boolean isSelected;

    public Indent(long id, int schoolId, String name, String price, String numberOfBoxes, String numberOfItems,
                  String shippingFrom, String shippingFromLat, String shippingFromLong, String shippedOn, String receivedOn, boolean isSelected) {
        this.id = id;
        this.schoolId = schoolId;
        this.name = name;
        this.price = price;
        this.numberOfBoxes = numberOfBoxes;
        this.numberOfItems = numberOfItems;
        this.shippingFrom = shippingFrom;
        this.shippingFromLat = shippingFromLat;
        this.shippingFromLong = shippingFromLong;
        this.shippedOn = shippedOn;
        this.receivedOn = receivedOn;
        this.isSelected = isSelected;
    }

    public Indent(JSONObject jsonObject) {
        JSONObject schoolJSON = jsonObject.optJSONObject(APIStatic.Key.school);

        this.id = jsonObject.optInt(APIStatic.Key.id);
        this.schoolId = schoolJSON.optInt(APIStatic.Key.id);
        this.name = jsonObject.optString(APIStatic.Key.name);
        this.price = jsonObject.optString(APIStatic.Key.price);
        this.numberOfBoxes = jsonObject.optString(APIStatic.Key.numberOfBoxes);
        this.numberOfItems = jsonObject.optString(APIStatic.Key.numberOfItems);
        this.isSelected = false;
        this.shippingFrom = jsonObject.optString(APIStatic.Key.shippingFrom);
        this.shippingFromLat = jsonObject.optString(APIStatic.Key.shippingFromLat);
        this.shippingFromLong = jsonObject.optString(APIStatic.Key.shippingFromLong);
        this.shippedOn = jsonObject.optString(APIStatic.Key.shippedOn);
        this.receivedOn = jsonObject.optString(APIStatic.Key.receivedOn);
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

    public String getShippingFrom() {
        return shippingFrom;
    }

    public void setShippingFrom(String shippingFrom) {
        this.shippingFrom = shippingFrom;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShippingFromLat() {
        return shippingFromLat;
    }

    public void setShippingFromLat(String shippingFromLat) {
        this.shippingFromLat = shippingFromLat;
    }

    public String getShippingFromLong() {
        return shippingFromLong;
    }

    public void setShippingFromLong(String shippingFromLong) {
        this.shippingFromLong = shippingFromLong;
    }

    public String getShippedOn() {
        return shippedOn;
    }

    public void setShippedOn(String shippedOn) {
        this.shippedOn = shippedOn;
    }

    public String getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(String receivedOn) {
        this.receivedOn = receivedOn;
    }
}
