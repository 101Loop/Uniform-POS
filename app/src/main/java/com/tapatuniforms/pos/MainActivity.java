package com.tapatuniforms.pos;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tapatuniforms.pos.activity.LoginActivity;
import com.tapatuniforms.pos.activity.PinLoginActivity;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.model.User;

import java.util.ArrayList;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

public class MainActivity extends AppCompatActivity {
    private DatabaseSingleton db;
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.tapatuniforms.pos.helper.syncAdapter";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = BuildConfig.APPLICATION_ID;
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!BuildConfig.DEBUG) {
            Sentry.init(new AndroidSentryClientFactory(this));
        }

        mAccount = createSyncAccount(this);
        db = DatabaseHelper.getDatabase(this);
        ArrayList<User> userList = (ArrayList<User>) db.userDao().getAll();

        if (userList.size() > 0) {
            Intent intent = new Intent(this, PinLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

//        startActivity(new Intent(this, PosActivity.class));
    }

    public static Account createSyncAccount(Context context) {
        // Create the account type and default account

        return new Account(ACCOUNT, ACCOUNT_TYPE);
    }
}
