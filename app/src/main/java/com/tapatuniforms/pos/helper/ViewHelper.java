package com.tapatuniforms.pos.helper;

import android.view.View;

public class ViewHelper {
    /**
     * Make a view Visible.
     * @param view View that needs to be visible
     */
    public static void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    /**
     * Hides a view.
     * @param view View that needs to be invisible.
     */
    public static void hideView(View view) {
        view.setVisibility(View.INVISIBLE);
    }
}
