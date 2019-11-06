package com.tapatuniforms.pos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("variantId")}, foreignKeys = @ForeignKey(entity = ProductVariant.class,
        parentColumns = "id",
        childColumns = "variantId",
        onDelete = CASCADE))
public class Stock {
    @PrimaryKey(autoGenerate = true)
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
    public Stock(int variantId, int display, int warehouse) {
        this.variantId = variantId;
        this.display = display;
        this.warehouse = warehouse;
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
