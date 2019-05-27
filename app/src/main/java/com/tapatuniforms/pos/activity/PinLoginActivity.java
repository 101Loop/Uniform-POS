package com.tapatuniforms.pos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.PinLoginAdapter;

public class PinLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_login);

        RecyclerView loginRecyclerView = findViewById(R.id.loginRecyclerView);
        loginRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loginRecyclerView.setAdapter(new PinLoginAdapter());
    }
}
