package com.example.kcco.csmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kcco.csmap.DAO.BuildingDAO;
import com.example.kcco.csmap.DAO.RoutesDAO;
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


/**TODO: Need to get the map to follow the user
 * TODO: Get the navigation layer to show the users location currently
 */



public class TrackActivity extends FragmentActivity implements RouteTracker.LocationCallBack {

    //Constant for distance
    private static final double SEARCH_DISTANCE = 0.01; //in miles
    private static final double BUILDING_DISTANCE = 0.01; //in miles

    //popupPrompt Input
    private String promptInput = "";
    private boolean getPrompt = false;

    private static final int POINTS_PER_AVERAGE = 5;
    public GoogleMap mMap; // Might be null if Google Play services APK is not available.

    // Used for testing the route lines
    private static final LatLng UCSD = new LatLng(32.880114, -117.233981);
    private static final LatLng GEISEL = new LatLng(32.881132, -117.237639);
    private static final LatLng RIMAC = new LatLng(32.887298, -117.239616);

    // Used for testing to create route based on ArrayList
    //private ArrayList<LatLng> route = new ArrayList<>();

    // For averaging points
    private int pointUpdateCounter = 0;
    private double latAvg=0, lngAvg=0;
    // Route object
    private Route routeToDisplay;
    private Polyline currentDisplayed; // Polyline displayed on the map

    // Used to set camera position
    private static CameraPosition cameraPosition;

    private static RouteTracker GPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display map
        setContentView(R.layout.activity_track);
        setUpMapIfNeeded();

        GPS = new RouteTracker(this, this);

        // Set up building markers
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent nextScreen = new Intent(TrackActivity.this, RoomAvailOptionsActivity.class);
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

/////////////////////////////LocationCallBack Functions ///////////////////////////////////////
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

    // This method is used to center on current location
    public void dropPinAndCenterCamera(LatLng pointToCenterOn) {
        cameraPosition = new CameraPosition.Builder()
                .target(pointToCenterOn)      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to North
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

/////////////////////////Component Functions ///////////////////////////////////////
    /*  Button function track
     *  Button name: btnSurrey
     *  Describe: Begin to track the route.
     */
    public void track(View view){
        if(GPS.tracking == false) {// using the instance variable tracking to keep track of button
            GPS.startGPSTrack();
            ((Button)view).setText("Stop");
            if(currentDisplayed != null) {
                // Removes the current displayed polyline when starting to track again
                currentDisplayed.remove();
                currentDisplayed = null; // get rid of currentDispalyed
            }
        }
        else {
            GPS.stopGPSTrack();
            ((Button)view).setText("Track");
            Route thisRoute = GPS.returnCompletedRoute();
            RoutesDAO routeInfo = new RoutesDAO(TrackActivity.this);
            RoutesDAO subRoute = new RoutesDAO(TrackActivity.this);

            //local variables for route information
            int startLocId, endLocId;
            int transport, timeSpent;
            int userId = UserDAO.getCurrentUserId();
            ArrayList<LatLng> latLngRoute = thisRoute.getLatLngArray();

            //TODO: testing popupPrompt, not working as expected, need further research.
            TrackActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    popupPrompt("Testing", "input");
                }
            });
            Log.d("TrackActivity", "Got promptInput: " + promptInput);

            //search if the start location of thisRoute is existed
            // Makes sure that there is at least two points in the route
            if( latLngRoute.size() > 1 ) {

                //TODO: This section assumes it generate info from front end
                startLocId = verifyExistedPlace(latLngRoute.get(0), "Start Location");
                endLocId = verifyExistedPlace(latLngRoute.get(latLngRoute.size()-1), "End Location");
                transport = 8;
                timeSpent = 9382; // in second

                int routeId = routeInfo.createRoute(startLocId, endLocId, userId, transport, timeSpent);
                routeInfo.sendRouteInfo();
                subRoute.createSubRoute(routeId, latLngRoute);
                subRoute.sendSubRouteInfo();
            }

        }
    }

    private int verifyExistedPlace(LatLng point, String promptTitle){
        String placeName;
        int placeId;
        int userId = UserDAO.getCurrentUserId();

        //search if the given location point is existed in database.
        BuildingDAO existedPlace = BuildingDAO.searchBuilding(point,
                SEARCH_DISTANCE, TrackActivity.this);

        //the given location point did not exist in database
        if (existedPlace == null) {
            //TODO: the placeName should be the string generate from the front end.
            popupPrompt(promptTitle, "Place");
            placeName = "Somewhere";
            existedPlace = new BuildingDAO(TrackActivity.this);
            existedPlace.createBuilding(placeName, userId, point, BUILDING_DISTANCE);
            placeId = existedPlace.getPlaceId();
            existedPlace.sendBuildingInfo();
        }
        //the given location point did exist in database
        else {
            placeName = existedPlace.getName();
            //TODO: Assume front end prompt current place name, and then user change it.
            placeName = "Somewhere2";
            placeId = existedPlace.getPlaceId();
            existedPlace.setName(placeName);
            existedPlace.sendBuildingInfo();
        }
        return placeId;
    }



    //TODO: Not completed, need to figure way to get String value after click Okay. Something to deal with Thread.
    public void popupPrompt(String promptTitle, String place){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(promptTitle);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        input.setText(place);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public synchronized void onClick(DialogInterface dialog, int which) {
                promptInput = input.getText().toString();
                getPrompt = true;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }


    /*  Button function goToMainActivity
     *  Button name: btnMain
     *  Describe: Direct User to MapMainActivity
     */
    public void goToMainActivity(View view){
        Intent intent = new Intent(TrackActivity.this, MapMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        TrackActivity.this.startActivity(intent);
    }
}