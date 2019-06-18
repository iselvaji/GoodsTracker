package com.easyvan.goodstracker.model.rest.product;

import com.easyvan.goodstracker.model.rest.product.pojo.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sm5 on 6/12/2019.
 */

public interface RestApi {

    @GET("/deliveries")
    Call<List<Product>> fetchProducts(@Query("offset") int offset, @Query("limit") int limit);

}
