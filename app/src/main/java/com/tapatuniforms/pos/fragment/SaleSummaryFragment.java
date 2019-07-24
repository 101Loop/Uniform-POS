package com.tapatuniforms.pos.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.EmailRecyclerAdapter;
import com.tapatuniforms.pos.dialog.ShareDialog;

import java.util.ArrayList;
import java.util.Objects;

public class SaleSummaryFragment extends Fragment {
    private Button shareButton;
    private ArrayList<String> emailList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup_stock, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        shareButton = view.findViewById(R.id.share);
        shareButton.setOnClickListener(v -> {
            ShareDialog dialog = new ShareDialog(getContext());
            dialog.show();

            Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.
                    LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        });
    }
}
