package com.easyvan.goodstracker.model.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;

import com.easyvan.goodstracker.model.rest.DataLoadState;
import com.easyvan.goodstracker.model.rest.product.RestApi;
import com.easyvan.goodstracker.model.rest.product.RestApiClient;
import com.easyvan.goodstracker.model.rest.product.pojo.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.easyvan.goodstracker.utils.AppConstants.FIRST_PAGE;
import static com.easyvan.goodstracker.utils.AppConstants.PAGE_SIZE;


/**
 * Created by sm5 on 6/13/2019.
 */

public class ProductDataSource extends PageKeyedDataSource<Integer, Product> {

    private final MutableLiveData<DataLoadState> mDataLodingState;
    private Context mContext;

    ProductDataSource(Context context) {
        mContext = context;
        mDataLodingState = new MutableLiveData<>();
    }

    public LiveData getDataLoadState() {
        return mDataLodingState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Product> callback) {

        mDataLodingState.postValue(DataLoadState.LOADING);
        RestApi api = RestApiClient.getClient(mContext).create(RestApi.class);
        Call<List<Product>> call = api.fetchProducts(FIRST_PAGE, PAGE_SIZE);

        try {
            Response<List<Product>> response = call.execute();
            if(response != null && response.isSuccessful()) {
                List<Product> products = response.body();
                if (products != null && products.size() != 0) {
                    callback.onResult(products, FIRST_PAGE, products.size() + 1);
                }
            }
            mDataLodingState.postValue(DataLoadState.LOADED);
        } catch (Exception e) {
            e.printStackTrace();
            mDataLodingState.postValue(DataLoadState.FAILED);
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {

        mDataLodingState.postValue(DataLoadState.LOADING);
        RestApi api = RestApiClient.getClient(mContext).create(RestApi.class);
        Call<List<Product>> call = api.fetchProducts(params.key, PAGE_SIZE);

        try {
            Response<List<Product>> response = call.execute();
            if(response != null && response.isSuccessful()) {
                List<Product> products = response.body();
                if(products != null && products.size() != 0) {
                    callback.onResult(products, params.key + PAGE_SIZE);
                }
            }
            mDataLodingState.postValue(DataLoadState.LOADED);

        } catch (Exception e) {
            e.printStackTrace();
            mDataLodingState.postValue(DataLoadState.FAILED);
        }
    }
}
