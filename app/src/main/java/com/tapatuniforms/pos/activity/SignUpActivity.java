package com.tapatuniforms.pos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.civilmachines.drfapi.DjangoJSONObjectRequest;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.APIErrorListener;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.AppStatic;
import com.tapatuniforms.pos.helper.CustomRelativeLayout;
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
    private CustomRelativeLayout signUpLayout;
    private EditText nameEditText, emailEditText, mobileEditText;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }

    private void init() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        signUpLayout = findViewById(R.id.activity_landing);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
    }

    public void SignUpClicked(View view) {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String mobile = mobileEditText.getText().toString().trim();

        if (name.isEmpty()) {
            nameEditText.requestFocus();
            nameEditText.setError("Name Can't be Empty");
        } else if (!Validator.isValidEmail(email)) {
            emailEditText.requestFocus();
            emailEditText.setError("Email Not Valid");
        } else if (!Validator.isValidMobile(mobile)) {
            mobileEditText.requestFocus();
            mobileEditText.setError("Mobile Number not valid");
        } else {
            try {
                sendRequest(name, email, mobile);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRequest(String name, String email, String mobile) throws JSONException {
        if (!Validator.isNetworkConnected(this)) {
            Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();
        JSONObject requestObject = new JSONObject();
        requestObject.put(APIStatic.Key.name, name);
        requestObject.put(APIStatic.Key.email, email);
        requestObject.put(APIStatic.Key.mobile, mobile);

        DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                Request.Method.POST, APIStatic.User.otpRegLoginURL, requestObject,
                response -> {
                    // Response Received

                    Toast.makeText(getApplicationContext(), "OTP Sent", Toast.LENGTH_SHORT)
                            .show();

                    dismissDialog();
                    Intent intent = new Intent(this, OtpActivity.class);
                    intent.putExtra(AppStatic.name, name);
                    intent.putExtra(AppStatic.email, email);
                    intent.putExtra(AppStatic.mobile, mobile);
                    intent.putExtra(AppStatic.isLogin, false);
                    startActivity(intent);
                    finish();
                }, new APIErrorListener(this, dialog), this);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    public void loginClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showProgressDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }

        dialog.setTitle(R.string.dialog_title);
        dialog.setMessage(getResources().getString(R.string.dialog_message));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
