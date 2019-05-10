package com.tapatuniforms.pos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tapatuniforms.pos.R;

import java.util.ArrayList;

public class SaleReportFragment extends Fragment {
    Spinner billerSpinner, categorySpinner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sale_report, container, false);

        init(v);

        return v;

    }

    private void init(View v) {
        billerSpinner = v.findViewById(R.id.billerSpinner);
        categorySpinner = v.findViewById(R.id.categorySpinner);

        ArrayList<String> list = new ArrayList<>();
        list.add("Nick Fury");
        list.add("Phil Coulson");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billerSpinner.setAdapter(spinnerArrayAdapter);

        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("All");
        categoryList.add("Men's Wear");
        categoryList.add("Women's Wear");
        categoryList.add("Kids Wear");
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(catAdapter);
    }
}
