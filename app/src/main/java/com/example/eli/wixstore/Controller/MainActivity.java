package com.example.eli.wixstore.Controller;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.eli.wixstore.R;
import com.example.eli.wixstore.View.ListPager;

public class MainActivity extends AppCompatActivity {

    private SearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListPager listPager = (ListPager) findViewById(R.id.list_pager);
        listPager.filter("");
        handleInent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        mSearchView = null;
        if (searchItem != null) {
            mSearchView = (SearchView) searchItem.getActionView();
        }
        if (mSearchView != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleInent(intent);
    }
    private void handleInent(Intent intent) {
        setIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Intent myIntent = new Intent(this, SearchResultActivity.class);
            myIntent.putExtra(SearchResultActivity.EXTRA_QUERY_STR, query); //Optional parameters
            this.startActivity(myIntent);
            if(mSearchView != null) {
                mSearchView.setQuery("", false);
                mSearchView.setIconified(true);
            }
        }
    }
}
