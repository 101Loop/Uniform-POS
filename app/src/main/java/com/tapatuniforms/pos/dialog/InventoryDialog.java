package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.InventoryPopupListAdapter;
import com.tapatuniforms.pos.helper.RoundedCornerLayout;
import com.tapatuniforms.pos.model.Product;

public class InventoryDialog extends AlertDialog implements InventoryPopupListAdapter.ItemCountChangeListener {
    private final static String TAG = "InventoryDialog";
    private RecyclerView itemRecyclerView;
    private Button closeButton;
    private Button transferButton;
    private String title;
    private TextView titleText, itemName, itemType, itemColorView, warehouseQuantityView, displayQuantityView, stockItemsView, totalTransferView;
    private RoundedCornerLayout colorImage;
    private Product item;
    private LinearLayout stockWarehouseLayout;

    public InventoryDialog(Context context, Product item, String title) {
        super(context);

        this.item = item;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_dialog);

        titleText = findViewById(R.id.titleText);
        itemName = findViewById(R.id.itemName);
        itemType = findViewById(R.id.itemTypeView);
        colorImage = findViewById(R.id.colorImage);
        itemColorView = findViewById(R.id.itemColorView);
        warehouseQuantityView = findViewById(R.id.warehouseQuantityView);
        displayQuantityView = findViewById(R.id.displayQuantityView);
        stockWarehouseLayout = findViewById(R.id.stockWarehouseLayout);
        stockItemsView = findViewById(R.id.stockItemsView);
        totalTransferView = findViewById(R.id.totalTransferView);
        transferButton = findViewById(R.id.transferButton);
        closeButton = findViewById(R.id.closeButton);
        itemRecyclerView = findViewById(R.id.itemListRecyclerView);

        InventoryPopupListAdapter inventoryPopupListAdapter = new InventoryPopupListAdapter(getContext(), item);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemRecyclerView.setAdapter(inventoryPopupListAdapter);
        inventoryPopupListAdapter.setOnItemCountChangeListener(this);

        titleText.setText(title);
        transferButton.setText(title);

        int totalStock = 0;
        for (String currentStock : item.getWarehouseStockList()) {
            totalStock += Integer.parseInt(currentStock);
        }
        stockItemsView.setText(totalStock + " in Stock");

        if (!title.equalsIgnoreCase("transfer")) {
            stockWarehouseLayout.setVisibility(View.GONE);
        } else {
            stockWarehouseLayout.setVisibility(View.VISIBLE);
        }

        String type = item.getProductType();
        if (type == null || type.isEmpty() || type.equalsIgnoreCase("null")) {
            type = "";
        }
        itemType.setText(type);

        itemName.setText(item.getName());
        colorImage.setBackgroundColor(Color.parseColor(item.getColorCode()));
        itemColorView.setText(item.getColor());

        transferButton.setOnClickListener(view -> Log.d(TAG, "transfer clicked"));

        closeButton.setOnClickListener(view -> dismiss());
    }

    @Override
    public void onItemChangeListener(int count) {
        totalTransferView.setText(String.valueOf(count));
    }
}
