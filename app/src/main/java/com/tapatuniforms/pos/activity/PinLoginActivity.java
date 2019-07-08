package com.tapatuniforms.pos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.civilmachines.drfapi.UserSharedPreferenceAdapter;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.model.User;

import java.util.ArrayList;

public class PinLoginActivity extends AppCompatActivity {
    private static final String TAG = "PinLoginActivity";
    private Spinner userSpinner;

    private ArrayList<User> userList;
    private User user;

    private DatabaseSingleton db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_login);

        db = DatabaseHelper.getDatabase(this);

        initView();
    }

    private void initView() {
        userSpinner = findViewById(R.id.userSpinner);

        setUp();
    }

    private void setUp() {
        userList = (ArrayList<User>) db.userDao().getAll();
        ArrayAdapter<User> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, userList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(spinnerAdapter);
    }

    public void loginButtonClicked(View view) {
        User user = (User) userSpinner.getSelectedItem();

        if (user != null) {
            UserSharedPreferenceAdapter usrAdap = new UserSharedPreferenceAdapter(this);
            usrAdap.saveToken(user.getToken());
            startActivity(new Intent(this, PosActivity.class));
            finish();
        }
    }

    public void loginClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void signUpClicked(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }
}
