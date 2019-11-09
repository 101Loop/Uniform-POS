package com.tapatuniforms.pos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.civilmachines.drfapi.DjangoJSONObjectRequest;
import com.civilmachines.drfapi.UserSharedPreferenceAdapter;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.APIErrorListener;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.AppStatic;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.VolleySingleton;
import com.tapatuniforms.pos.model.User;

import org.json.JSONObject;

public class OtpActivity extends AppCompatActivity {
    private EditText otpView;

    private String name, email, mobile;
    private boolean isLogin;

    private DatabaseSingleton db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Intent intent = getIntent();

        isLogin = intent.getBooleanExtra(AppStatic.isLogin, true);

        if (isLogin) {
            mobile = intent.getStringExtra(AppStatic.mobile);
        } else {
            name = intent.getStringExtra(AppStatic.name);
            email = intent.getStringExtra(AppStatic.email);
            mobile = intent.getStringExtra(AppStatic.mobile);
        }

        db = DatabaseHelper.getDatabase(this);
        init();
    }

    private void init() {
        otpView = findViewById(R.id.otpEditText);
    }

    public void verifyOtpClicked(View view) {
        try {
            String otp = otpView.getText().toString().trim();
            JSONObject requestData = new JSONObject();

            if (otp.length() < 3) {
                otpView.requestFocus();
                otpView.setError("OTP is not valid");
                return;
            }

            if (isLogin) {
                requestData.put(APIStatic.Key.isLogin, true);
                requestData.put(APIStatic.Key.destination, mobile);
                requestData.put(APIStatic.Key.verifyOtp, otp);

                verifyOtp(APIStatic.User.loginOTPURL, requestData);
            } else {
                requestData.put(APIStatic.Key.name, name);
                requestData.put(APIStatic.Key.email, email);
                requestData.put(APIStatic.Key.mobile, mobile);
                requestData.put(APIStatic.Key.verifyOtp, otp);

                verifyOtp(APIStatic.User.otpRegLoginURL, requestData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verifyOtp(String url, JSONObject requestData) {
        if (!Validator.isNetworkConnected(this)) {
            Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }

        DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                Request.Method.POST, url, requestData,
                response -> {
                    // Response Received
                    Toast.makeText(getApplicationContext(), "OTP Verified", Toast.LENGTH_SHORT)
                            .show();

                    UserSharedPreferenceAdapter usrAdap = new UserSharedPreferenceAdapter(this);
                    String token = response.optString(APIStatic.Key.token);
                    usrAdap.saveToken(token);

                    User user = new User(token);
                    User existingUser = db.userDao().getUserByMobile(user.getMobile());

                    if (existingUser == null) {
                        db.userDao().insert(user);
                    } else {
                        db.userDao().updateToken(user.getMobile(), token);
                    }

                    Intent intent = new Intent(this, PinLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }, new APIErrorListener(this), this);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }
}
