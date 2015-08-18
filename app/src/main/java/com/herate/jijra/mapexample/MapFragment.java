package com.herate.jijra.mapexample;

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
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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
import com.herate.jijra.mapexample.models.SipEvent;


public class MapFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = MapFragment.class.getSimpleName();

    private GoogleMap googleMap;
    private MapView mapView;
    private Typeface font;
    private ClusterManager<EventItem> mClusterManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationManager mLocationManager;
    private FloatingActionButton mButton;


    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
    }
    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_fullmap,container, false);
        mapView = (MapView)v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        final Firebase myFirebaseRef = new Firebase("https://sipmap.firebaseio.com/");
        final Firebase eventRef = myFirebaseRef.child("events");


        font = Typeface.createFromAsset(
                getActivity().getAssets(),
                "Roboto-Thin.ttf");
        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e){
            Log.e(TAG, "Error thrown", e);
        }

        googleMap = mapView.getMap();
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

        eventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "event added:" + dataSnapshot.getValue());

                SipEvent sipEvent = dataSnapshot.getValue(SipEvent.class);
//                addEventOnMap(sipEvent);
                EventItem c = new EventItem(sipEvent);
                mClusterManager.addItem(c);
                mapView.invalidate();
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
        mapView.invalidate();
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

    private void setUpClusterer(){
        mClusterManager = new ClusterManager<EventItem>(getActivity(), googleMap);
        mClusterManager.setRenderer(new SipClusterRenderer());
        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
    }

    public Location getLocation(){
        Criteria criteria = new Criteria();
        Location lastLocation;
        lastLocation = mLocationManager.getLastKnownLocation(mLocationManager.getBestProvider(criteria, false));
        return lastLocation;
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
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.sipmap_icon)).title(item.nick);
        }
    }


}
