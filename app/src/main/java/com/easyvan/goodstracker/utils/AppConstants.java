package com.easyvan.goodstracker.utils;

/**
 * Created by sm5 on 6/15/2019.
 */

public class AppConstants {

    public static final int LOCATION_REQUEST = 1000;
    public static final String MAP_API_DISTANCE_UNIT = "metric";
    public static final String MAP_API_MODE = "driving";

    // Cache
    public static final String CACHE_FILE_NAME = "offlineCache";
    public static final long CACHE_MAX_SIZE = 10 * 1024 * 1024; // 10MB

    public static final long CACHE_MAX_STALE = 60 * 60 * 24; // 24 hours
    public static final int CACHE_MAX_AGE = 5;

    // Pagination
    public static final int PAGE_SIZE = 20;
    public static final int FIRST_PAGE = 1;

    public static final String KEY_PRODUCT = "com.easyvan.goodstracker.product";
    public static final String KEY_USER_LOCATION = "com.easyvan.goodstracker.location";

    // location update
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000; //0.5 min
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS  = 15000;
    public static final long MIN_DISTANCE_BETWEEN_UPDATES = 20; //20 meter
}
