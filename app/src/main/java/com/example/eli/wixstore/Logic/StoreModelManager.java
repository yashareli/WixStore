package com.example.eli.wixstore.Logic;

import android.util.SparseArray;

import com.example.eli.wixstore.Logic.NetWork.ProductData;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eli on 13/10/2016.
 */

public class StoreModelManager implements IOnMoreDataReady {

    private static final int MAX_PAGE_HASNT_REACH = -1;
    private static StoreModelManager mInstance = null;
    private HashMap<String, String> mValuesCheckSum;
    private SparseArray<PageRequester> mRequestHistory;
    private int mMaxPage;

    private StoreModelManager() {
        mValuesCheckSum = new HashMap<String, String>();
        mRequestHistory = new SparseArray<>();
        mMaxPage = MAX_PAGE_HASNT_REACH;
    }
    public static StoreModelManager getInstance() {
        if(mInstance == null) {
            mInstance = new StoreModelManager();
        }
        return mInstance;
    }
    public void getPageData(int page, IOnMoreDataReady readyListener) {
        if(mMaxPage == MAX_PAGE_HASNT_REACH || mMaxPage>page) {
            PageRequester requestData = mRequestHistory.valueAt(page);
            if (requestData == null) {
                requestData = new PageRequester(page);
                mRequestHistory.setValueAt(page, requestData);
            }
            requestData.observe(readyListener);
            requestData.observe(this);
        }else {
            readyListener.onNoMoreData(mMaxPage);
        }
    }

    @Override
    public void onNoMoreData(int page) {
        mMaxPage = page;
    }

    @Override
    public void onDataReturenedSuccessfully(List<ProductData> mProductList) {

    }

    @Override
    public void onDataFailed() {

    }

    public List<ProductData> filterDuplicates(List<ProductData> products) {
        ArrayList<ProductData> fiteredList = new ArrayList<ProductData>();
        StoreModelManager modelManager = StoreModelManager.getInstance();
        for (ProductData product : products) {
            String checkSum = getCheckSum(product);
            if(!modelManager.mValuesCheckSum.containsKey(checkSum)) {
                modelManager.mValuesCheckSum.put(checkSum,checkSum);
                fiteredList.add(product);
            }
        }
        return fiteredList;
    }
    private String getCheckSum(ProductData product) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(product.getImageLink());
        stringBuilder.append(product.getPrice());
        stringBuilder.append(product.getTitle());
        return getMD5EncryptedString(stringBuilder.toString());
    }

    //http://stackoverflow.com/questions/13152736/how-to-generate-an-md5-checksum-for-a-file-in-android
    public static String getMD5EncryptedString(String encTarget){
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) {
            md5 = "0"+md5;
        }
        return md5;
    }

}
