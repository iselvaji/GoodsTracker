package com.easyvan.goodstracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.easyvan.goodstracker.utils.AppConstants.KEY_PREFERENCE;
import static com.easyvan.goodstracker.utils.AppConstants.KEY_PREFERENCE_LOCATION_TRACK_ENABLED;

/**
 * Created by sm5 on 6/29/2019.
 */

public class PreferenceUtils {

    public static void updateLocationTrackUIPreference(Context context, boolean isLocationTrackEnabled) {

        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putBoolean(KEY_PREFERENCE_LOCATION_TRACK_ENABLED, isLocationTrackEnabled);
        editor.commit();
    }

    public static boolean isLocationTrackEnabledInUI(Context context) {
        return getPreference(context).getBoolean(KEY_PREFERENCE_LOCATION_TRACK_ENABLED, false);
    }

    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE);
    }
}
