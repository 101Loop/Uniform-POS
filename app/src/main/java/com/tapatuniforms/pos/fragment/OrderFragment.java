package com.tapatuniforms.pos.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.OrderAdapter;
import com.tapatuniforms.pos.adapter.SubOrderAdapter;

public class OrderFragment extends Fragment {
    private static final String TAG = "OrderFragment";

    private RecyclerView orderRecycler;
    private OrderAdapter orderAdapter;

    private RecyclerView subOrderRecycler;
    private SubOrderAdapter subOrderAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        orderRecycler = view.findViewById(R.id.orderRecyclerView);
        subOrderRecycler = view.findViewById(R.id.subOrderRecycler);

        orderAdapter = new OrderAdapter();
        subOrderAdapter = new SubOrderAdapter();
        setData();
    }

    private void setData() {
        orderRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecycler.setAdapter(orderAdapter);

        subOrderRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        subOrderRecycler.setAdapter(subOrderAdapter);
    }

}
