package com.herate.jijra.mapexample;

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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jijra on 17.7.2015.
 **/
public class BeerMapFragment extends Fragment {
    private static final String TAG = BeerMapFragment.class.getSimpleName();

    private MapView mMapView;
    private GoogleMap googleMap;

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

        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
        mMapView.onPause();
    }

    //TODO: Include map drawing inside here.
    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mMapView.onDestroy();
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
