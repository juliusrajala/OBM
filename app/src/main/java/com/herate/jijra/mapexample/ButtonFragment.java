package com.herate.jijra.mapexample;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Turku");

        googleMap.addMarker(marker);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

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
                return false;
            }
        });

        return v;
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
