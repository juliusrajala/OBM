package com.herate.jijra.mapexample;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.herate.jijra.mapexample.models.EventItem;

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

    private String mNick;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationManager mLocationManager;
    private TextView mQuote;
    private Typeface font;
    private ClusterManager<EventItem> mClusterManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_button, parent, false);
        mMapView = (MapView)v.findViewById(R.id.mapCard);
        mMapView.onCreate(savedInstanceState);
        mQuote = (TextView)v.findViewById(R.id.quote);
        final Firebase myFirebaseRef = new Firebase("https://sipmap.firebaseio.com/");

        font = Typeface.createFromAsset(
                getActivity().getAssets(),
                "Roboto-Thin.ttf");
        mQuote.setTypeface(font);
        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e){
            Log.e(TAG, "Error thrown", e);
        }

        googleMap = mMapView.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        setUpClusterer();
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
                Toast.makeText(getActivity(), "Hold button to register drink", Toast.LENGTH_SHORT).show();
            }
        });

        final Firebase eventRef = myFirebaseRef.child("events");


        mButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "New marker added.", Toast.LENGTH_SHORT).show();
                SipEvent e = makeEvent();
                Firebase newPostRef = eventRef.push();
//                .child(mNick+"_"+e.getDate().getTime());
                newPostRef.setValue(e);
                Log.d(TAG, newPostRef.getKey());
//                SipLab.get(getActivity()).addEvent(e);
//                populateMap();
                return false;
            }
        });

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, " There are: " + dataSnapshot.getChildrenCount() + " children");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, "The read failed.");
            }
        });

        eventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "event added:" + dataSnapshot.getValue());

                SipEvent sipEvent = dataSnapshot.getValue(SipEvent.class);
//                addEventOnMap(sipEvent);
                EventItem c = new EventItem(sipEvent);
                mClusterManager.addItem(c);
                mMapView.invalidate();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        return v;
    }

    private void setUpClusterer(){
        mClusterManager = new ClusterManager<EventItem>(getActivity(), googleMap);
        mClusterManager.setRenderer(new SipClusterRenderer());
        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
    }



    public void fixMap(){
        mLastLocation = getLocation();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    public void addEventOnMap(SipEvent event){
        final MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(event.getLatitude(), event.getLongitude()))
                .title(event.getNick())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));

        googleMap.addMarker(marker);


    }

    public SipEvent makeEvent(){
        SipEvent sipEvent = new SipEvent(mNick, new Date(), getLocation().getLatitude(), getLocation().getLongitude());
        return sipEvent;
    }

    public Location getLocation(){
        Criteria criteria = new Criteria();
        Location lastLocation;
        lastLocation = mLocationManager.getLastKnownLocation(mLocationManager.getBestProvider(criteria, false));
        return lastLocation;
    }

    public void populateMap(){
//        mEvents = SipLab.get(getActivity()).getSips();
//        Log.d(TAG, "Populating map with markers, there's: " + mEvents.size());
//
//        Collections.sort(mEvents);
//        if(mEvents.size() == 0)
//            return;
//        try{
//            for(int i=0; i<20; i++){
//                MarkerOptions marker = new MarkerOptions()
//                        .position(mEvents.get(i).getLatLng())
//                        .title(mEvents.get(i).getNick())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
//                googleMap.addMarker(marker);
//
//            }
//        }catch(IndexOutOfBoundsException e){
//            Log.e(TAG,"Index out of bounds", e);
//            for(SipEvent a : mEvents){
//                MarkerOptions marker = new MarkerOptions()
//                        .position(a.getLatLng())
//                        .title(a.getNick())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
//                googleMap.addMarker(marker);
//            }
//        }
//        try{
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception a){
//            Log.e(TAG, "Error thrown", a);
//        }
//
        if(mMapView == null){
            Log.d(TAG, "mapView is null");
            return;
        }
//
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
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        mNick = "Julius";
        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
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

    public static ButtonFragment newInstance(){
        return new ButtonFragment();
    }

    private class SipClusterRenderer extends DefaultClusterRenderer<EventItem> {
        private final IconGenerator mIconGenerator = new IconGenerator(getActivity().getApplicationContext());
        private final ImageView mImageView;
        private final int mDimension;

        public SipClusterRenderer(){
            super(getActivity().getApplicationContext(), googleMap, mClusterManager);
            mImageView = new ImageView(getActivity().getApplicationContext());
            mDimension = 100;
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            mIconGenerator.setContentView(mImageView);
        }
        @Override
        public void onBeforeClusterItemRendered(EventItem item, MarkerOptions markerOptions){
            mImageView.setImageResource(item.imageResource);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)).title(item.nick);
        }
    }

}
