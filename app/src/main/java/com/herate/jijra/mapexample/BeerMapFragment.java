package com.herate.jijra.mapexample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by jijra on 17.7.2015.
 **/
public class BeerMapFragment extends Fragment implements FragmentLifecycle {
    private static final String TAG = BeerMapFragment.class.getSimpleName();

    private MapView mMapView;
    private GoogleMap googleMap;

    private ArrayList<ClugEvent> mEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_map, parent, false);

        mMapView = (MapView)v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e){
            Log.e(TAG, "Error thrown", e);
        }

        googleMap = mMapView.getMap();

        float latitude = 60.450692f;
        float longitude =  22.278664f;

        MarkerOptions marker = new MarkerOptions().position( new LatLng(latitude, longitude)).title("Turku");

        googleMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        mEvents = ClugLab.get(getActivity()).getClugs();


        return v;
    }

    /*
    * TODO: Implement map population as a runnable and see if that helps. Current mode doesn't work properly.
    *
    * */

    public void populateMap(){
        Log.d(TAG, "Populating map with markers.");
        if(mEvents != null){
            for(ClugEvent e : mEvents){
                MarkerOptions marker = new MarkerOptions()
                        .position(e.getLatLng())
                        .title(e.getNick())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
                googleMap.addMarker(marker);
            }
        }
    }

    @Override
    public void onPauseFragment(){
        Log.i(TAG, "onPauseFragment()");
    }

    @Override
    public void onResumeFragment(){
        Log.i(TAG, "onResumeFragment()");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "OnPause called");
        mMapView.onPause();
    }


    //TODO: Include map drawing inside here.
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "OnResume called");
        populateMap();
        mMapView.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Log.d(TAG, "OnAttach called");
        populateMap();
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public static BeerMapFragment newInstance(){
        return new BeerMapFragment();
    }
}
