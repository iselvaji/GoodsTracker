package com.easyvan.goodstracker.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.easyvan.goodstracker.R;

/**
 * Created by sm5 on 6/12/2019.
 */

public class UIUtils {

    public static void showView(View view, boolean show) {

        if(view != null) {
            if(show) {
                view.setVisibility(View.VISIBLE);
            }
            else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public static void showSnackbarWithMsg(View container, String message) {

        final Snackbar snackbar = Snackbar.make(container, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
