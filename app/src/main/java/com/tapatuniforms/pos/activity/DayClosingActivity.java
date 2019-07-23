package com.tapatuniforms.pos.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.DayClosingPagerAdapter;
import com.tapatuniforms.pos.fragment.SaleSummaryFragment;

public class DayClosingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "DayClosingActivity";
    private TextView saleText, stockReportText, closeDayText;
    private ViewPager viewPager;
    private LinearLayout saleSummaryLayout;
    private LinearLayout stockReportLayout;
    private LinearLayout closeDayLayout;

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

        saleSummaryLayout.setOnClickListener(view -> viewPager.setCurrentItem(0));

        stockReportLayout.setOnClickListener(view -> viewPager.setCurrentItem(1));

        closeDayLayout.setOnClickListener(view -> viewPager.setCurrentItem(2));
    }

    private void initViews() {
        saleText = findViewById(R.id.saleText);
        stockReportText = findViewById(R.id.stockReport);
        closeDayText = findViewById(R.id.closeDay);
        saleSummaryLayout = findViewById(R.id.saleSummaryLayout);
        stockReportLayout = findViewById(R.id.stockReportLayout);
        closeDayLayout = findViewById(R.id.closeDayLayout);
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
                saleSummaryLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_arrow));
                saleText.setTextColor(getResources().getColor(R.color.white1));
                break;
            case 1:
                stockReportLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_arrow));
                stockReportText.setTextColor(getResources().getColor(R.color.white1));
                break;
            case 2:
                closeDayLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_arrow));
                closeDayText.setTextColor(getResources().getColor(R.color.white1));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
