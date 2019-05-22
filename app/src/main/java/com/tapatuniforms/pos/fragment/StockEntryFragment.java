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
import com.tapatuniforms.pos.adapter.StockIndentAdapter;
import com.tapatuniforms.pos.adapter.StockBoxAdapter;
import com.tapatuniforms.pos.dialog.StockItemDialog;
import com.tapatuniforms.pos.model.Box;
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.Indent;
import com.tapatuniforms.pos.model.Product;

import java.util.ArrayList;
import java.util.Objects;

public class StockEntryFragment extends Fragment implements StockBoxAdapter.OnBoxClickListener {
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
        itemRecyclerView.setAdapter(new StockIndentAdapter(getIndentList()));

        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        StockBoxAdapter stockAdapter = new StockBoxAdapter(getBoxList());
        requestRecyclerView.setAdapter(stockAdapter);

        stockAdapter.setOnBoxClickListener(this);
    }

    @Override
    public void onBoxSelected() {
        StockItemDialog dialog = new StockItemDialog(getContext());
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private ArrayList<Indent> getIndentList() {
        ArrayList<Indent> list = new ArrayList<>();

        list.add(new Indent(1, "Indent 1", "20/May/2019 06:44 PM", "Vivek Kumar",
                5, 2000, 260));
        list.add(new Indent(1, "Indent 2", "20/May/2019 06:44 PM", "Vivek Kumar",
                9, 5200, 350));
        list.add(new Indent(1, "Indent 3", "20/May/2019 06:44 PM", "Vivek Kumar",
                8, 4700, 300));
        list.add(new Indent(1, "Indent 4", "20/May/2019 06:44 PM", "Vivek Kumar",
                5, 2400, 400));

        return list;
    }

    private ArrayList<Box> getBoxList() {
        ArrayList<Box> list = new ArrayList<>();

        list.add(new Box(1, "Box 1", "1234456677", "10/May/2019 6:55 PM",
                300, 213));
        list.add(new Box(1, "Box 2", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
//        list.add(new Box(1, "Box 3", "1234456677", "10/May/2019 6:55 PM",
//                300, 213));
//        list.add(new Box(1, "Box 4", "1234456677", "10/May/2019 6:55 PM",
//                300, 223));
//        list.add(new Box(1, "Box 5", "1234456677", "10/May/2019 6:55 PM",
//                300, 200));

        return list;

    }
}
