package com.example.kcco.csmap;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;


/**
 * Created by jason on 5/11/15.
 * This is the MapsActivity. Everything that controls what happens on the map would go here.
 */
public class RouteTracker implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {



    // Interface for to do callback from RouteTracker ie updating gps points
    public interface LocationCallBack {
        void handleNewLocation(Location location);
        void plotNewRoute(ArrayList<Double> Lat, ArrayList<Double> Lng);
        void updateRoutePts(Location location);
    }

    private static GoogleApiClient mGoogleApiClient; // Google Services API
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private LocationRequest mLocationRequest; // For GPS
    private Context mContext; //
    private LocationCallBack mLocationCallBack; // Callback

    // These ArrayLists will save the lat and lng of the collected points to be passed into route.java or database
    private ArrayList<Double> trackedLocLat = new ArrayList<>();
    private ArrayList<Double> trackedLocLng = new ArrayList<>();

    public boolean tracking = false;

    public RouteTracker(Context context, LocationCallBack callback) {
        mContext = context;
        mLocationCallBack = callback;
        mGoogleApiClient = new GoogleApiClient.Builder( context )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi(LocationServices.API)
                .build();
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval( 5 * 10000 ) // 10 seconds, in milliseconds
                .setFastestInterval( 1 * 1000 ); // 1 second, in milliseconds
        //.setSmallestDisplacement(3); // minimum 3 meters per update

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if( location == null ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this );
        }
        else {
            mLocationCallBack.handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onLocationChanged(Location location) {
        //mLocationCallBack.handleNewLocation(location);
        //if( tracking == true ) {
            mLocationCallBack.updateRoutePts(location); // keeps updating the points
            Log.d("ok", "some message");
        //}
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Activity activity = (Activity)mContext;
        if( connectionResult.hasResolution() ) {
            try {
                // Start an Activity that tries to resolve error
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST );
            } catch ( IntentSender.SendIntentException e ) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " +
                    connectionResult.getErrorCode() );
        }
    }

    public static void onResume() {
        mGoogleApiClient.connect();
    }

    public void onPause() {
        if(mGoogleApiClient.isConnected()) {
            //LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    // Starts receiving location updates
    public void startGPSTrack() {
        //tracking = true;
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this );
    }

    // Stops receiving real time location updates
    public void stopGPSTrack() {
        //tracking = false;
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        mLocationCallBack.plotNewRoute(trackedLocLat, trackedLocLng);
        trackedLocLat = new ArrayList<>();
        trackedLocLng = new ArrayList<>();
    }

}
