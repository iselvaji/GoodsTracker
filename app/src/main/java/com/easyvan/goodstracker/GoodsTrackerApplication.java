package com.easyvan.goodstracker;

import android.app.Application;
import android.content.Context;

import java.io.File;

import okhttp3.Cache;

import static com.easyvan.goodstracker.utils.AppConstants.CACHE_FILE_NAME;
import static com.easyvan.goodstracker.utils.AppConstants.CACHE_MAX_SIZE;

/**
 * Created by sm5 on 6/15/2019.
 */

public class GoodsTrackerApplication extends Application {

    private static GoodsTrackerApplication mApplication;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static GoodsTrackerApplication getInstance() {
        if(mApplication == null) {
            synchronized (GoodsTrackerApplication.class) {
                if(mApplication == null) {
                    mApplication = new GoodsTrackerApplication();
                }
            }
        }
        return mApplication;
    }

    public Context getContext() {
        return mContext;
    }

    public Cache getOfflineCache() {
        File httpCacheDir = new File(mContext.getCacheDir(), CACHE_FILE_NAME);
        Cache cache = null;
        if(cache == null) {
            cache = new Cache(httpCacheDir, CACHE_MAX_SIZE);
        }
        return cache;

    }
}
