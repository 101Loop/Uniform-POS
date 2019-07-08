package com.tapatuniforms.pos;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tapatuniforms.pos.activity.DayClosingActivity;
import com.tapatuniforms.pos.activity.LoginActivity;
import com.tapatuniforms.pos.activity.PinLoginActivity;
import com.tapatuniforms.pos.activity.PosActivity;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.model.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseSingleton db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = DatabaseHelper.getDatabase(this);
        ArrayList<User> userList = (ArrayList<User>) db.userDao().getAll();

        if (userList.size() > 0) {
            startActivity(new Intent(this, PinLoginActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

//        startActivity(new Intent(this, PosActivity.class));
    }
}
