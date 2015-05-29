package com.example.kcco.csmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kcco.csmap.DAO.BuildingDAO;
import com.example.kcco.csmap.DAO.Messenger;
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

    // User to store all building markers
    private ArrayList<Marker> allMarkers = new ArrayList<Marker>();
    private ArrayList<Pair<Marker, BuildingDAO>> locations = new ArrayList<Pair<Marker, BuildingDAO>>();

    //timer
    private Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_track);
        setContentView(R.layout.activity_map_main);
        setUpMapIfNeeded();


        timer = (Chronometer)this.findViewById(R.id.chronometer);
        timer.setVisibility(View.GONE);
        GPS = new RouteTracker(this, this);

        // Set up building markers
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                boolean isGaryMaker = false;
                for (int i = 0; i < allMarkers.size(); i++) {
                    if (allMarkers.get(i).getId().equals(marker.getId())) {
                        isGaryMaker = true;
                        break;
                    }
                }
                if (isGaryMaker) {
                    Intent nextScreen = new Intent(MapMainActivity.this, RoomAvailOptionsActivity.class);
                    nextScreen.putExtra("BuildingName", marker.getTitle());
                    startActivity(nextScreen);
                } else {
                    //TODO: after marker is clicked.
                    for (int i = 0; i < locations.size(); i++) {
                        //Compare saved Marker in location and current clicked Marker
                        if (locations.get(i).first.getId().equals(marker.getId())) {
                            LatLng currentLocation = new LatLng(32.881132, -117.237639);

                            Intent nextScreen = new Intent(MapMainActivity.this, RouteActivity.class);
                            nextScreen.putExtra("destinationPlaceId", locations.get(i).second.getPlaceId());
                            nextScreen.putExtra("latitude", currentLocation.latitude);
                            nextScreen.putExtra("longitude", currentLocation.longitude);
                            startActivity(nextScreen);
                            break;

                        }
                    }

//                    Toast.makeText(MapMainActivity.this, "Nothing is happen yet, TODO here", Toast.LENGTH_LONG).show();
                }
            }
        });

        for( MapsConstants.MarkerDetails building : MapsConstants.allBuildings ) {
            allMarkers.add(mMap.addMarker(new MarkerOptions()
                            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_gary))
                            .position(building.getPosition())
                            .title(building.getTitle())
                            .snippet(building.getSnippet())
                            .visible(false)
            ));
        }

        //TODO: finish this function for search routes
        fromRouteActivity();
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

    public void plotNewRoute(ArrayList<Double> Lat, ArrayList<Double> Lng) {
        /*
        for(int i = 0; i < Lat.size(); ++i) {
            route.add(new LatLng(Lat.get(i), Lng.get(i)));
        }
        newRoute = new Route(route);
        mMap.addPolyline(newRoute.drawRoute());
        */
    }

    public void plottingRecommendations(LatLng currentLoc, int buildingId, int transportId)
    {
        /* must have the inputIds converted into destination IDs and transport ID*/
        ArrayList<Route> bestRoutes= RouteProcessing.getBestRoutes(currentLoc, buildingId, transportId, this);
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

/////////////////////////////Component functions//////////////////////////////////////////////////
    //TODO: Should be deleted after it is done
    public void goToAddPlaceActivity(View view){
        Intent intent = new Intent(MapMainActivity.this, AddPlaceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MapMainActivity.this.startActivity(intent);
    }

    final String[] buildingMarkerStatus = {"Show Markers", "Hide Markers"};
    public void toggleBuildingMarkers(View view) {
        Button thisButton = (Button) findViewById(R.id.mapMenuToggleBuildingMarker);

        // Markers are not shown, show markers
        if(thisButton.getText().equals(buildingMarkerStatus[0])) {
            for (Marker currMarker : allMarkers) {
                currMarker.setVisible(true);
            }
            thisButton.setText(buildingMarkerStatus[1]);
        }
        // Markers are shown, hide markers
        else {
            for (Marker currMarker : allMarkers) {
                currMarker.setVisible(false);
            }
            thisButton.setText(buildingMarkerStatus[0]);
        }
    }

    public void toggleMenu(View view) {
        Log.d("MapMainActivity", "Do nothing because Menu always there");
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

    public void logout(View view) {
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

    /*  Button function track
     *  Button name: btnSurrey
     *  Describe: Begin to track the route.
     */
    public void track(View view) {
        if (GPS.tracking == false) {// using the instance variable tracking to keep track of button
            GPS.startGPSTrack();
            // show timer
            timer.setVisibility(View.VISIBLE);
            // reset timer
            timer.setBase(SystemClock.elapsedRealtime());
            // timer start
            timer.start();
            /********************************* TIMER START ***********************************/
            //timerValue = (TextView) findViewById(R.id.timer_text);
            //startTime = SystemClock.uptimeMillis();
            //timerValue.postDelayed(updateTimerThread, 0);
            /**************************** TIMER START END**********************************/

            ((Button) view).setText("Stop");
            if (currentDisplayed != null) {
                // Removes the current displayed polyline when starting to track again
                currentDisplayed.remove();
                currentDisplayed = null; // get rid of currentDispalyed
            }
        } else {
            GPS.stopGPSTrack();
            //stop timer
            timer.stop();
            //hide timer;
            timer.setVisibility(View.GONE);
            /******************************* TIMER STOP ******************************/
            ((Button) view).setText("Track");
            Route thisRoute = GPS.returnCompletedRoute();
            ArrayList<LatLng> latLngRoute = thisRoute.getLatLngArray();
            if (latLngRoute.size() > 1) {
                RouteProcessing.saveRoutePrompt(thisRoute, MapMainActivity.this);
            }

        }
    }

    //Let user enter searchTerm for the destination and drop Marker in the map if any match
    public void searchRoutePrompt(View view){
        AlertDialog.Builder prompt = new AlertDialog.Builder(MapMainActivity.this);
        prompt.setTitle("Search");

        // Set up the Layout, EditText, TextView, CheckBox
        LinearLayout layout = new LinearLayout(MapMainActivity.this);
        final EditText txtSearchInput = new EditText(MapMainActivity.this);
        final TextView txtSearch = new TextView(MapMainActivity.this);

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        txtSearchInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        txtSearchInput.setLayoutParams(lparams);
        txtSearchInput.setHint("Destination");

        txtSearch.setLayoutParams(lparams);
        txtSearch.setText("Destination");

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        layout.addView(txtSearch);
        layout.addView(txtSearchInput);
        prompt.setView(layout);

        // Set up the buttons
        prompt.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                createLocationMarker(txtSearchInput.getText().toString());
                Log.d("MapMainActivity", "All data should be saved");
            }
        });
        prompt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        prompt.show();
    }

/////////////////////////////Helper functions//////////////////////////////////////////////////

    private void createLocationMarker(String searchTerm){
        //Clean any previous Marker if it has any,
        for (Pair<Marker, BuildingDAO> location: locations)
            location.first.remove();

        //Create new Pair Marker and BiildingDAO
        locations = new ArrayList<Pair<Marker,BuildingDAO>>();

        //Generate a list of destionations if there are any match with searchTerm
        ArrayList<BuildingDAO> destinations = BuildingDAO.searchAllBuildings(searchTerm, MapMainActivity.this);
//        ArrayList<Pair<LatLng,String>> destinations = RouteProcessing.findLocations(searchTerm, MapMainActivity.this);

        //destinations is not null means there are some match from database
        if(destinations != null) {
            for (BuildingDAO destination : destinations) {
                Marker newLocation = mMap.addMarker(new MarkerOptions()
                                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_d))
                                .position(destination.getCenterPoint())
                                .title(destination.getName())
                                .snippet("Click here to get routes")
                                .visible(true)
                );
                locations.add(new Pair<Marker, BuildingDAO>(newLocation, destination));
            }
        }
        //destinations is null means no match in database
        else{
            Messenger.error(searchTerm + " is invalid name", MapMainActivity.this);
        }

    }

    public void fromRouteActivity(){
        //TODO: find a way to recognize the previous Activity is RouteActivity.
        String prevActivityName = getIntent().getStringExtra("activity");
        if( prevActivityName != null && prevActivityName.equals("RouteActivity")){
            Messenger.toast("TODO: I am from RouteActivity, now is getBestRoute and display it, lol", MapMainActivity.this);

            int destinationId = getIntent().getIntExtra("destinationPlaceId", 0);
            double latitude = getIntent().getDoubleExtra("latitude", 0);
            double longitude = getIntent().getDoubleExtra("longitude", 0);
            LatLng startLocation = new LatLng(latitude, longitude);

        }



    }
}