package com.easyvan.goodstracker.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import com.easyvan.goodstracker.model.datasource.ProductDataFactory;
import com.easyvan.goodstracker.model.datasource.ProductDataSource;
import com.easyvan.goodstracker.model.rest.DataLoadState;
import com.easyvan.goodstracker.model.rest.product.pojo.Product;

import static com.easyvan.goodstracker.utils.AppConstants.PAGE_SIZE;

/**
 * Created by sm5 on 6/12/2019.
 */

public class MainViewModel extends AndroidViewModel {

    private LiveData<PagedList<Product>> mPagedListLiveData;
    private final LiveData<DataLoadState> mDataLoadStateLiveData;
    private final ProductDataFactory mDataFactory;

    public MainViewModel(Application application) {
        super(application);

        mDataFactory = new ProductDataFactory(application.getApplicationContext());

        initPagedList();

        mDataLoadStateLiveData = Transformations.switchMap(mDataFactory.getDataSourceLiveData(), new Function<PageKeyedDataSource<Integer, Product>, LiveData<DataLoadState>>() {
                    @Override
                    public LiveData<DataLoadState> apply(PageKeyedDataSource<Integer, Product> input) {
                        ProductDataSource source = (ProductDataSource)input;
                        return source.getDataLoadState();
                    }
                });
    }

    private void initPagedList() {

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(20)
                .setPageSize(PAGE_SIZE).build();

        mPagedListLiveData = (new LivePagedListBuilder(mDataFactory, pagedListConfig)).build();
    }

    public LiveData<PagedList<Product>> getProducts() {
        return mPagedListLiveData;
    }

    public LiveData<DataLoadState> getDataLoadLiveData() {
        return mDataLoadStateLiveData;
    }

}
