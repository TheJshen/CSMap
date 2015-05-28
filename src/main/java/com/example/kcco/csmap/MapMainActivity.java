package com.example.kcco.csmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class MapMainActivity extends FragmentActivity implements RouteTracker.LocationCallBack {

    //Constant for distance
    private static final double SEARCH_DISTANCE = 0.01; //in miles
    private static final double BUILDING_DISTANCE = 0.01; //in miles

    //Transport Constant
    private static final int CAR_MODE = 1000;
    private static final int SKATE_MODE = 100;
    private static final int BIKE_MODE = 10;
    private static final int WALK_MODE = 1;

    //saveRoutePrompt Input
    private String promptInput = "";

    //Send Route parameter
    private BuildingDAO startLoc;
    private BuildingDAO endLoc;
    private RoutesDAO subRoute;
    private RoutesDAO routeInfo;
    private int startLocId;
    private int endLocId;
    private int timeSpent;
    private int transport;
    private int userId;


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


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

    // User to store all building markers
    private ArrayList<Marker> allMarkers = new ArrayList<Marker>();
    private ArrayList<Marker> locations = new ArrayList<Marker>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_track);
        setContentView(R.layout.activity_map_main);
        setUpMapIfNeeded();


        GPS = new RouteTracker(this, this);

        // Set up building markers
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                boolean isGaryMaker = false;
                for( int i = 0; i < allMarkers.size(); i++){
                    if( allMarkers.get(i).getId().equals(marker.getId())){
                        isGaryMaker = true;
                        break;
                    }
                }
                if( isGaryMaker ) {
                    Intent nextScreen = new Intent(MapMainActivity.this, RoomAvailOptionsActivity.class);
                    nextScreen.putExtra("BuildingName", marker.getTitle());
                    startActivity(nextScreen);
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
        routeToDisplay = GPS.returnCompletedRoute();
    }

    // This method will be used to plot a line to the map.
    // First it removes a polyline from the map if there is one
    // Then it creates a route object and add it to the map
    //
    public void displayARoute(ArrayList<Double> lat, ArrayList<Double> lng) {
        if( currentDisplayed != null ) {
            currentDisplayed.remove();
        }
        Route toDisplay = new Route(lat, lng);
        currentDisplayed = mMap.addPolyline(toDisplay.drawRoute());
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
            ArrayList<LatLng> latLngRoute = thisRoute.getLatLngArray();
            if(latLngRoute.size() > 1) {
                saveRoutePrompt(latLngRoute);
            }

        }
    }






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

                //TODO: add function dropMarker() here
                Log.d("MapMainActivity", "All data should be saved");

                if(writeToSD(txtSearchInput.getText().toString())){
                    Log.d("MapMainActivity", "write to file sucess");
                }
                else
                    Log.d("MapMainActivity", "write to file failure");
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


    public void saveRoutePrompt(final ArrayList<LatLng> thisRoute){
        routeInfo = new RoutesDAO(MapMainActivity.this);
        subRoute = new RoutesDAO(MapMainActivity.this);

        startLoc = BuildingDAO.searchBuilding(thisRoute.get(0),
                SEARCH_DISTANCE, MapMainActivity.this);
        endLoc = BuildingDAO.searchBuilding(thisRoute.get(thisRoute.size()-1),
                SEARCH_DISTANCE, MapMainActivity.this);

        //Building for popupDialog
        AlertDialog.Builder prompt = new AlertDialog.Builder(MapMainActivity.this);
        prompt.setTitle("User Input");

        // Set up the Layout, EditText, TextView, CheckBox
        LinearLayout layout = new LinearLayout(MapMainActivity.this);
        final EditText txtFromInput = new EditText(MapMainActivity.this);
        final EditText txtToInput = new EditText(MapMainActivity.this);
        final TextView txtFromLoc = new TextView(MapMainActivity.this);
        final TextView txtToLoc = new TextView(MapMainActivity.this);
        final TextView txtTransport = new TextView(MapMainActivity.this);
        final CheckBox boxWalk = new CheckBox(MapMainActivity.this);
        final CheckBox boxCar = new CheckBox(MapMainActivity.this);
        final CheckBox boxBike = new CheckBox(MapMainActivity.this);
        final CheckBox boxSkate = new CheckBox(MapMainActivity.this);

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        txtFromInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        txtFromInput.setLayoutParams(lparams);
        if(startLoc == null) {
            txtFromInput.setHint("From Location");
        }
        else{
            txtFromInput.setText(startLoc.getName());
        }

        txtToInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        txtToInput.setLayoutParams(lparams);
        if(endLoc == null) {
            txtToInput.setHint("To Location");
        }
        else{
            txtToInput.setText(endLoc.getName());
        }

        txtFromLoc.setLayoutParams(lparams);
        txtFromLoc.setText("From Location");
        txtToLoc.setLayoutParams(lparams);
        txtToLoc.setText("To Location");
        txtTransport.setLayoutParams(lparams);
        txtTransport.setText("Transport");

        boxWalk.setLayoutParams(lparams);
        boxWalk.setText("Walk");
        boxBike.setLayoutParams(lparams);
        boxBike.setText("Bike");
        boxSkate.setLayoutParams(lparams);
        boxSkate.setText("Skate");
        boxCar.setLayoutParams(lparams);
        boxCar.setText("Car");

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        layout.addView(txtFromLoc);
        layout.addView(txtFromInput);
        layout.addView(txtToLoc);
        layout.addView(txtToInput);
        layout.addView(boxWalk);
        layout.addView(boxBike);
        layout.addView(boxSkate);
        layout.addView(boxCar);
        prompt.setView(layout);

        // Set up the buttons
        prompt.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startLocId = verifyExistedPlace(startLoc, thisRoute.get(0), txtFromInput.getText().toString());
                endLocId = verifyExistedPlace(endLoc, thisRoute.get(thisRoute.size() - 1), txtToInput.getText().toString());
                //TODO: Timer give out the time here
                timeSpent = 9382; // in second
                transport = 0;
                if (boxWalk.isChecked())
                    transport += WALK_MODE;
                if (boxBike.isChecked())
                    transport += BIKE_MODE;
                if (boxSkate.isChecked())
                    transport += SKATE_MODE;
                if (boxCar.isChecked())
                    transport += CAR_MODE;

                int routeId = routeInfo.createRoute(startLocId, endLocId, userId, transport, timeSpent);
                routeInfo.sendRouteInfo();
                subRoute.createSubRoute(routeId, thisRoute);
                subRoute.sendSubRouteInfo();

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

    private int verifyExistedPlace(BuildingDAO existedPlace, LatLng point, String placeName){
        int placeId;

        //the given location point did not exist in database
        if (existedPlace == null) {
            existedPlace = new BuildingDAO(MapMainActivity.this);
            existedPlace.createBuilding(placeName, userId, point, BUILDING_DISTANCE);
            placeId = existedPlace.getPlaceId();
            existedPlace.sendBuildingInfo();
        }
        //the given location point did exist in database
        else {
            placeId = existedPlace.getPlaceId();
            existedPlace.setName(placeName);
            existedPlace.sendBuildingInfo();
        }
        return placeId;
    }

//    private void createLocationMarker(String searchTerm){
//        ArrayList<Pair<LatLng,String>>
//
//    }

    public Boolean writeToSD(String text){
        Boolean write_successful = false;
        File root=null;
        try {
            // <span id="IL_AD8" class="IL_AD">check for</span> SDcard
            root = Environment.getExternalStorageDirectory();
            Log.i("writeToSD","path.." +root.getAbsolutePath());

            //check sdcard permission
            if (root.canWrite()){
                File fileDir = new File(root.getAbsolutePath());
                fileDir.mkdirs();

                File file= new File(fileDir, "samplefile.txt");
                FileWriter filewriter = new FileWriter(file);
                BufferedWriter out = new BufferedWriter(filewriter);
                out.write(text);
                out.close();
                write_successful = true;
            }
        } catch (IOException e) {
            Log.e("ERROR:---", "Could not write file to SDCard" + e.getMessage());
            write_successful = false;
        }
        return write_successful;
    }
}