package com.tapatuniforms.pos.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.DayClosingStockAdapter;

import java.util.Objects;

public class DayClosingStockFragment extends Fragment {
    private RecyclerView stockRecycler;
    private Button nextButton;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_day_closing, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        viewPager = Objects.requireNonNull(getActivity()).findViewById(R.id.view_pager);
        nextButton = view.findViewById(R.id.nextButton);
        stockRecycler = view.findViewById(R.id.stockListRecycler);

        stockRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        stockRecycler.setAdapter(new DayClosingStockAdapter());

        nextButton.setOnClickListener(v -> {
            if (viewPager != null) {
                viewPager.setCurrentItem(2);
            }
        });
    }
}
