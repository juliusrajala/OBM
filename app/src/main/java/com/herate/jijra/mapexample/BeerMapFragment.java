package com.herate.jijra.mapexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jijra on 17.7.2015.
 */
public class BeerMapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = new View(getActivity());//TODO: Replace with something sensible
        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    //TODO: Include map drawing inside here.
    @Override
    public void onResume(){
        super.onResume();
    }


    public static BeerMapFragment newInstance(){
        return new BeerMapFragment();
    }
}
