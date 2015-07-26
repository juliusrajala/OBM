package com.herate.jijra.mapexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;


public class MainFragmentActivity extends SingleFragmentActivity {
    private static final String TAG = MainFragmentActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ImageView mLogo;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_name);
        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setDisplayShowTitleEnabled(false);
//        ab.setLogo(R.drawable.textmaponly);
//        ab.setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Fragment createFragment(){
        return new ButtonFragment();
    }

}
