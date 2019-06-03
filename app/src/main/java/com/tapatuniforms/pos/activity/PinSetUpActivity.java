package com.tapatuniforms.pos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;

public class PinSetUpActivity extends AppCompatActivity {
    private EditText pinEditText, confirmPinEditText;
    private Button saveButton;

    long id;

    DatabaseSingleton db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_set_up);

        id = getIntent().getLongExtra("id", -1);
        db = DatabaseHelper.getDatabase(this);

        initView();
    }

    private void initView() {
        pinEditText = findViewById(R.id.pinEditText);
        confirmPinEditText = findViewById(R.id.confirmPinEditText);
        saveButton = findViewById(R.id.saveButton);
    }

    public void saveButtonClicked(View view) {
        String pin = pinEditText.getText().toString().trim();
        String confirmPin = confirmPinEditText.getText().toString().trim();

        if (pin.length() < 4) {
            pinEditText.requestFocus();
            pinEditText.setError("Pin can't be less than 4");
        } else if (!confirmPin.equals(pin)) {
            confirmPinEditText.requestFocus();
            confirmPinEditText.setError("Pin didn't match");
        } else {
            db.userDao().setPin(id, pin);
            startActivity(new Intent(this, PinLoginActivity.class));
        }
    }
}
