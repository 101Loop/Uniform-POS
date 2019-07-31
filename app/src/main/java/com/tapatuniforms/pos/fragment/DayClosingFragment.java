package com.tapatuniforms.pos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.DayClosingPagerAdapter;

import java.util.Objects;

public class DayClosingFragment extends Fragment implements ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DayClosingActivity";
    private TextView saleText, stockReportText, closeDayText;
    private ViewPager viewPager;
    private LinearLayout saleSummaryLayout;
    private LinearLayout stockReportLayout;
    private LinearLayout closeDayLayout;
    private DayClosingPagerAdapter dayClosingPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_day_closing, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        saleText = view.findViewById(R.id.saleText);
        stockReportText = view.findViewById(R.id.stockReport);
        closeDayText = view.findViewById(R.id.closeDay);
        saleSummaryLayout = view.findViewById(R.id.saleSummaryLayout);
        stockReportLayout = view.findViewById(R.id.stockReportLayout);
        closeDayLayout = view.findViewById(R.id.closeDayLayout);

        setViews(view);
    }

    private void setViews(View view) {
        dayClosingPagerAdapter = new DayClosingPagerAdapter(getActivity(), getChildFragmentManager());
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(dayClosingPagerAdapter);

        viewPager.addOnPageChangeListener(this);

        saleSummaryLayout.setOnClickListener(view1 -> viewPager.setCurrentItem(0));

        stockReportLayout.setOnClickListener(view1 -> viewPager.setCurrentItem(1));

        closeDayLayout.setOnClickListener(view1 -> viewPager.setCurrentItem(2));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        saleText.setTextColor(getResources().getColor(R.color.black3));
        stockReportText.setTextColor(getResources().getColor(R.color.black3));
        closeDayText.setTextColor(getResources().getColor(R.color.black3));

        saleSummaryLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        stockReportLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        closeDayLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        switch (position) {
            case 0:
                saleSummaryLayout.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow));
                stockReportLayout.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_unselected));
                closeDayLayout.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_unselected));
                saleText.setTextColor(getResources().getColor(R.color.white1));
                break;

            case 1:
                stockReportLayout.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow));
                closeDayLayout.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_unselected));
                saleSummaryLayout.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_unselected));
                stockReportText.setTextColor(getResources().getColor(R.color.white1));
                break;

            case 2:
                closeDayLayout.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow));
                saleSummaryLayout.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_unselected));
                stockReportLayout.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_unselected));
                closeDayText.setTextColor(getResources().getColor(R.color.white1));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}

