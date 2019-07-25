package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.StockBoxItemAdapter;

public class StockItemDialog extends AlertDialog {
    private RecyclerView itemRecyclerView;
    private Button closeButton;
    private EditText barcodeText;
    private Button addButton;

    public StockItemDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_item_dialog_layout);

        barcodeText = findViewById(R.id.barcodeEditText);
        addButton = findViewById(R.id.addButton);
        closeButton = findViewById(R.id.closeButton);

        itemRecyclerView = findViewById(R.id.itemRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        StockBoxItemAdapter adapter = new StockBoxItemAdapter(getContext());
        itemRecyclerView.setAdapter(adapter);

        addButton.setOnClickListener(view -> {
            String barcode = barcodeText.getText().toString();
            //TODO: add some logic
        });

        closeButton.setOnClickListener(view -> dismiss());
    }
}
