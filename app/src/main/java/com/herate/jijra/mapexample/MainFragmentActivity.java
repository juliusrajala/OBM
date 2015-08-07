package com.herate.jijra.mapexample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.herate.jijra.mapexample.models.DrawerItem;
import com.herate.jijra.mapexample.models.NavDrawerAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainFragmentActivity extends SingleFragmentActivity {
    private static final String TAG = MainFragmentActivity.class.getSimpleName();

    private static final int BUTTON_FRAGMENT = 1;
    private static final int SETTINGS_FRAGMENT = 2;
    private int currentFragment = 1;

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private RecyclerView mRecyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<DrawerItem> dataList;
    private ImageView mLogo;
    private NavDrawerAdapter mAdapter;
    private LinearLayoutManager mManager;
    private Typeface font;
    public Firebase myFirebaseRef;

    public String HEADER_NAME = "Julius Rajala";
    public int HEADER_IMAGE = R.drawable.profilepix;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Database stuff
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://sipmap.firebaseio.com/");

        setContentView(R.layout.fragment_activity);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        dataList = new ArrayList<>();
        addItemsToDataList();
        font = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");

        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mRecyclerView = (RecyclerView)findViewById(R.id.RecyclerView);
        mAdapter = new NavDrawerAdapter(dataList, this, HEADER_NAME, HEADER_IMAGE, font);
        mManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_name);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        ab.setLogo(R.mipmap.ic_logotext);
        ab.setDisplayUseLogoEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
            }
        };

        final GestureDetector mGestureDetector =
                new GestureDetector(MainFragmentActivity.this, new GestureDetector.SimpleOnGestureListener(){
                    @Override
                public boolean onSingleTapUp(MotionEvent e){
                        return true;
                    }
                });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(){
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent){
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(child != null && mGestureDetector.onTouchEvent(motionEvent)){
                    mDrawer.closeDrawers();
                    onTouchDrawer(recyclerView.getChildLayoutPosition(child));
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e){

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        onTouchDrawer(currentFragment);
    }

    public void openFragment(final Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    private void onTouchDrawer(final int position){
        //TODO: Make more fragments, add them here
        currentFragment = position;
        switch (position){
            case BUTTON_FRAGMENT:
                openFragment(new ButtonFragment());
                break;
            case SETTINGS_FRAGMENT:
                break;
            default:
                return;
        }
    }

    private void addItemsToDataList(){
        dataList.add(new DrawerItem(getString(R.string.home), R.mipmap.drawer_home));
        dataList.add(new DrawerItem(getString(R.string.map), R.mipmap.drawer_map));
        dataList.add(new DrawerItem(getString(R.string.settings), R.mipmap.drawer_settings));
        dataList.add(new DrawerItem(getString(R.string.data), R.mipmap.drawer_data));
        dataList.add(new DrawerItem(getString(R.string.about), R.mipmap.drawer_about));
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
