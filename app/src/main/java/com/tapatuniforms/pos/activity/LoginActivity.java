package com.tapatuniforms.pos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.civilmachines.drfapi.DjangoJSONObjectRequest;
import com.civilmachines.drfapi.UserSharedPreferenceAdapter;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.APIErrorListener;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.VolleySingleton;
import com.tapatuniforms.pos.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText nameEditText, emailEditText, mobileEditText, otpEditText;
    private boolean isOtpSent = false;

    private DatabaseSingleton db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = DatabaseHelper.getDatabase(this);

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
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String mobileNumber = mobileEditText.getText().toString().trim();
        String otp = otpEditText.getText().toString().trim();

        if (!isOtpSent) {
            if (name.length() < 3) {
                nameEditText.requestFocus();
                nameEditText.setError("Name not valid");
            } else if (!Validator.isValidEmail(email) || email.isEmpty()) {
                emailEditText.requestFocus();
                emailEditText.setError("Email not valid");
            } else if (!Validator.isValidMobile(mobileNumber) || mobileNumber.isEmpty()) {
                mobileEditText.requestFocus();
                mobileEditText.setError("Mobile Number not valid");
            } else {
                try {
                    JSONObject object = new JSONObject();
                    object.put(APIStatic.keyName, name);
                    object.put(APIStatic.User.keyEmail, email);
                    object.put(APIStatic.User.keyMobile, mobileNumber);
                    sendRequest(object, RequestType.SEND);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (!otp.isEmpty() && otp.length() > 4) {
                try {
                    JSONObject object = new JSONObject();
                    object.put(APIStatic.keyName, name);
                    object.put(APIStatic.User.keyEmail, email);
                    object.put(APIStatic.User.keyMobile, mobileNumber);
                    object.put(APIStatic.User.keyVerifyOTP, otp);
                    sendRequest(object, RequestType.VERIFY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                otpEditText.requestFocus();
                otpEditText.setError("Enter a Valid OTP");
            }
        }
    }

    private void sendRequest(JSONObject requestObject, RequestType requestType) {
        Log.d(TAG, "sendRequest: " + requestObject);
        DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                Request.Method.POST, APIStatic.User.otpRegLoginURL, requestObject,
                response -> {
                    // Response Received
                    if (requestType.equals(RequestType.SEND)) {
                        isOtpSent = true;
                        otpEditText.setEnabled(true);
                    } else {
                        UserSharedPreferenceAdapter usrAdap = new UserSharedPreferenceAdapter(this);
                        String token = response.optString(APIStatic.User.keyToken);
                        usrAdap.saveToken(token);

                        long id = db.userDao().insert(new User(token));

                        Intent intent = new Intent(this, PinSetUpActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        finish();
                    }
                }, new APIErrorListener(this), this);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    private enum RequestType {
        SEND, VERIFY
    }
}
