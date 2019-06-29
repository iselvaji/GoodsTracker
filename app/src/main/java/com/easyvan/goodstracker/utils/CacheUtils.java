package com.easyvan.goodstracker.utils;

import android.content.Context;

import java.io.File;

import okhttp3.Cache;

import static com.easyvan.goodstracker.utils.AppConstants.CACHE_FILE_NAME;
import static com.easyvan.goodstracker.utils.AppConstants.CACHE_MAX_SIZE;

/**
 * Created by sm5 on 6/24/2019.
 */

public class CacheUtils {

    public static Cache getOfflineCache(Context context) {
        File httpCacheDir = new File(context.getCacheDir(), CACHE_FILE_NAME);
        Cache cache = null;
        if(cache == null) {
            cache = new Cache(httpCacheDir, CACHE_MAX_SIZE);
        }
        return cache;
    }
}
