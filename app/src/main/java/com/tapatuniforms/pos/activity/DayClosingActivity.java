package com.tapatuniforms.pos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.DayClosingPagerAdapter;

public class DayClosingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private TextView saleText, stockReportText, closeDayText;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_closing);

        initViews();

        DayClosingPagerAdapter dayClosingPagerAdapter = new DayClosingPagerAdapter(this,
                getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(dayClosingPagerAdapter);

        viewPager.addOnPageChangeListener(this);
    }

    private void initViews() {
        saleText = findViewById(R.id.saleText);
        stockReportText = findViewById(R.id.stockReport);
        closeDayText = findViewById(R.id.closeDay);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        saleText.setTextColor(getResources().getColor(R.color.black3));
        stockReportText.setTextColor(getResources().getColor(R.color.black3));
        closeDayText.setTextColor(getResources().getColor(R.color.black3));

        switch (position) {
            case 0:
                saleText.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                stockReportText.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                closeDayText.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
