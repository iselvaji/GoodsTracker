package com.easyvan.goodstracker.model.rest.map;

import com.easyvan.goodstracker.BuildConfig;
import com.easyvan.goodstracker.model.rest.map.pojo.MapRouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sm5 on 6/16/2019.
 */

public interface MapApi {

    @GET("api/directions/json?key="+ BuildConfig.GEO_API_KEY)
    Call<MapRouteResponse> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

}
