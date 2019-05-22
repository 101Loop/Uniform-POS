package com.tapatuniforms.pos.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tapatuniforms.pos.fragment.DayClosingStockFragment;
import com.tapatuniforms.pos.fragment.DayCompleteFragment;
import com.tapatuniforms.pos.fragment.SaleSummaryFragment;

public class DayClosingPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public DayClosingPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SaleSummaryFragment();
            case 1:
                return new DayClosingStockFragment();
            case 2:
                return new DayCompleteFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
