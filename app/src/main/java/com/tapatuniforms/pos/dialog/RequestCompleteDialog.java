package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.tapatuniforms.pos.R;

public class RequestCompleteDialog extends AlertDialog {

    public RequestCompleteDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_complete_dialog);

        Runnable updateTask = new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        };

        new Handler().postDelayed(updateTask, 2500);
    }
}
