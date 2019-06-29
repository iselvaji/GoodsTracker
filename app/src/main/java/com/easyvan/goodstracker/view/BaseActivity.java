package com.easyvan.goodstracker.view;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.easyvan.goodstracker.R;
import com.easyvan.goodstracker.utils.AppConstants;
import com.easyvan.goodstracker.utils.ConnectionLiveData;
import com.easyvan.goodstracker.utils.ConnectionModel;

import static com.easyvan.goodstracker.utils.PreferenceUtils.isLocationTrackEnabledInUI;
import static com.easyvan.goodstracker.utils.PreferenceUtils.updateLocationTrackUIPreference;

/**
 * Created by sm5 on 6/12/2019.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private SparseIntArray mErrorString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErrorString = new SparseIntArray();

        ConnectionLiveData connectionLiveData = new ConnectionLiveData(getApplicationContext());
        connectionLiveData.observe(this, new Observer<ConnectionModel>() {
            @Override
            public void onChanged(@Nullable ConnectionModel connection) {
                onConnectionStateChange(connection.isConnected());
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.txt_enable),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    protected void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mErrorString.put(requestCode, stringId);
            int permissionCheck = PackageManager.PERMISSION_GRANTED;
            boolean shouldShowRequestPermissionRationale = false;
            for (String permission : requestedPermissions) {
                permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
                shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
            }
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale) {
                    Snackbar.make(findViewById(android.R.id.content), stringId,
                            Snackbar.LENGTH_LONG).
                            setAction(getString(R.string.txt_grant),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(BaseActivity.this, requestedPermissions, requestCode);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
                }
            }
            else {
               onPermissionsGranted(requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    protected abstract void onPermissionsGranted(int requestCode);

    protected abstract void onConnectionStateChange(boolean connected);


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(Build.VERSION.SDK_INT > 11) {
            invalidateOptionsMenu();
            MenuItem menuLocation = menu.findItem(R.id.menuLocation);
            menuLocation.setChecked(isLocationTrackEnabledInUI(this));
            menuLocation.setVisible(! isLocationTrackEnabledInUI(this));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserLocation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLocation:
                item.setChecked(! item.isChecked());
                updateLocationTrackUIPreference(this, item.isChecked());
                getUserLocation();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void getUserLocation() {

        if(isLocationTrackEnabledInUI(this)) {
            requestAppPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    R.string.msg_runtime_permissions,
                    AppConstants.LOCATION_REQUEST);
        }
    }
}
