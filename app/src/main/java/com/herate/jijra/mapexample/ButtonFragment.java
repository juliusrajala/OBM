package com.herate.jijra.mapexample;


import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by jijra on 17.7.2015.
 *
 *
 **/
public class ButtonFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = ButtonFragment.class.getSimpleName();

    private FloatingActionButton mButton;
    private MapView mMapView;
    private GoogleMap googleMap;

    private ArrayList<SipEvent> mEvents;
    private String mNick;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationManager mLocationManager;
    private Toolbar mToolbar;

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
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mLastLocation = getLocation();
        fixMap();
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(getActivity(), "Nope", Toast.LENGTH_LONG).show();
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
                SipLab.get(getActivity()).addEvent(makeEvent());
                Log.d(TAG, String.valueOf(SipLab.get(getActivity()).getSips().size()));
                populateMap();
                return false;
            }
        });

        return v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();

        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    public void fixMap(){
        mLastLocation = getLocation();
        if(mLastLocation != null){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 12.0f));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .tilt(60.0f)
                    .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .zoom(15)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public Location getLocation(){
        Criteria criteria = new Criteria();
        Location lastLocation;
        lastLocation = mLocationManager.getLastKnownLocation(mLocationManager.getBestProvider(criteria, false));
        return lastLocation;
    }

    public void populateMap(){
        mEvents = SipLab.get(getActivity()).getSips();
        Log.d(TAG, "Populating map with markers, there's: " + mEvents.size());

        Collections.sort(mEvents);
        if(mEvents.size() == 0)
            return;
        try{
            for(int i=0; i<20; i++){
                MarkerOptions marker = new MarkerOptions()
                        .position(mEvents.get(i).getLatLng())
                        .title(mEvents.get(i).getNick())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
                googleMap.addMarker(marker);

            }
        }catch(IndexOutOfBoundsException e){
            Log.e(TAG,"Index out of bounds", e);
            for(SipEvent a : mEvents){
                MarkerOptions marker = new MarkerOptions()
                        .position(a.getLatLng())
                        .title(a.getNick())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
                googleMap.addMarker(marker);
            }
        }
        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception a){
            Log.e(TAG, "Error thrown", a);
        }

        if(mMapView == null){
            Log.d(TAG, "mapView is null");
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mMapView.invalidate();
                fixMap();
            }
        });
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }



    @Override
    public void onConnected(Bundle connectionHint){
        Log.d(TAG, "Connected with location");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
        mMapView.invalidate();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
        populateMap();
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "OnPause called");
        SipLab.get(getActivity()).saveSipEvents();
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

    public SipEvent makeEvent(){
        SipEvent sipEvent = new SipEvent("Julius", new Date(), getLocation().getLatitude(), getLocation().getLongitude());
        return sipEvent;
    }

    public static ButtonFragment newInstance(){
        return new ButtonFragment();
    }
}
