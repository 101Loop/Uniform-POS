package com.tapatuniforms.pos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tapatuniforms.pos.R;

import java.util.ArrayList;

public class SaleReportFragment extends Fragment implements View.OnClickListener {
    Spinner billerSpinner, categorySpinner;
    TextView startDateView, endDateView;
    ImageView startDateImage, endDateImage;

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
        startDateView = v.findViewById(R.id.startDateView);
        startDateImage = v.findViewById(R.id.imageView);
        endDateView = v.findViewById(R.id.endDateView);
        endDateImage = v.findViewById(R.id.imageView2);

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

        startDateView.setOnClickListener(this);
        startDateImage.setOnClickListener(this);
        endDateView.setOnClickListener(this);
        endDateImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startDateView:
                onStartDateViewClicked();
                break;
            case R.id.imageView:
                onStartDateViewClicked();
                break;
            case R.id.endDateView:
                onEndDateViewClicked();
                break;
            case R.id.imageView2:
                onEndDateViewClicked();
                break;
        }
    }

    private void onStartDateViewClicked() {}

    private void onEndDateViewClicked() {}
}
