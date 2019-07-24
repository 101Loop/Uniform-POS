package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.EmailRecyclerAdapter;

public class ShareDialog extends AlertDialog {
    private static final String TAG = "ShareDialog";

    private RecyclerView emailRecycler;
    private EmailRecyclerAdapter emailAdapter;

    public ShareDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_dialog_layout);

        emailRecycler = findViewById(R.id.emailRecyclerView);
        emailRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        emailAdapter = new EmailRecyclerAdapter();

        emailRecycler.setAdapter(new EmailRecyclerAdapter());
    }
}
