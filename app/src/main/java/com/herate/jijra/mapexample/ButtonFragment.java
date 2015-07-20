package com.herate.jijra.mapexample;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by jijra on 17.7.2015.
 *
 *
 **/
public class ButtonFragment extends Fragment implements FragmentLifecycle {
    private static final String TAG = ButtonFragment.class.getSimpleName();

    private FloatingActionButton mButton;
    private MapView mMapView;
    private GoogleMap googleMap;
    private TextView appName;

    private ArrayList<ClugEvent> mEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_button, parent, false);
        mMapView = (MapView)v.findViewById(R.id.mapCard);
        mMapView.onCreate(savedInstanceState);

        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e){
            Log.e(TAG, "Error thrown", e);
        }

        googleMap = mMapView.getMap();

        float latitude = 60.450692f;
        float longitude =  22.278664f;

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(getActivity(),"Nope", Toast.LENGTH_LONG).show();
            }
        });

        mButton = (FloatingActionButton)v.findViewById(R.id.add_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Hold button to register drink",Toast.LENGTH_SHORT).show();
            }
        });


        mButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "New marker added.", Toast.LENGTH_SHORT).show();
                ClugEvent clugEvent = new ClugEvent();
                ClugLab.get(getActivity()).addEvent(clugEvent);
                Log.d(TAG, String.valueOf(ClugLab.get(getActivity()).getClugs().size()));
                populateMap();
                return false;
            }
        });

        return v;
    }

    public void populateMap(){
        mEvents = ClugLab.get(getActivity()).getClugs();
        Log.d(TAG, "Populating map with markers, there's: " + mEvents.size());

        for(ClugEvent e : mEvents){
            MarkerOptions marker = new MarkerOptions()
                    .position(e.getLatLng())
                    .title(e.getNick())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
            googleMap.addMarker(marker);
            try{
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception a){
                Log.e(TAG, "Error thrown", a);
            }

        }


        if(mMapView == null){
            Log.d(TAG, "mapView is null");
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMapView.invalidate();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        populateMap();
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "OnPause called");
        mMapView.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mMapView != null)
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
        if(mMapView != null)
            mMapView.onLowMemory();
    }

    public ClugEvent makeEvent(){
        ClugEvent clugEvent = new ClugEvent();
        //TODO: Make getters get actual information here
        return clugEvent;
    }

    @Override
    public void onPauseFragment(){
        Log.i(TAG, "onPauseFragment()");
    }

    @Override
    public void onResumeFragment(){
        Log.i(TAG, "onResumeFragment()");
    }


    public static ButtonFragment newInstance(){
        return new ButtonFragment();
    }
}
