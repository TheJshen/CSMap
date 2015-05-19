package com.example.kcco.csmap;

import java.util.ArrayList;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;


/**TODO: Need to get the map to follow the user
 * TODO: Get the navigation layer to show the users location currently
 * TODO: Have a route be able to inputted into the data base
 */



public class MapsActivity extends FragmentActivity implements RouteTracker.LocationCallBack {

    private static final int POINTS_PER_AVERAGE = 5;
    public GoogleMap mMap; // Might be null if Google Play services APK is not available.

    // Used for testing the route lines
    private static final LatLng UCSD = new LatLng(32.880114, -117.233981);
    private static final LatLng GEISEL = new LatLng(32.881132, -117.237639);
    private static final LatLng RIMAC = new LatLng(32.887298, -117.239616);

    // Used for testing to create route based on ArrayList
    private ArrayList<LatLng> route = new ArrayList<>();

    // For averaging points
    private int pointUpdateCounter = 0;
    private double latAvg=0, lngAvg=0;
    // Route object
    private Route newRoute;
    private Polyline currentDisplayed;

    // Used to set camera position
    private static CameraPosition cameraPosition;

    private static RouteTracker GPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display map
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        // Button used to test tracking
        final Button button = (Button) findViewById(R.id.btnSurrey);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(GPS.tracking == false)
                    GPS.startGPSTrack();
                else
                    GPS.stopGPSTrack();
            }
        });

        GPS = new RouteTracker(this, this);

        // Set up building markers
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.v("Listener", "Tapped the " + marker.getTitle() + " info window Gary");

                Intent nextScreen = new Intent(MapsActivity.this, ClassroomInfoActivity.class);
                nextScreen.putExtra("BuildingName", marker.getTitle());
                startActivity(nextScreen);
            }
        });

        for( MapsConstants.MarkerDetails building : MapsConstants.allBuildings ) {
            mMap.addMarker(new MarkerOptions()
                            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.gary_pointer))
                            .position(building.getPosition())
                            .title(building.getTitle())
                            .snippet(building.getSnippet())
            );
        }

    }

    // When app resumes from pause
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded(); // for map
        RouteTracker.onResume();
    }

    // When the app gets paused
    @Override
    protected void onPause() {
        super.onPause();
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
                mMap.setMyLocationEnabled(true);
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
            /*TODORouteTracker tmep = new RouteTracker(this, this);
            Location myLocation =  tmep.getPreviousLocation();
            LatLng firstLoc = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            */
        //LatLng first = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        // Sets the camera position to cameraPosition
        cameraPosition = new CameraPosition.Builder()
                .target(UCSD)      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to North
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void handleNewLocation(Location location) {

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        route.add(latLng); // Save the first point
        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude() ))      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to North
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        /*MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("You are here!");

        mMap.addMarker(options);*/
    }

    @Override
    public void plotNewRoute(ArrayList<Double> Lat, ArrayList<Double> Lng) {
        for(int i = 0; i < Lat.size(); ++i) {
            route.add(new LatLng(Lat.get(i), Lng.get(i)));
        }
        newRoute = new Route(route);
        mMap.addPolyline(newRoute.drawRoute());
    }

    public void plottingRecommendations(ArrayList<Integer> inputIDs)
    {
        ArrayList<Route> bestRoutes= RouteProcessing.getBestRoutes(inputIDs, this);
        for( int index = 0 ; index < bestRoutes.size() ; index++)
        {
            mMap.addPolyline(bestRoutes.get(index).drawRoute());
        }
    }


    @Override
    public void updateRoutePts(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        //LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        pointUpdateCounter++;//
        // Calculate the average of 5 points
        if( pointUpdateCounter == POINTS_PER_AVERAGE ) {
            latAvg += currentLatitude;
            lngAvg += currentLongitude;
            latAvg /= POINTS_PER_AVERAGE;
            lngAvg /= POINTS_PER_AVERAGE;
            pointUpdateCounter = 0; // resets counter
            LatLng latLng = new LatLng(latAvg, lngAvg);
            if(currentDisplayed != null) {
                newRoute.addToRoute(currentDisplayed, latLng);
            } else {
                route.add(latLng);
                newRoute = new Route(route);
                currentDisplayed = mMap.addPolyline(newRoute.drawRoute());
            }
            latAvg = lngAvg = 0;
        }
        else {
            latAvg += currentLatitude;
            lngAvg += currentLongitude;
        }
    }
}