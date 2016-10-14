package com.example.eli.wixstore.Logic.NetWork;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Eli on 14/10/2016.
 */

public interface ProductsListAPI {
    @GET("products.php")
    Call<List<ProductData>> getPage(@Query("page") int page);
}
