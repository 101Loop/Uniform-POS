package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.EmailRecyclerAdapter;
import com.tapatuniforms.pos.helper.Validator;

import java.util.ArrayList;

public class ShareDialog extends AlertDialog {
    private static final String TAG = "ShareDialog";

    private RecyclerView emailRecycler;
    private EmailRecyclerAdapter emailAdapter;
    private Button addButton;
    private EditText emailText;
    private Button closeButton;

    private ArrayList<String> emailList;

    public ShareDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_dialog_layout);

        emailList = new ArrayList<>();

        emailRecycler = findViewById(R.id.emailRecyclerView);
        addButton = findViewById(R.id.addButton);
        closeButton = findViewById(R.id.closeButton);
        emailText = findViewById(R.id.emailText);

        emailRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        emailAdapter = new EmailRecyclerAdapter(getContext(), emailList);
        emailRecycler.setAdapter(emailAdapter);

        addButton.setOnClickListener(view -> {
            String email = emailText.getText().toString();

            if (!email.isEmpty()) {

                if (!Validator.isValidEmail(email)) {
                    emailText.setError("This email is not valid");
                    return;
                }

                emailList.add(email);
                emailAdapter.notifyDataSetChanged();
                emailText.setText(null);
            }else{
                emailText.setError("This field can't be empty");
            }
        });

        closeButton.setOnClickListener(view -> {
            dismiss();
        });
    }
}
