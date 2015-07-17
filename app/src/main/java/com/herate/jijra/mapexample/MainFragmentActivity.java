package com.herate.jijra.mapexample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainFragmentActivity extends FragmentActivity {
    private static final String TAG = MainFragmentActivity.class.getSimpleName();

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Starting onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(new MapPagerAdapter(getSupportFragmentManager()));
    }

    private class MapPagerAdapter extends FragmentPagerAdapter{

        public MapPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int pos){
            switch(pos){
                case 1:
                    Log.d(TAG, "Changing to MapFragment");
                    return BeerMapFragment.newInstance();
                case 0:
                    Log.d(TAG, "Changing to ButtonFragment");
                    return ButtonFragment.newInstance();
                default:
                    return ButtonFragment.newInstance();
            }
        }

        @Override
        public int getCount(){
            return 2;
        }
    }

}
