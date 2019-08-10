package com.tapatuniforms.pos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.tapatuniforms.pos.helper.APIStatic;

import org.json.JSONObject;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("productId")}, foreignKeys = @ForeignKey(entity = ProductHeader.class,
        parentColumns = "id",
        childColumns = "productId",
        onDelete = CASCADE))

public class BoxItem {
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "productId")
    private int productId;
    private int numberOfItems;
    private int numberOfScannedItems;
    private int boxId;

    public BoxItem(int id, int productId, int numberOfItems, int numberOfScannedItems, int boxId) {
        this.id = id;
        this.productId = productId;
        this.numberOfItems = numberOfItems;
        this.numberOfScannedItems = numberOfScannedItems;
        this.boxId = boxId;
    }

    public BoxItem(JSONObject jsonObject) {
        JSONObject productJSON = jsonObject.optJSONObject(APIStatic.Key.product).optJSONObject(APIStatic.Key.outletProduct).optJSONObject(APIStatic.Key.product);

        this.id = jsonObject.optInt(APIStatic.Key.id);
        this.productId = productJSON.optInt(APIStatic.Key.id);
        this.numberOfItems = jsonObject.optInt(APIStatic.Key.numberOfItem);
        this.numberOfScannedItems = jsonObject.optInt(APIStatic.Key.numberOfScannedItems);
        this.boxId = jsonObject.optInt(APIStatic.Key.box);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public int getNumberOfScannedItems() {
        return numberOfScannedItems;
    }

    public void setNumberOfScannedItems(int numberOfScannedItems) {
        this.numberOfScannedItems = numberOfScannedItems;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
