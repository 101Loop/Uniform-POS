package com.tapatuniforms.pos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.DatabaseSingleton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginClicked(View view) {
        startActivity(new Intent(this, PosActivity.class));
        finish();
    }

    private void database() {
        DatabaseSingleton databaseSingleton = Room.databaseBuilder(this,
                DatabaseSingleton.class, getString(R.string.database))
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
}
