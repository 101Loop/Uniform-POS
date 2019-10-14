package com.tapatuniforms.pos.model;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieEntry;

public class PieChartItem {
    private int value;
    private String label;
    private String color;

    public PieChartItem(int value, String label, String color) {
        this.value = value;
        this.label = label;
        this.color = color;
    }

    public PieEntry getPieEntry(){
        return new PieEntry(this.value, this.label);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
