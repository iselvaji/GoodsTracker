package com.easyvan.goodstracker.model.rest.product;

import com.easyvan.goodstracker.GoodsTrackerApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.easyvan.goodstracker.utils.AppConstants.CACHE_MAX_AGE;
import static com.easyvan.goodstracker.utils.AppConstants.CACHE_MAX_STALE;
import static com.easyvan.goodstracker.utils.NetworkUtils.isNetworkConnected;

/**
 * Created by sm5 on 6/12/2019.
 */

public class RestApiClient {

    private static final String BASE_URL = "https://mock-api-mobile.dev.lalamove.com/";
    private static Retrofit mRetrofit = null;

    public static Retrofit getClient() {

        if (mRetrofit == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .cache(GoodsTrackerApplication.getInstance().getOfflineCache())
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            if(isNetworkConnected()){
                                request = request.newBuilder().header("Cache-Control", "public, max-age=" + CACHE_MAX_AGE).build();
                            }
                            else {
                                request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_MAX_STALE).build();
                            }
                            return chain.proceed(request);
                        }
                    });

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return mRetrofit;
    }
}
