package com.easyvan.goodstracker.model.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.easyvan.goodstracker.model.rest.product.pojo.Product;

/**
 * Created by sm5 on 6/13/2019.
 */

public class ProductDataFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, Product>> mDataSourceLiveData = new MutableLiveData<>();

    @Override
    public DataSource create() {
        ProductDataSource productDataSource = new ProductDataSource();
        mDataSourceLiveData.postValue(productDataSource);
        return productDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getDataSourceLiveData() {
        return mDataSourceLiveData;
    }


}
