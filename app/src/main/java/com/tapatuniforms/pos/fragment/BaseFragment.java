package com.tapatuniforms.pos.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tapatuniforms.pos.R;

import java.text.SimpleDateFormat;
import java.util.Date;

class BaseFragment extends Fragment {
    String getDate() {
        Date date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-YYYY");
        return dateFormat.format(date);
    }

    void onBackPressClicked() {
        FragmentManager fm = getFragmentManager();

        if (fm != null)
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new DashboardFragment()).commit();
    }
}
