package com.tapatuniforms.pos.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.CategoryAdapter;
import com.tapatuniforms.pos.adapter.InventoryAdapter;
import com.tapatuniforms.pos.adapter.InventoryOrderAdapter;
import com.tapatuniforms.pos.dialog.InventoryDialog;
import com.tapatuniforms.pos.helper.DataHelper;
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.model.Category;
import com.tapatuniforms.pos.model.InventoryItem;

import java.util.ArrayList;

public class InventoryFragment extends Fragment implements InventoryAdapter.ButtonClickListener {
    private RecyclerView inventoryRecyclerView, recommendedRecyclerView;
    private InventoryAdapter adapter;
    private ArrayList<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private RecyclerView categoryRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        categoryList = new ArrayList<>();
        categoryRecyclerView = view.findViewById(R.id.categoryRecycler);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        categoryRecyclerView.addItemDecoration(new GridItemDecoration(16, 16));
        categoryRecyclerView.setAdapter(categoryAdapter);

        inventoryRecyclerView = view.findViewById(R.id.inventoryRecyclerView);
        inventoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        inventoryRecyclerView.addItemDecoration(new GridItemDecoration(12, 12));
        adapter = new InventoryAdapter(getContext(), getItemList());
        inventoryRecyclerView.setAdapter(adapter);

        recommendedRecyclerView = view.findViewById(R.id.recommendedRecyclerView);
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recommendedRecyclerView.setAdapter(new InventoryOrderAdapter());

        adapter.setOnClickListener(this);

        DataHelper.fetchCategories(getContext(), categoryList, categoryAdapter);
        setData();
    }

    private void setData() {
        adapter.setOnClickListener(inventoryItem -> {
            InventoryDialog dialog = new InventoryDialog(getContext());

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            dialog.show();
        });
    }

    private ArrayList<InventoryItem> getItemList() {
        ArrayList<InventoryItem> list = new ArrayList<>();

        list.add(new InventoryItem(1, "Shirt Type 1", "12123342", 50, 2));
        list.add(new InventoryItem(2, "Shirt Type 2", "12123342", 60, 20));
        list.add(new InventoryItem(3, "Shirt Type 3", "12123342", 80, 7));
        list.add(new InventoryItem(4, "Shirt Type 4", "12123342", 70, 10));
        list.add(new InventoryItem(5, "Shirt Type 5", "12123342", 50, 5));
        list.add(new InventoryItem(6, "Shirt Type 6", "12123342", 40, 10));
        list.add(new InventoryItem(7, "Shirt Type 7", "12123342", 10, 0));
        list.add(new InventoryItem(8, "Shirt Type 8", "12123342", 20, 8));
        list.add(new InventoryItem(9, "Shirt Type 9", "12123342", 50, 4));
        list.add(new InventoryItem(10, "Shirt Type 10", "12123342", 70, 10));

        return list;
    }

    @Override
    public void onTransferButtonClick(InventoryItem item) {
        InventoryDialog dialog = new InventoryDialog(getContext());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();
    }
}
