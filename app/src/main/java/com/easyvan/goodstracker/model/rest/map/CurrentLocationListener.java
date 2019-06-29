package com.easyvan.goodstracker.model.rest.map;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.easyvan.goodstracker.utils.AppConstants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.easyvan.goodstracker.utils.AppConstants.UPDATE_INTERVAL_IN_MILLISECONDS;

/**
 * Created by sm5 on 6/17/2019.
 */

public class CurrentLocationListener extends LiveData<Location> implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static CurrentLocationListener mInstance;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Context mContext;

    private synchronized void buildGoogleApiClient(Context appContext) {
        mGoogleApiClient = new GoogleApiClient.Builder(appContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private CurrentLocationListener(Context appContext) {
        mContext = appContext;
        buildGoogleApiClient(appContext);
    }

    public static CurrentLocationListener getInstance(Context appContext) {
        if (mInstance == null) {
            mInstance = new CurrentLocationListener(appContext);
        }
        return mInstance;
    }

    @Override
    protected void onActive() {
        super.onActive();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (mGoogleApiClient.isConnected()) {
            getFusedLocationProviderClient().removeLocationUpdates(locationCallback);
        }
        mGoogleApiClient.disconnect();
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Task locationTask = getFusedLocationProviderClient().getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>()  {
            @Override
            public void onSuccess(Location location) {
                if(location != null)
                    setValue(location);
            }
        });

        if(hasActiveObservers() &&  mGoogleApiClient.isConnected()) {
            LocationRequest locationRequest = LocationRequest.create();
            //locationRequest.setSmallestDisplacement(MIN_DISTANCE_BETWEEN_UPDATES);
            locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            getFusedLocationProviderClient().requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null)
            setValue(location);
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location lastLocation = locationResult.getLastLocation();
            if(lastLocation != null)
                setValue(lastLocation);
        }
    };

    private FusedLocationProviderClient getFusedLocationProviderClient() {
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        }
        return mFusedLocationProviderClient;
    }
}