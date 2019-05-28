package com.tapatuniforms.pos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.UserListAdapter;
import com.tapatuniforms.pos.helper.ViewHelper;

public class PinLoginActivity extends AppCompatActivity {
    private View listView, loginView;
    private RecyclerView userListView;
    private ImageView backButton;
    private Button loginButton;
    private TextView addNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_login);

        initView();
    }

    private void initView() {
        listView = findViewById(R.id.listView);
        loginView = findViewById(R.id.loginView);
        userListView = findViewById(R.id.loginRecyclerView);
        backButton = findViewById(R.id.backButton);
        loginButton = findViewById(R.id.loginButton);
        addNewUser = findViewById(R.id.addNewUser);

        setUp();
    }

    private void setUp() {
        // Hide Login View initially
        ViewHelper.hideView(loginView);

        userListView.setLayoutManager(new LinearLayoutManager(this));
        UserListAdapter adapter = new UserListAdapter();
        userListView.setAdapter(adapter);
        adapter.setOnUserSelectedListener(() -> {
            ViewHelper.hideView(listView);
            ViewHelper.showView(loginView);
        });

        backButton.setOnClickListener((v) -> {
            ViewHelper.hideView(loginView);
            ViewHelper.showView(listView);
        });

        loginButton.setOnClickListener((v) -> {
            startActivity(new Intent(this, PosActivity.class));
            finish();
        });

        addNewUser.setOnClickListener((v) -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

}
