package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.StockBoxItemAdapter;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.model.Box;
import com.tapatuniforms.pos.model.BoxItem;
import com.tapatuniforms.pos.network.StockOrder;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StockItemDialog extends AlertDialog {
    private RecyclerView itemRecyclerView;
    private Button closeButton;
    private EditText barcodeText;
    private Button addButton;
    private Box box;
    private TextView boxNameView;
    private TextView indentNameView;
    private ArrayList<BoxItem> boxItemList;
    private StockBoxItemAdapter adapter;
    private LinearLayout boxItemLayout;
    private TextView noBoxItemText;

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
        boxItemLayout = findViewById(R.id.boxItemLayout);
        noBoxItemText = findViewById(R.id.noBoxItemText);

        boxNameView.setText(box.getName());
        indentNameView.setText(box.getIndentName());
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        boxItemList = new ArrayList<>();
        adapter = new StockBoxItemAdapter(getContext(), boxItemList);
        getBoxItemList();
        itemRecyclerView.setAdapter(adapter);

        addButton.setOnClickListener(view -> {
            String barcode = barcodeText.getText().toString();
            //TODO: add some logic
        });

        closeButton.setOnClickListener(view -> dismiss());
    }

    private void getBoxItemList() {
        StockOrder.getInstance(getContext()).getBoxItem(boxItemList, adapter, box.getId(), this);
    }

    public void checkAvailability() {
        if (boxItemList == null || boxItemList.size() == 0) {
            boxItemLayout.setVisibility(View.GONE);
            noBoxItemText.setVisibility(View.VISIBLE);
        } else {
            boxItemLayout.setVisibility(View.VISIBLE);
            noBoxItemText.setVisibility(View.GONE);
        }
    }
}
