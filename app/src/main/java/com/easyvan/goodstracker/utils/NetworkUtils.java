package com.easyvan.goodstracker.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sm5 on 6/12/2019.
 */

public class NetworkUtils {

    public static boolean isNetworkConnected(Context context) {

        //Context context = GoodsTrackerApplication.getContext();

        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
