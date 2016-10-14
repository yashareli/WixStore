package com.example.eli.wixstore.Logic.NetWork;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eli on 13/10/2016.
 */

public class ProductsGetRequest implements Callback<List<ProductData>> {

    public static final String BASE_URL = "https://stark-atoll-33661.herokuapp.com/products.php/";
    private Retrofit mRetrofit;
    private IOnPageRequestReturned mListener;
    public ProductsGetRequest(IOnPageRequestReturned listener) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mListener = listener;
    }
    public void getPage(int pageNum) {
        ProductsListAPI productsListAPI = mRetrofit.create(ProductsListAPI.class);
        Call<List<ProductData>> call = productsListAPI.getPage(pageNum);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<ProductData>> call, Response<List<ProductData>> response) {
        if(mListener != null) {
            mListener.onCallSucceeded(response.body());
        }
    }

    @Override
    public void onFailure(Call<List<ProductData>> call, Throwable t) {
        if(mListener != null) {
            mListener.onCallFailed();
        }
    }
}
