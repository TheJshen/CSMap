package com.example.kcco.csmap;

import android.app.Activity;
import android.content.Context;
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



/**
 * Created by jason on 5/11/15.
 * This is the TrackActivity. Everything that controls what happens on the map would go here.
 */
public class RouteTracker implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {



    // Interface for to do callback from RouteTracker ie updating gps points
    public interface LocationCallBack {
        void handleNewLocation(Location location);
        void updateRoutePts(Location location);
    }

    private static GoogleApiClient mGoogleApiClient; // Google Services API
    private LocationRequest mLocationRequest; // For GPS
    private LocationCallBack mLocationCallBack; // Callback
    private Context mContext;

    public static final String TAG = TrackActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static int POINTS_PER_AVERAGE = 5;

    // These ArrayLists will save the lat and lng of the collected points to be passed into route.java or database
    private double latAvg = 0, lngAvg = 0;
    private Route currentTrackingRoute;

    public boolean tracking = false; // If the button can do the toggling we can remove this boolean
    private int pointUpdateCounter = 0;

    public RouteTracker(Context context, LocationCallBack callback) {
        mContext = context;
        mLocationCallBack = callback;
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(50) //  in milliseconds
                .setFastestInterval(25) // in milliseconds
                .setMaxWaitTime(75);
                //.setSmallestDisplacement(3); // minimum 3 meters per update
        mGoogleApiClient = new GoogleApiClient.Builder( context )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this );

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
        mLocationCallBack.handleNewLocation(location);
        if( tracking == true ) {
            mLocationCallBack.updateRoutePts(location); // keeps updating the points. Used to draw to map
            addNewPointToRoute(location); // Called to store the points into arrays
        }
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

    // Connect on resume
    public static void onResume() {
        mGoogleApiClient.connect();
    }

    public void onPause() {
        /*
        if(mGoogleApiClient.isConnected()) {
            //LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, this);
            //mGoogleApiClient.disconnect(); // Do not disconnect so it will keep tracking the background
        }*/
    }

    // Starts receiving location updates
    public void startGPSTrack() {
        tracking = true;
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this );
        currentTrackingRoute = new Route(); // Create new route when tracking starts
    }

    // Stops receiving real time location updates
    public void stopGPSTrack() {
        tracking = false;
        if(mGoogleApiClient.isConnected()) {
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        /* When finished with tracking add in time and other misc. info. to be stored in route */

    }

    // Takes the points from location updates and add them to a list to be passed into route.
    public void addNewPointToRoute(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        //LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        pointUpdateCounter++;//
        // Calculate the average of 5 points
        if( pointUpdateCounter == POINTS_PER_AVERAGE ) {
            pointUpdateCounter = 0; // resets counter
            latAvg += currentLatitude;
            lngAvg += currentLongitude;
            latAvg /= POINTS_PER_AVERAGE;
            lngAvg /= POINTS_PER_AVERAGE;
            currentTrackingRoute.addToRoute(new LatLng(latAvg, lngAvg));
            latAvg = lngAvg = 0; // reset for next few points
        }
        else {
            latAvg += currentLatitude; // keeps adding the new lat if < POINTS_PER_AVERAGE
            lngAvg += currentLongitude; // same as above but for lng
        }
    }

    // Return route object to be plotted onto map and/or added to database
    public Route returnCompletedRoute() {
        return currentTrackingRoute;
    }
}
