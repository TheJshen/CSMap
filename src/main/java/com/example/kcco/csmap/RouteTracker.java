package com.example.kcco.csmap;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by jason on 5/11/15.
 */
public class RouteTracker implements
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            LocationListener {

    private static MapsActivity mapsActivity;
    private static GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest; // For GPS

    public RouteTracker( MapsActivity main ) {
        mapsActivity = main;
        mGoogleApiClient = new GoogleApiClient.Builder( mapsActivity )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi(LocationServices.API)
                .build();
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval( 10 * 10000 ) // 10 seconds, in milliseconds
                .setFastestInterval( 1 * 1000 ); // 1 second, in milliseconds
    }

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

    @Override
    public void onConnectionSuspended(int i) {
        Log.i( TAG, "Location services suspended. Please reconnect." );
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if( connectionResult.hasResolution() ) {
            try {
                // Start an Activity that tries to resolve error
                connectionResult.startResolutionForResult(mapsActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST );
            } catch ( IntentSender.SendIntentException e ) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " +
                    connectionResult.getErrorCode() );
        }
    }

    private void handleNewLocation( Location location ) {
        Log.d( TAG, location.toString() );
        double currentLat = location.getLatitude();
        double currentLng = location.getLongitude();
        LatLng currentPos = new LatLng( currentLat, currentLng );
        MarkerOptions options = new MarkerOptions().position(currentPos).title("HERE!");
        mapsActivity.mMap.addMarker(options);
    }

    public static void onResume() {
        mGoogleApiClient.connect();
    }

    public void onPause() {
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
}
