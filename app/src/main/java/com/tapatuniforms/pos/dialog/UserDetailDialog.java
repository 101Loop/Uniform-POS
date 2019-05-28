package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tapatuniforms.pos.R;

public class UserDetailDialog extends AlertDialog {
    TextView billingButton;

    public UserDetailDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dialog);

        billingButton = findViewById(R.id.billingButton);

        billingButton.setOnClickListener(v -> dismiss());
    }
}
