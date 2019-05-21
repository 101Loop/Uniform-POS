package com.tapatuniforms.pos.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.StockProductAdapter;
import com.tapatuniforms.pos.adapter.StockRequestAdapter;
import com.tapatuniforms.pos.dialog.StockItemDialog;
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.Product;

import java.util.ArrayList;

public class StockEntryFragment extends Fragment implements StockRequestAdapter.OnBoxClickListener {
    private RecyclerView itemRecyclerView, requestRecyclerView;
    private Button requestButton;
    private ArrayList<CartItem> requestList;
    private ArrayList<Product> productList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stock_entry, container, false);

        init(v);

        return v;
    }


    private void init(View v) {
        itemRecyclerView = v.findViewById(R.id.itemRecyclerView);
        requestRecyclerView = v.findViewById(R.id.itemRequestRecyclerView);
//        requestButton = v.findViewById(R.id.raiseRequestButton);

        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemRecyclerView.setAdapter(new StockProductAdapter());

        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        StockRequestAdapter stockAdapter = new StockRequestAdapter();
        requestRecyclerView.setAdapter(stockAdapter);

        stockAdapter.setOnBoxClickListener(this);
    }

    @Override
    public void onBoxSelected() {
        StockItemDialog dialog = new StockItemDialog(getContext());
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
