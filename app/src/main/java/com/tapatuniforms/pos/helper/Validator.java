package com.tapatuniforms.pos.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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

    public static void hideKeyBoardEditText(Activity activity, EditText editText) {
        editText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(activity);
                return true;
            }

            return false;
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();

        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
