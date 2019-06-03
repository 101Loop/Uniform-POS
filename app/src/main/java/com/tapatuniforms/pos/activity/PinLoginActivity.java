package com.tapatuniforms.pos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.UserListAdapter;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.ViewHelper;
import com.tapatuniforms.pos.model.User;

import java.util.ArrayList;

public class PinLoginActivity extends AppCompatActivity {
    private View listView, loginView;
    private RecyclerView userListView;
    private EditText pinEditText;
    private ImageView backButton;
    private Button loginButton;
    private TextView addNewUser;

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
        listView = findViewById(R.id.listView);
        loginView = findViewById(R.id.loginView);
        userListView = findViewById(R.id.loginRecyclerView);
        pinEditText = findViewById(R.id.pinEditText);
        backButton = findViewById(R.id.backButton);
        loginButton = findViewById(R.id.loginButton);
        addNewUser = findViewById(R.id.addNewUser);

        setUp();
    }

    private void setUp() {
        // Hide Login View initially
        ViewHelper.hideView(loginView);

        userListView.setLayoutManager(new LinearLayoutManager(this));
        userList = (ArrayList<User>) db.userDao().getAll();
        UserListAdapter adapter = new UserListAdapter(userList);
        userListView.setAdapter(adapter);
        adapter.setOnUserSelectedListener((user) -> {
            ViewHelper.hideView(listView);
            ViewHelper.showView(loginView);
            this.user = user;

            if (user.getPin().isEmpty()) {
                Intent intent = new Intent(this, PinSetUpActivity.class);
                intent.putExtra("id", user.getId());
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener((v) -> {
            ViewHelper.hideView(loginView);
            ViewHelper.showView(listView);
            user = null;
        });

        loginButton.setOnClickListener((v) -> handleLoginClick());

        addNewUser.setOnClickListener((v) -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void handleLoginClick() {
        String pin = pinEditText.getText().toString().trim();

        if (!pin.equals(user.getPin())) {
            pinEditText.requestFocus();
            pinEditText.setError("Pin didn't match");
        } else {
            startActivity(new Intent(this, PosActivity.class));
        }
    }

}
