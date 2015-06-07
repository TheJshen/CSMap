package com.example.kcco.csmap;

/**
 * Created by jason on 5/6/15.
 *
 * Last Edit:
 *              Luis 5/18/15
 *
 */

import android.graphics.Color;
import android.widget.Chronometer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;

/**
 * This Class will handle all the route drawing and editing. Points of the route will be stored
 * as an arrayList in this object
 */
public class Route {

    // Constants
    private static final int LINE_WIDTH = 10;
    private static final int CENTER_ZOOM = 19;
    private static final int CENTER_TILT = 45;
    private static final int CENTER_BEARING =0;

    // local parameters
    private GoogleMap map; // reference to the canvas
    private ArrayList<LatLng> routePoints; // list of points that makes up the route
    private Marker start, end; // marker that designates beginning and end of route
    private Polyline visibleLine; // visible line on the map
    private Chronometer routeTime; // time of route
    private LatLng startLoc, endLoc; // location of start and end point

    // Database ids
    private int routeID;
    private int createdBy;
    Date createWhen;


    public Route() {
        routePoints = new ArrayList<>();
    }

    public Route(ArrayList<LatLng> points) {
        routePoints = points;
    }

    /**
     * Constructor that takes the reference to the map to draw on
     * @param mMap
     */
    public Route(GoogleMap mMap) {
        map = mMap; // map reference to display on
        routePoints = new ArrayList<>(); // create empty list of points
    }

    /**
     * Constructor that takes an array of points.
     * Takes a reference to the map so it knows what to draw on
     * @param mMap
     * @param points
     */
    public Route( GoogleMap mMap, ArrayList<LatLng> points ) {
        map = mMap; // map reference to display on
        routePoints = points; // saving the reference to the points
    }

    /**
     * Constructor that takes an array of points and reference to the map to draw on
     * The routeId to store in database and the current location to generate start and end points
     * @param mMap
     * @param points
     * @param routeId
     * @param current
     */
    public Route( GoogleMap mMap, ArrayList<LatLng> points, int routeId, LatLng current ) {
        map = mMap; // map reference to display on
        routePoints = points; // saving the reference to the list of points
        routeID = routeId; // route id to find/save to database
        processStartEndMarker(current); // Finds the start end marker
    }

    /**
     * This method will be used to find which end of the line is the start and end of the path
     * based on the current location of the user at the time of execution.
     * @param current
     */
    private void processStartEndMarker( LatLng current ) {
        // Calculate distance1
        double distance1 = RouteProcessing.getDistance(current, routePoints.get(0));
        // Calculate distance2
        double distance2 = RouteProcessing.getDistance(current, routePoints.get(routePoints.size()-1));

        // Determine start and end
        if( distance1 < distance2 ) {
            endLoc = routePoints.get(0);
            startLoc = routePoints.get(routePoints.size()-1);

        } else {
            startLoc = routePoints.get(0);
            endLoc = routePoints.get(routePoints.size()-1);
        }
    }

    /**
     *  Method to create both markers on the map
     */
    public void createMarker(){
        createStartMarker();
        createEndMarker();
    }

    /**
     * This method would be used to create the start marker on the map
     */
    public void createStartMarker() {
        start = map.addMarker(new MarkerOptions()
                .position(startLoc)
                .title("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    /**
     * This method will create the end marker on the map
     */
    public void createEndMarker() {
        end = map.addMarker(new MarkerOptions()
                .position(endLoc)
                .title("Finish")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

    }

    /**
     * Clear all markers on the map that belongs to this
     */
    public void clearAllMarkers() {
        if(end != null)
            end.remove();
        if(start != null)
            start.remove();
    }

    /**
     * Clear just the start marker
     */
    public void clearStartMarker() {
        if(start != null)
            start.remove();
    }

    /**
     * clears the polyline off the map
     */
    public void clearPolyline() {
        if(visibleLine != null)
            visibleLine.remove();
    }

    /**
     * removes all markers and polyline related to this route off the map
     */
    public void removeAll() {
        clearAllMarkers();
        clearPolyline();
    }

    /**
     * Method to draw the line onto the map
     */
    public void draw() {
        // check if it is already showing
        if(visibleLine != null)
            visibleLine.remove();
        // Add to map
        visibleLine = map.addPolyline(new PolylineOptions()
                .addAll(routePoints)
                .width(LINE_WIDTH)
                .color(Color.BLUE));
    }

    /**
     * Method to draw the line with non default color(blue)
     * @param color
     */
    public void draw(int color) {
        // check if it is already showing
        if(visibleLine != null)
            visibleLine.remove();
        // Add to map
        visibleLine = map.addPolyline(new PolylineOptions()
                .addAll(routePoints)
                .width(LINE_WIDTH)
                .color(color));
    }

    /**
     * Method used to center camera on the start marker
     */
    public void centerCameraOnStart() {
        CameraPosition cameraPos = new CameraPosition.Builder()
                .target(startLoc)
                .zoom(CENTER_ZOOM)
                .bearing(CENTER_BEARING)
                .tilt(CENTER_TILT)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
    }

    /**
     * Method used to center on end marker
     */
    public void centerCameraOnFinish() {
        CameraPosition cameraPos = new CameraPosition.Builder()
                .target(endLoc)
                .zoom(CENTER_ZOOM)
                .bearing(CENTER_BEARING)
                .tilt(CENTER_TILT)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
    }

    // Route constructor
    public Route( ArrayList<Double> lat, ArrayList<Double> lng ) {
        routePoints = new ArrayList<LatLng>();
        for(int i = 0; i < lat.size(); i++ ) {
            LatLng newCoord = new LatLng( lat.get(i), lng.get(i) );
            routePoints.add(newCoord);
        }
    }


    // Returns PolyLineOptions to be added to the google map
    public PolylineOptions drawRoute() {
        return new PolylineOptions()
                .addAll(routePoints)
                .width(LINE_WIDTH)
                .color(Color.BLUE);
    }

    // Draws route on map with a specific color.
    // Color is 32-bit ARGB
    public PolylineOptions drawRoute(int color) {
        return new PolylineOptions()
                .addAll(routePoints)
                .width(LINE_WIDTH)
                .color(color);
    }

    // Append a point onto a route
    public void addToRoute(LatLng lastPoint) {
        routePoints.add(lastPoint); // append a point to the array
    }

    // Appends another point onto the route. Used for live tracking
    // This is only for adding points to a route that is already drawn
    // on the map
    public void addToRoute(Polyline route, LatLng lastPoint) {
        routePoints.add(lastPoint); // append a point to the route already displayed
        route.setPoints(routePoints); //
    }

    // Getter method
    public ArrayList<LatLng> getLatLngArray() {
        return routePoints;
    }

//    // Used to pull out the list of Latitude Points
//    public ArrayList<Double> getLatitudeArray() {
//        ArrayList<Double> toReturn = new ArrayList<>();
//        for(int i = 0; i < routePoints.size(); i++ ) {
//            toReturn.add(routePoints.get(i).latitude);
//        }
//        return toReturn;
//    }
//
//    // Used to pull out the list of Longitude Points
//    public ArrayList<Double> getLongitudeArray() {
//        ArrayList<Double> toReturn = new ArrayList<>();
//        for(int i = 0; i < routePoints.size(); i++ ) {
//            toReturn.add(routePoints.get(i).longitude);
//        }
//        return toReturn;
//    }

    /**
     * Set the userId
     * @param userID
     */
    public void setCreatedBy( int userID ) {
        createdBy = userID;
    }

    /**
     * set the route time
     * @param timeElapsed
     */
    public void setRouteTime( Chronometer timeElapsed ) {
        routeTime = timeElapsed;
    }

    /**
     * set the date when it is created
     * @param date
     */
    public void setCreationDate( Date date ) {
        createWhen = date;
    }

    /**
     * set the routeId to reference in teh database
     * @param routeId
     */
    public void setRouteId( int routeId ) {
        routeID = routeId;
    }

    /**
     * Get the userId
     * @return
     */
    public int getCreatedBy() {
        return createdBy;
    }

    /**
     * Get the time to take the route
     * @return
     */
    public Chronometer getRouteTime() {
        return routeTime;
    }

    /**
     * Get the date that it was created
     * @return
     */
    public Date getCreationDate() {
        return createWhen;
    }

    /**
     * Get route Id to search in the dataebase
     * @return
     */
    public int getRouteID() {
        return routeID;
    }

    /**
     * pass back the polyline so you it can be altered
     * @return
     */
    public Polyline getPolyline() {
        return visibleLine;
    }

    /**
     * Sets the line color to red
     */
    public void setColorRed() {
        visibleLine.setColor(Color.RED);
    }

    /**
     * Sets the line color to blue
     */
    public void setColorBlue() {
        visibleLine.setColor(Color.BLUE);
    }

    /**
     * set the z index to 0
     */
    public void setZIndexZero() {
        visibleLine.setZIndex(0);
    }
}