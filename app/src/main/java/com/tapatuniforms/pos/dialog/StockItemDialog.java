package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.StockBoxItemAdapter;
import com.tapatuniforms.pos.model.Box;

public class StockItemDialog extends AlertDialog {
    private RecyclerView itemRecyclerView;
    private Button closeButton;
    private EditText barcodeText;
    private Button addButton;
    private Box box;
    private TextView boxNameView;
    private TextView indentNameView;

    public StockItemDialog(Context context, Box box) {
        super(context);
        this.box = box;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_item_dialog_layout);

        barcodeText = findViewById(R.id.barcodeEditText);
        addButton = findViewById(R.id.addButton);
        closeButton = findViewById(R.id.closeButton);
        boxNameView = findViewById(R.id.boxNameView);
        indentNameView = findViewById(R.id.indentNameView);
        itemRecyclerView = findViewById(R.id.itemRecyclerView);

        boxNameView.setText(box.getName());
        indentNameView.setText(box.getIndentName());
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
