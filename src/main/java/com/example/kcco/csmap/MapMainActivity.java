package com.example.kcco.csmap;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kcco.csmap.Misc.FlyOutContainer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;


public class MapMainActivity extends FragmentActivity implements RouteTracker.LocationCallBack {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    FlyOutContainer root;


    // For averaging points
    private int pointUpdateCounter = 0;
    private double latAvg=0, lngAvg=0;
    // Route object
    private Route routeToDisplay;
    private Polyline currentDisplayed; // Polyline displayed on the map
    
    // Used for testing the route lines
    private static final LatLng UCSD = new LatLng(32.880114, -117.233981);
    private static final LatLng GEISEL = new LatLng(32.881132, -117.237639);
    private static final LatLng RIMAC = new LatLng(32.887298, -117.239616);

    // Used to set camera position
    private static CameraPosition cameraPosition;

    private static RouteTracker GPS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_track);
        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.activity_map_main, null);
        setContentView(root);
        setUpMapIfNeeded();



        GPS = new RouteTracker(this, this);

        // Set up building markers
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent nextScreen = new Intent(MapMainActivity.this, RoomAvailOptionsActivity.class);
                nextScreen.putExtra("BuildingName", marker.getTitle());
                startActivity(nextScreen);
            }
        });

        for( MapsConstants.MarkerDetails building : MapsConstants.allBuildings ) {
            mMap.addMarker(new MarkerOptions()
                            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_gary))
                            .position(building.getPosition())
                            .title(building.getTitle())
                            .snippet(building.getSnippet())
            );
        }


    }

/////////////////////////////////Overriding Activity Functions//////////////////////////////////////
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

/////////////////////////////////Overriding LocationCallBack Functions//////////////////////////////

    @Override
    public void handleNewLocation(Location location) {

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        //route.add(latLng); // Save the first point
        routeToDisplay = GPS.returnCompletedRoute();
        /*cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude() ))      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to North
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        */
        /*MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("You are here!");

        mMap.addMarker(options);*/
    }

    @Override
    public void plotNewRoute(ArrayList<Double> Lat, ArrayList<Double> Lng) {
        /*
        for(int i = 0; i < Lat.size(); ++i) {
            route.add(new LatLng(Lat.get(i), Lng.get(i)));
        }
        newRoute = new Route(route);
        mMap.addPolyline(newRoute.drawRoute());
        */
    }

    public void plottingRecommendations(ArrayList<Integer> inputIDs)
    {
        ArrayList<Route> bestRoutes= RouteProcessing.getBestRoutes(inputIDs, this);
        for( int index = 0 ; index < bestRoutes.size() ; index++)
        {
            mMap.addPolyline(bestRoutes.get(index).drawRoute());
        }
    }


    // This method updates the route plot on the map as the GPS is tracking a new route
    // currentDisplayed is the Polyline displayed on the mapsActivity
    @Override
    public void updateRoutePts(Location location) {
        if(currentDisplayed == null) {
            currentDisplayed = mMap.addPolyline(routeToDisplay.drawRoute());
        } else {
            currentDisplayed.setPoints(routeToDisplay.getLatLngArray());
        }
    }


    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        cameraPosition = new CameraPosition.Builder()
                .target(UCSD)      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to North
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


/////////////////////////////Component functions//////////////////////////////////////////////////

    public void toggleMenu(View view) {
        this.root.toogleMenu();
    }

    public void goToTrackActivity(View view){
        toggleMenu(view);
        Intent intent = new Intent(MapMainActivity.this, TrackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MapMainActivity.this.startActivity(intent);
    }

    public void goToRouteActivity(View view){
        toggleMenu(view);
        Intent intent = new Intent(MapMainActivity.this, RouteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MapMainActivity.this.startActivity(intent);

    }

    public void goToLoginActivity(){
        Intent intent = new Intent(MapMainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MapMainActivity.this.startActivity(intent);
    }

    public void logout(View view){
        toggleMenu(view);
        if (UserDAO.isUserActive()){
            Toast.makeText(MapMainActivity.this, "You have been logged out.", Toast.LENGTH_LONG).show();
            Log.d("TrackActivity", "User " + Integer.toString(UserDAO.getCurrentUserId()) + " Logout");
            UserDAO.logOut(MapMainActivity.this);
        }
        else {
            UserDAO.logOut(MapMainActivity.this);
            Log.d("TrackActivity", "User " + Integer.toString(UserDAO.getCurrentUserId()) + " should not show up");
        }
    }


}