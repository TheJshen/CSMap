package com.example.kcco.csmap;

/**
 * Created by jason on 5/6/15.
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

    private ArrayList<LatLng> routePts;
    private String createdBy;
    private int routeTime;
    Date createAt, updatedAt;


    // Constructor saves the points into the private membervariable
    public Route( ArrayList<LatLng> points ) {
        routePts = points;
    }

    // Returns PolyLineOptions to be added to the google map
    public PolylineOptions drawRoute() {
        return new PolylineOptions()
                .addAll(routePts)
                .width(5)
                .color(Color.BLUE);
    }

    // Appends another point onto the route. Used for live tracking
    public void addToRoute(Polyline route, LatLng lastPoint) {
        routePts.add(lastPoint); // append a point to the route already displayed
        route.setPoints(routePts); //
    }
}
