package com.tapatuniforms.pos.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.DayClosingStockAdapter;

public class DayClosingStockFragment extends Fragment {
    private RecyclerView stockRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_day_closing, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        stockRecycler = view.findViewById(R.id.stockListRecycler);
        stockRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        stockRecycler.setAdapter(new DayClosingStockAdapter());
    }
}
