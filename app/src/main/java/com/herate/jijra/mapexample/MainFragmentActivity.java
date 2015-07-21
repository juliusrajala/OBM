package com.herate.jijra.mapexample;

import android.support.v4.app.Fragment;


public class MainFragmentActivity extends SingleFragmentActivity {
    private static final String TAG = MainFragmentActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment(){
        return new ButtonFragment();
    }

}
