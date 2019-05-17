package com.tapatuniforms.pos.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tapatuniforms.pos.fragment.InventoryTab1Fragment;
import com.tapatuniforms.pos.fragment.SetupItemFragment;
import com.tapatuniforms.pos.fragment.SetupStockFragment;

public class SetupPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public SetupPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SetupStockFragment();
            case 1:
                return new SetupItemFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
