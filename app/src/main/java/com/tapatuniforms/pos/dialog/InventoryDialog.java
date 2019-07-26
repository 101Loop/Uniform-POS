package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.InventoryPopupListAdapter;

public class InventoryDialog extends AlertDialog {
    private RecyclerView itemRecyclerView;
    private Button closeButton;
    private Button transferButton;

    public InventoryDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_dialog);

        transferButton = findViewById(R.id.transferButton);
        closeButton = findViewById(R.id.closeButton);
        itemRecyclerView = findViewById(R.id.itemListRecyclerView);

        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemRecyclerView.setAdapter(new InventoryPopupListAdapter());

        transferButton.setOnClickListener(view -> {});

        closeButton.setOnClickListener(view -> dismiss());
    }
}
