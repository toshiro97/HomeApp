package com.toshiro97.homeapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;


public class EaseActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<AppEntry>> {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease);

        getSupportLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<List<AppEntry>> onCreateLoader(
            int arg0, Bundle arg1) {
        // TODO Auto-generated method stub
        return new AppLoader(this);
    }

    private ViewPager pager;
    private MyPagerAdapter pagerAdapter;

    @Override
    public void onLoadFinished(Loader<List<AppEntry>> loader,
                               List<AppEntry> appEntries) {
        // TODO Auto-generated method stub


        Iterator<AppEntry> iterator = appEntries.iterator();
        List<GridFragment> gridFragments = new ArrayList<GridFragment>();

        pager = (ViewPager) findViewById(R.id.pager);

        int i = 0;
        AppEntry entry;
        while (iterator.hasNext()) {
            ArrayList<GridItems> gridItems = new ArrayList<GridItems>();
            GridItems item;
            for (i = 0; i < 16; i++) {
                if (iterator.hasNext()) {
                    entry = iterator.next();
                    item = new GridItems(i, entry.getLabel(), entry.getIcon(), entry.getApplicationInfo());
                    gridItems.add(item);

                }
            }

            GridItems[] gp = {};
            GridItems[] gridPage = gridItems.toArray(gp);
            gridFragments.add(new GridFragment(gridPage, EaseActivity.this));
        }
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), gridFragments);
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<AppEntry>> arg0) {
        // TODO Auto-generated method stub

    }
}
