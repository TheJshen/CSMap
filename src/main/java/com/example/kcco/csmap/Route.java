package com.example.kcco.csmap;

/**
 * Created by jason on 5/6/15.
 *
 * Last Edit:
 *              Luis 5/18/15
 *
 */

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;

/*
 * This Class will handle all the route drawing and editing. Points of the route will be stored
 * as an arrayList in this object
 */
public class Route {

    private static final int LINE_WIDTH = 10;
    private ArrayList<LatLng> routePoints;
    private int createdBy;
    private double routeTime;
    Date createWhen;


    // No arg constructor. Creates empty route
    public Route() {
        routePoints = new ArrayList<>();
    }

    // Constructor saves the points into the private membervariable
    public Route( ArrayList<LatLng> points ) {
        routePoints = points;
    }

/*    public Route( ArrayList<LatLng> points, String created_by, int routeT)
    {
        createdBy = created_by;
        routeTime = routeT;
    }*/

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

    // Used to pull out the list of Latitude Points
    public ArrayList<Double> getLatitudeArray() {
        ArrayList<Double> toReturn = new ArrayList<>();
        for(int i = 0; i < routePoints.size(); i++ ) {
            toReturn.add(routePoints.get(i).latitude);
        }
        return toReturn;
    }

    // Used to pull out the list of Longitude Points
    public ArrayList<Double> getLongitudeArray() {
        ArrayList<Double> toReturn = new ArrayList<>();
        for(int i = 0; i < routePoints.size(); i++ ) {
            toReturn.add(routePoints.get(i).longitude);
        }
        return toReturn;
    }

    public void setCreatedBy( int userID ) {
        createdBy = userID;
    }

    public void setRouteTime( double timeElapsed ) {
        routeTime = timeElapsed;
    }

    public void setCreationDate( Date date ) {
        createWhen = date;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public double getRouteTime() {
        return routeTime;
    }

    public Date getCreationDate() {
        return createWhen;
    }
}