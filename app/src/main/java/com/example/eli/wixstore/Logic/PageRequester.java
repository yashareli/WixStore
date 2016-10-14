package com.example.eli.wixstore.Logic;

import com.example.eli.wixstore.Logic.NetWork.IOnPageRequestReturned;
import com.example.eli.wixstore.Logic.NetWork.ProductData;
import com.example.eli.wixstore.Logic.NetWork.ProductsGetRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eli on 14/10/2016.
 */

public class PageRequester implements IOnPageRequestReturned {

    private enum eQueryStatus{
        InProgress,
        Succeded,
        Failed,
        NoMoreData
    }
    private final ArrayList<IOnMoreDataReady> mReadyListeners;
    private eQueryStatus mQueryStatus;
    private ArrayList<ProductData> mProductList;
    private int mQuery;
    public PageRequester(int query) {
        mReadyListeners = new ArrayList<>();
        mQueryStatus = eQueryStatus.InProgress;
        mProductList = new ArrayList<>();
        mQuery = query;
        ProductsGetRequest request = new ProductsGetRequest(this);
        request.getPage(query);
    }
    public void observe(IOnMoreDataReady listener) {
        synchronized (mReadyListeners) {
            if(mQueryStatus == eQueryStatus.InProgress) {
                mReadyListeners.add(listener);
            } else {
                sendResultToListener(listener);
            }
        }
    }
    private void sendResultToListener(IOnMoreDataReady listener) {
        synchronized (mReadyListeners) {
            switch(mQueryStatus) {
                case Succeded:
                    listener.onDataReturenedSuccessfully(mProductList);
                    break;
                case Failed:
                    listener.onDataFailed();
                    break;
                case NoMoreData:
                    listener.onNoMoreData(mQuery);
                    break;
            }
        }
    }
    @Override
    public void onCallSucceeded(List<ProductData> products) {
        synchronized (mReadyListeners) {
            StoreModelManager modelManager = StoreModelManager.getInstance();
            if (products.size() > 0) {
                List<ProductData> filteredList = modelManager.filterDuplicates(products);
                if (filteredList.size() > 0) {
                    mProductList.addAll(filteredList);
                }
                mQueryStatus = eQueryStatus.Succeded;
            }else {
                mQueryStatus = eQueryStatus.NoMoreData;
            }
            notifyObservers();
        }
    }
    @Override
    public void onCallFailed() {
        synchronized (mReadyListeners) {
            mQueryStatus = eQueryStatus.Failed;
            notifyObservers();
        }
    }
    private void notifyObservers() {
        synchronized (mReadyListeners) {
            for(IOnMoreDataReady listener : mReadyListeners) {
                sendResultToListener(listener);
            }
        }
    }

}
