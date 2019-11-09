package com.tapatuniforms.pos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText destinationView;
    private boolean isOtpSent = false;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        destinationView = findViewById(R.id.destinationView);
    }

    public void loginClicked(View view) {
        String destination = destinationView.getText().toString().trim();

        if (destination.isEmpty()) {
            destinationView.requestFocus();
            destinationView.setError("This field can't be empty");
        } else if (destination.length() < 5 || (!Validator.isValidEmail(destination) && !Validator.isValidMobile(destination))) {
            destinationView.requestFocus();
            destinationView.setError("Invalid value");
        } else {
            try {
                destinationView.setError(null);
                sendRequest(destination);
            } catch (JSONException e) {
                dismissDialog();
                e.printStackTrace();
            }
        }
    }

    private void sendRequest(String destination) throws JSONException {
        if (!Validator.isNetworkConnected(this)) {
            Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();
        JSONObject requestObject = new JSONObject();
        requestObject.put(APIStatic.Key.isLogin, true);
        requestObject.put(APIStatic.Key.destination, destination);

        Log.d(TAG, "sendRequest: " + requestObject);

        DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                Request.Method.POST, APIStatic.User.loginOTPURL, requestObject,
                response -> {
                    // Response Received
                    dismissDialog();
                    Toast.makeText(getApplicationContext(), "OTP Sent", Toast.LENGTH_SHORT)
                            .show();

                    Intent intent = new Intent(this, OtpActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(AppStatic.mobile, destination);
                    intent.putExtra(AppStatic.isLogin, true);
                    startActivity(intent);
                    finish();
                }, new APIErrorListener(this, dialog), this);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    public void signUpClicked(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
