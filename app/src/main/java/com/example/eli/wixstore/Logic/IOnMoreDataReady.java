package com.example.eli.wixstore.Logic;

import com.example.eli.wixstore.Logic.NetWork.ProductData;

import java.util.List;

/**
 * Created by Eli on 14/10/2016.
 */

public interface IOnMoreDataReady {
    void onNoMoreData(int page);
    void onDataReturenedSuccessfully(List<ProductData> mProductList);
    void onDataFailed();

}
