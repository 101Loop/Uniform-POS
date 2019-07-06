package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.StockBoxItemAdapter;

public class StockItemDialog extends AlertDialog {
    public StockItemDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_item_dialog_layout);

        RecyclerView itemRecyclerView = findViewById(R.id.itemRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        StockBoxItemAdapter adapter = new StockBoxItemAdapter();
        itemRecyclerView.setAdapter(adapter);
    }
}
