package com.easyvan.goodstracker.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.easyvan.goodstracker.GoodsTrackerApplication;

/**
 * Created by sm5 on 6/12/2019.
 */

public class NetworkUtils {

    public static boolean isNetworkConnected() {

        Context context = GoodsTrackerApplication.getInstance().getContext();

        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
