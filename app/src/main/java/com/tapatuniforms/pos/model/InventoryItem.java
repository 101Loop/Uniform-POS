package com.tapatuniforms.pos.model;

public class InventoryItem {
    private long id;
    private String name;
    private String serialNumber;
    private int warehouseQuantity;
    private int displayQuantity;

    public InventoryItem(long id, String name, String serialNumber, int warehouseQuantity, int displayQuantity) {
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
        this.warehouseQuantity = warehouseQuantity;
        this.displayQuantity = displayQuantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getWarehouseQuantity() {
        return warehouseQuantity;
    }

    public void setWarehouseQuantity(int warehouseQuantity) {
        this.warehouseQuantity = warehouseQuantity;
    }

    public int getDisplayQuantity() {
        return displayQuantity;
    }

    public void setDisplayQuantity(int displayQuantity) {
        this.displayQuantity = displayQuantity;
    }
}
