package com.tapatuniforms.pos.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

public class Validator {
    /**
     * Check if Email Address is Valid
     * @param email email that needs to be checked
     * @return return true if email is valid, else false
     */
    public static boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Check if Mobile number is valid
     * @param phone mobile number that needs to be checked
     * @return return true if number is valid, else false
     */
    public static boolean isValidMobile(CharSequence phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    /**
     * Checks if device is connected to the internet
     * @param context Application Context
     * @return returns true if network is connected else returns false
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * sets an empty text error
     * @param inputLayout input layout on which error is to be set
     * */
    public static void setEmptyError(TextInputLayout inputLayout){
        inputLayout.setError("This field can't be empty");
    }
}
