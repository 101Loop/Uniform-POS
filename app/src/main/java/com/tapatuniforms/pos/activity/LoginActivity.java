package com.tapatuniforms.pos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.civilmachines.drfapi.DjangoErrorListener;
import com.civilmachines.drfapi.DjangoJSONObjectRequest;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.APIErrorListener;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.VolleySingleton;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText nameEditText, emailEditText, mobileEditText, otpEditText;
    private boolean isOtpSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    private void init() {
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        otpEditText = findViewById(R.id.otpEditText);

        otpEditText.setEnabled(false);
    }

    public void loginClicked(View view) {
        String mobileNumber = mobileEditText.getText().toString().trim();
        String otp = otpEditText.getText().toString().trim();

        if (!isOtpSent) {
            if (Validator.isValidMobile(mobileNumber) && !mobileNumber.isEmpty()) {
                JSONObject object = new JSONObject();
                sendRequest(object, RequestType.SEND);
            } else Toast.makeText(this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
        } else {
            if (!otp.isEmpty()) {
                JSONObject object = new JSONObject();
                sendRequest(object, RequestType.VERIFY);
            } else Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }


        startActivity(new Intent(this, PosActivity.class));
        finish();
    }

    private void sendRequest(JSONObject requestObject, RequestType requestType) {
        DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                Request.Method.POST, APIStatic.User.otpRegLoginURL, requestObject,
                response -> {
                    // Response Received
                }, new APIErrorListener(this), this);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    private void database() {
        DatabaseSingleton databaseSingleton = Room.databaseBuilder(this,
                DatabaseSingleton.class, getString(R.string.database))
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    private enum RequestType {
        SEND, VERIFY
    }
}
