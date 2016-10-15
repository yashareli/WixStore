package com.example.eli.wixstore.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.eli.wixstore.Logic.IOnMoreDataReady;
import com.example.eli.wixstore.Logic.NetWork.ProductData;
import com.example.eli.wixstore.Logic.StoreModelManager;
import com.example.eli.wixstore.R;
import com.example.eli.wixstore.View.PagingListSourceCode.PagingListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eli on 14/10/2016.
 */

public class ListPager extends RelativeLayout implements IOnMoreDataReady, PagingListView.Pagingable {
    private PagingListView mListView;
    private StorePagingAdapter mAdapter;
    private StoreModelManager mStoreModelManager;
    private int mCurrPage;
    private String mFilter;
    private int mRowHeight;
    public ListPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mStoreModelManager = StoreModelManager.getInstance();
        mCurrPage =1;
        mRowHeight =0;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mListView = (PagingListView) findViewById(R.id.paging_list_view);
        mListView.setHasMoreItems(true);
        mListView.setPagingableListener(this);
    }

    @Override
    public void onNoMoreData(int page) {
        mListView.setHasMoreItems(false);
    }

    @Override
    public void onDataReturenedSuccessfully(List<ProductData> productList) {
        if(mFilter !=null && !mFilter.equals("")) {
            productList = filterResults(productList);
        }
        mListView.onFinishLoading(true,productList);
        checkIfNeededMoreItems(productList.size());
    }
    private void checkIfNeededMoreItems(int numOfNewItems) {
        if(mRowHeight == 0 && numOfNewItems > 0) {
            mRowHeight = mAdapter.getItemHeight();
        }
        if(mRowHeight == 0) {
            onLoadMoreItems();
        }else if(mListView.getNumbersOfRowsInScreen(mRowHeight) > mListView.getCount()) {
            onLoadMoreItems();
        }
    }
    private List<ProductData> filterResults(List<ProductData> productList) {
        ArrayList<ProductData> filteredResults = new ArrayList<>();
        for(ProductData product : productList) {
            if(product != null && product.getTitle()!=null &&  product.getTitle().toLowerCase().contains(mFilter)) {
                filteredResults.add(product);
            }
        }
        return filteredResults;
    }
    @Override
    public void onDataFailed() {
        mListView.onFinishLoading(true,null);
    }

    public void filter(String query) {
        mFilter = query.toLowerCase();
        mAdapter = new StorePagingAdapter(getContext(), R.layout.product_layout);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onLoadMoreItems() {
        mStoreModelManager.getPageData(mCurrPage++,this);
    }
}
