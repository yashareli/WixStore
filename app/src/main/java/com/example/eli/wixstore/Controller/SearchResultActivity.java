package com.example.eli.wixstore.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.eli.wixstore.R;
import com.example.eli.wixstore.View.ListPager;

/**
 * Created by Eli on 14/10/2016.
 */

public class SearchResultActivity extends AppCompatActivity {
    public static final String EXTRA_QUERY_STR = "EXTRA_QUERY_STR";

    private ListPager mListPager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIntent(getIntent());
        setContentView(R.layout.activity_main);
        mListPager = (ListPager) findViewById(R.id.list_pager);
        String query = getIntent().getExtras().getString(EXTRA_QUERY_STR);
        mListPager.filter(query);
        getSupportActionBar().setTitle(query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
