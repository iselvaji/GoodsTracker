package com.easyvan.goodstracker.view.adapters;

import android.location.Location;

import com.easyvan.goodstracker.model.rest.product.pojo.Product;

/**
 * Created by sm5 on 11/10/2018.
 */

public interface ItemSelectCallback {
    void onItemClicked(Product item, Location userLocation);
}
