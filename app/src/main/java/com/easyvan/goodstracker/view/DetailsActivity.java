package com.easyvan.goodstracker.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easyvan.goodstracker.R;
import com.easyvan.goodstracker.model.rest.map.CurrentLocationListener;
import com.easyvan.goodstracker.model.rest.product.pojo.Product;
import com.easyvan.goodstracker.utils.MapUtils;
import com.easyvan.goodstracker.viewmodel.DetailsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easyvan.goodstracker.utils.AppConstants.KEY_PRODUCT;
import static com.easyvan.goodstracker.utils.AppConstants.KEY_USER_LOCATION;
import static com.easyvan.goodstracker.utils.AppConstants.LOCATION_REQUEST;

/**
 * Created by sm5 on 6/13/2019.
 */

public class DetailsActivity extends BaseActivity implements OnMapReadyCallback{

    @Nullable
    private GoogleMap gmap;
    private Product mProduct;
    private Location mCurrentlocation;

    private DetailsViewModel mViewModel;

    @BindView(R.id.textview_location)
    TextView mTextViewLocation;

    @BindView(R.id.textview_description)
    TextView mTextViewDescription;

    @BindView(R.id.textview_distance)
    TextView mTextViewDistance;

    @BindView(R.id.image_product)
    ImageView mImageView;

    @BindView(R.id.container)
    View mContainer;

    @BindString(R.string.err_no_connectivity)
    String ErrorNoConnection;

    @BindString(R.string.msg_network_avalable)
    String ConnectionAvailable;

    @BindString(R.string.txt_title_details)
    String Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle(Title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        mViewModel = ViewModelProviders.of(DetailsActivity.this).get(DetailsViewModel.class);

    }

    private void displayProductDetails() {

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            mProduct = (Product) extras.get(KEY_PRODUCT);
            mCurrentlocation = (Location) extras.get(KEY_USER_LOCATION);

            if(mProduct != null) {
                mTextViewDescription.setText(mProduct.getDescription());
                mTextViewLocation.setText(mProduct.getLocation().getAddress());

                if (mProduct.getImageUrl() != null) {
                    Glide.with(this)
                            .load(mProduct.getImageUrl())
                            .error(Glide.with(this)
                                    .load(R.mipmap.ic_launcher))
                            .into(mImageView);
                }

                displayLocationDetails(mCurrentlocation);
            }

        }
    }

    private void displayLocationDetails(Location location) {

        if(location != null && mProduct != null) {
            double distance = MapUtils.distanceBetween(location.getLatitude(), location.getLongitude(),
                    mProduct.getLocation().getLat(), mProduct.getLocation().getLng(), "K");

            mTextViewDistance.setText(getString(R.string.txt_unit_km, String.valueOf(distance)));

            mViewModel.getMapRoute(location, mProduct.getLocation()).observe(this, new Observer<List<LatLng>>() {
                @Override
                public void onChanged(@Nullable List<LatLng> latLngs) {
                    gmap.addPolyline(new PolylineOptions()
                            .addAll(latLngs).width(20).color(Color.RED).geodesic(true));

                }

            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gmap = googleMap;
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        gmap.getUiSettings().setZoomControlsEnabled(true);
        gmap.getUiSettings().setZoomGesturesEnabled(true);
        gmap.getUiSettings().setCompassEnabled(true);
        gmap.setMinZoomPreference(12);

        displayProductDetails();

        if(mProduct != null) {
            LatLng latLng = new LatLng(mProduct.getLocation().getLat(), mProduct.getLocation().getLng());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            gmap.addMarker(markerOptions);

            gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPermissionsGranted(int requestCode) {

        switch (requestCode) {
            case LOCATION_REQUEST:
                if(gmap != null) {
                    gmap.setMyLocationEnabled(true);
                }

                CurrentLocationListener.getInstance(getApplicationContext()).observe(this, new Observer<Location>() {
                    @Override
                    public void onChanged(@Nullable Location location) {
                        if (location != null) {
                            mCurrentlocation = location;
                            displayLocationDetails(location);
                        }
                    }
                });
        }
    }

    @Override
    public void onConnectionStateChange(boolean connected) {
        if(! connected) {
            Snackbar.make(mContainer, ErrorNoConnection, Snackbar.LENGTH_SHORT).show();
        }
    }
}