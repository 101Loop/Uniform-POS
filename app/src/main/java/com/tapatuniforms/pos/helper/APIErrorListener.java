package com.tapatuniforms.pos.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.civilmachines.drfapi.DjangoErrorListener;

import org.json.JSONObject;

public class APIErrorListener extends DjangoErrorListener {
    private static final String TAG = "APIErrorListener";
    private Context context;

    public APIErrorListener(Context context) {
        this.context = context;
    }

    @Override
    public void onNetworkError(String response) {
        defaultErrorListener(response);
    }

    @Override
    public void onMethodNotAllowedError(String message) {
        defaultErrorListener(message);
    }

    @Override
    public void onAuthFailureError(String response) {
        defaultErrorListener(response);
    }

    @Override
    public void onBadRequestError(String message) {
        defaultErrorListener(message);
    }

    @Override
    public void onBadRequestError(JSONObject response) {
        defaultErrorListener("Bad Request. Data sent in incorrect format!");
    }

    @Override
    public void onDefaultError(String response) {
        defaultErrorListener(response);
    }

    @Override
    public void onDefaultHTMLError(String response) {
        defaultErrorListener(response);
    }

    @Override
    public void onDefaultJsonError(JSONObject response) {
        defaultErrorListener("Some JSON Error occurred.");
    }

    @Override
    public void onForbiddenError(String message) {
        defaultErrorListener(message);
    }

    @Override
    public void onNoConnectionError(String response) {
        defaultErrorListener(response);
    }

    @Override
    public void onNonJsonError(String response) {
        defaultErrorListener(response);
    }

    @Override
    public void onNotFoundError(String message) {
        defaultErrorListener(message);
    }

    @Override
    public void onParseError(String response) {
        defaultErrorListener(response);
    }

    @Override
    public void onServerError(String response) {
        defaultErrorListener(response);
    }

    @Override
    public void onTimeoutError(String response) {
        defaultErrorListener(response);
    }

    @Override
    public void onUnprocessableEntityError(String message) {
        defaultErrorListener(message);
    }

    @Override
    public void onUnprocessableEntityError(JSONObject response) {
        defaultErrorListener("Unprocessable Entity");
    }

    @Override
    public void onUnsupportedMediaTypeError(String message) {
        defaultErrorListener(message);
    }

    public void defaultErrorListener(String message) {
        Log.e(TAG, "defaultErrorListener: " + message);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            super.onErrorResponse(error);
        } catch (Exception e) {
            Toast.makeText(context, "Can't connect to the server.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onErrorResponse: " + error.getMessage());
        }
    }
}
