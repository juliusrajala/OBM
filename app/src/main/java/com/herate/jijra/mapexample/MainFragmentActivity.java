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
import android.view.View;

import com.google.android.gms.games.internal.GamesContract;
import com.google.android.gms.maps.MapView;


public class MainFragmentActivity extends FragmentActivity {
    private static final String TAG = MainFragmentActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private MapPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Starting onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mAdapter = new MapPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mAdapter.notifyDataSetChanged();
                Fragment fr = mAdapter.getItem(position);
                if(fr instanceof BeerMapFragment){
                        fr.isResumed();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    private class MapPagerAdapter extends FragmentPagerAdapter{

        public MapPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int pos){
            switch(pos){
                case 0:
                    Log.d(TAG, "Changing to ButtonFragment");
                    return ButtonFragment.newInstance();
                case 1:
                    Log.d(TAG, "Changing to MapFragment");
                    return BeerMapFragment.newInstance();
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
