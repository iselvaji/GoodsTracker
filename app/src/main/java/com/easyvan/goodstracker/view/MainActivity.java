package com.easyvan.goodstracker.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.easyvan.goodstracker.R;
import com.easyvan.goodstracker.model.rest.DataLoadState;
import com.easyvan.goodstracker.model.rest.map.CurrentLocationListener;
import com.easyvan.goodstracker.model.rest.product.pojo.Product;
import com.easyvan.goodstracker.utils.AppConstants;
import com.easyvan.goodstracker.view.adapters.ItemSelectCallback;
import com.easyvan.goodstracker.view.adapters.ProductListAdapter;
import com.easyvan.goodstracker.viewmodel.MainViewModel;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easyvan.goodstracker.utils.AppConstants.KEY_PRODUCT;
import static com.easyvan.goodstracker.utils.AppConstants.KEY_USER_LOCATION;
import static com.easyvan.goodstracker.utils.UIUtils.showSnackbarWithMsg;
import static com.easyvan.goodstracker.utils.UIUtils.showView;

public class MainActivity extends BaseActivity implements ItemSelectCallback {

    private MainViewModel mViewModel;

    private ProductListAdapter mRecyclerViewAdapter;

    @BindView(R.id.container)
    View mContainer;

    @BindView(R.id.recyclerviewItems)
    RecyclerView mRecyclerView;

    @BindView(R.id.textviewNoItems)
    View mTextViewNoItems;

    @BindString(R.string.error_generic_desc_message)
    String GenericError;

    @BindString(R.string.err_no_connectivity)
    String ErrorNoConnection;

    @BindString(R.string.msg_network_avalable)
    String ConnectionAvailable;

    @BindString(R.string.msg_offline_data)
    String MsgOfflineDataDisplay;

    @BindString(R.string.permisson_granted)
    String MsgPermissonGranted;

    @BindString(R.string.txt_title_products)
    String Title;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRecyclerViewAdapter = new ProductListAdapter(this, this);
        mViewModel = ViewModelProviders.of(MainActivity.this).get(MainViewModel.class);

        initUIElements();
    }

    private void initUIElements() {

        getSupportActionBar().setTitle(Title);
       // mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        loadData();
    }

    private void loadData() {

        mViewModel.getProducts().observe(this, new Observer<PagedList<Product>>() {
            @Override
            public void onChanged(@Nullable PagedList<Product> products) {
                if (products.size() != 0) {
                    mTextViewNoItems.setVisibility(View.GONE);
                    mRecyclerViewAdapter.submitList(products);
                }
                else {
                    mTextViewNoItems.setVisibility(View.VISIBLE);
                }
            }
        });

        mViewModel.getDataLoadLiveData().observe(this, new Observer<DataLoadState>() {
            @Override
            public void onChanged(@Nullable DataLoadState dataLoadState) {
                updateDataLoadingStateUI(dataLoadState);
            }
        });
    }

    private void updateDataLoadingStateUI(DataLoadState loadState) {

        switch (loadState.getStatus()){
            case RUNNING:
                showView(mProgressBar, true);
                break;
            case SUCCESS:
                showView(mProgressBar, false);
                break;
            case FAILED:
                showView(mProgressBar, false);
                showSnackbarWithMsg(mContainer, GenericError);
                break;
        }

    }

    @Override
    public void onConnectionStateChange(boolean connected) {

        if(connected) {
            //Snackbar.make(mContainer, ConnectionAvailable, Snackbar.LENGTH_SHORT).show();
            loadData();
        }
        else {
            showSnackbarWithMsg(mContainer, ErrorNoConnection + MsgOfflineDataDisplay);
        }
    }

    @Override
    public void onItemClicked(Product item, Location userLocation) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(KEY_PRODUCT, item);
        intent.putExtra(KEY_USER_LOCATION, userLocation);
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPermissionsGranted(int requestCode) {

        //Snackbar.make(mContainer, MsgPermissonGranted, Snackbar.LENGTH_SHORT).show();

        switch (requestCode) {
            case AppConstants.LOCATION_REQUEST:
                CurrentLocationListener.getInstance(getApplicationContext()).observe(this, new Observer<Location>() {
                    @Override
                    public void onChanged(@Nullable Location location) {
                        if (location != null && mRecyclerViewAdapter != null) {
                            mRecyclerViewAdapter.updateLocation(location);
                        }
                    }
                });
            }
     }
}
