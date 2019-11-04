package com.tapatuniforms.pos.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tapatuniforms.pos.R;

class BaseFragment extends Fragment {
    void onBackPressClicked() {
        FragmentManager fm = getFragmentManager();

        if (fm != null)
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new DashboardFragment()).commit();
    }
}
