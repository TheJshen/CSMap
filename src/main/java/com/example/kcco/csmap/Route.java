package com.example.kcco.csmap;

/**
 * Created by jason on 5/6/15.
 */

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;

public class Route {

    private ArrayList<LatLng> routePts;
    private String createdBy;
    private int routeTime;
    Date createAt, updatedAt;


    public Route( ArrayList<LatLng> points ) {
        routePts = points;
    }

    // Returns PolyLineOptions to be added to the google map
    public PolylineOptions getRoute() {
        return new PolylineOptions()
                .addAll(routePts)
                .width(5)
                .color(Color.BLUE);
    }
}
