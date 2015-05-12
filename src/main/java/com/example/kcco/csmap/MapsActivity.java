package com.example.kcco.csmap;

import java.util.ArrayList;

import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapsActivity extends FragmentActivity {//implements
            //GoogleApiClient.ConnectionCallbacks,
            //GoogleApiClient.OnConnectionFailedListener,
            //LocationListener {

    public GoogleMap mMap; // Might be null if Google Play services APK is not available.

    // Used for testing the route lines
    private static final LatLng UCSD = new LatLng(32.880114, -117.233981);
    private static final LatLng GEISEL = new LatLng(32.881132, -117.237639);
    private ArrayList<LatLng> route = new ArrayList<>();
    private Route newRoute;

    // Used to set camera position
    private static CameraPosition cameraPosition;

    /*private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;*/

    private static RouteTracker GPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display map
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        // Create the test line from Center to Geisel
        route.add(UCSD);
        route.add(GEISEL);
        newRoute = new Route(route);
        mMap.addPolyline(newRoute.getRoute());

        /*
        // Create new GoogleApiClient to use LocationServices API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY )
                .setInterval( 10 * 10000 ) // 10 seconds, in milliseconds
                .setFastestInterval( 1 * 1000 ); // 1 second, in milliseconds
                */

        GPS = new RouteTracker(this);
    }

    // When app resumes from pause
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded(); // for map
        //mGoogleApiClient.connect(); // for GPS
        RouteTracker.onResume();
    }

    // When the app gets paused
    @Override
    protected void onPause() {
        super.onPause();
        /*if( mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, this );
            mGoogleApiClient.disconnect();
        }*/
        GPS.onPause();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UCSD, 19));
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Sets the camera position to cameraPosition
        cameraPosition = new CameraPosition.Builder()
                .target(UCSD)      // Sets the center of the map to Mountain View
                .zoom(19)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to North
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /*
    // for GPS location
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if( connectionResult.hasResolution() ) {
            try {
                // Start an Activity that tries to resolve error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST );
            } catch ( IntentSender.SendIntentException e ) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " +
                connectionResult.getErrorCode() );
        }
    }

    // for GPS location
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if( location == null ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this );
        }
        else {
            handleNewLocation(location);
        }
    }

    // for GPS location
    @Override
    public void onConnectionSuspended(int i) {
        Log.i( TAG, "Location services suspended. Please reconnect." );
    }

    // for GPS location
    private void handleNewLocation( Location location ) {
        Log.d( TAG, location.toString() );
        double currentLat = location.getLatitude();
        double currentLng = location.getLongitude();
        LatLng currentPos = new LatLng( currentLat, currentLng );
        MarkerOptions options = new MarkerOptions().position(currentPos).title("HERE!");
        mMap.addMarker(options);
    }

    // for GPS location
    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation( location );
    }
    */
}
