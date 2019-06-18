package com.easyvan.goodstracker.model.rest.map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sm5 on 6/12/2019.
 */

public class MapApiClient {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/";
    private static Retrofit mRetrofit = null;

    public static Retrofit getClient() {

        if (mRetrofit == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return mRetrofit;
    }
}
