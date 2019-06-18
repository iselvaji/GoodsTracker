package com.easyvan.goodstracker.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.easyvan.goodstracker.model.rest.product.pojo.Location;
import com.easyvan.goodstracker.model.rest.map.MapApi;
import com.easyvan.goodstracker.model.rest.map.MapApiClient;
import com.easyvan.goodstracker.model.rest.map.pojo.MapRouteResponse;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.easyvan.goodstracker.utils.AppConstants.MAP_API_DISTANCE_UNIT;
import static com.easyvan.goodstracker.utils.AppConstants.MAP_API_MODE;
import static com.easyvan.goodstracker.utils.MapUtils.decodePoly;

/**
 * Created by sm5 on 6/12/2019.
 */

public class DetailsViewModel extends ViewModel {

    private final MutableLiveData<List<LatLng>> mRouteListLiveData = new MutableLiveData<>();

    public LiveData<List<LatLng>> getMapRoute(android.location.Location source, Location destination) {

        MapApi mapApi = MapApiClient.getClient().create(MapApi.class);

        mapApi.getDistanceDuration(MAP_API_DISTANCE_UNIT, source.getLatitude() +","+source.getLongitude(),
                destination.getLat() +"," + destination.getLng(), MAP_API_MODE)
                .enqueue(new Callback<MapRouteResponse>() {
                    @Override
                    public void onResponse(Call<MapRouteResponse> call, Response<MapRouteResponse> response) {
                        if(response.isSuccessful()) {
                            for (int i = 0; i < response.body().getRoutes().size(); i++) {
                                String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                                mRouteListLiveData.postValue(decodePoly(encodedString));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MapRouteResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

        return mRouteListLiveData;
    }

}
