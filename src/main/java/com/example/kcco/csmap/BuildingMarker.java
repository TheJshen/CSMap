package com.example.kcco.csmap;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

import java.util.ArrayList;

/**
 * Created by David on 5/16/2015.
 */
public class BuildingMarker   {

    public BuildingMarker (GoogleMap mMap) {
        mMap.setOnInfoWindowClickListener(list);

        // Add Location to Map
        ArrayList<MarkerDetails> dets = new ArrayList<MarkerDetails>();
        dets.add(new MarkerDetails(32.879031, -117.241029, "Applied Physics and Math Building"));
        dets.add(new MarkerDetails(32.883957, -117.241928, "Asante Hall"));
        dets.add(new MarkerDetails(32.878113, -117.237273, "Center Hall"));
        dets.add(new MarkerDetails(32.885012, -117.241320, "Copley International Conference Center"));
        dets.add(new MarkerDetails(32.877912, -117.234771, "Conrad Prebys Music Center"));
        dets.add(new MarkerDetails(32.880546, -117.239491, "Cognitive Science Building"));
        dets.add(new MarkerDetails(32.881470, -117.235483, "Engineering Bldg Unit 1"));
        dets.add(new MarkerDetails(32.881030, -117.233135, "Engineering Bldg Unit 2"));
        dets.add(new MarkerDetails(32.886073, -117.242083, "Eleanor Roosevelt College Administration Building"));
        dets.add(new MarkerDetails(32.873933, -117.241043, "Galbraith Hall"));
        dets.add(new MarkerDetails(32.878390, -117.241672, "Humanities and Social Sciences Building"));
        dets.add(new MarkerDetails(32.878857, -117.241675, "Ledden Auditorium"));
        dets.add(new MarkerDetails(32.880507, -117.233811, "Literature Building"));
        dets.add(new MarkerDetails(32.877776, -117.240072, "Mandeville Center"));
        dets.add(new MarkerDetails(32.875339, -117.240022, "Mayer Hall"));
        dets.add(new MarkerDetails(32.881457, -117.240210, "Media Center and Communications"));
        dets.add(new MarkerDetails(32.879023, -117.242038, "McGill Hall"));
        dets.add(new MarkerDetails(32.875339, -117.240022, "Mayer Hall - Addition"));
        dets.add(new MarkerDetails(32.875252, -117.242816, "Natural Sciences Building"));
        dets.add(new MarkerDetails(32.886627, -117.241274, "Otterson Hall"));
        dets.add(new MarkerDetails(32.878291, -117.233940, "Pepper Canyon Hall"));
        dets.add(new MarkerDetails(32.879960, -117.240279, "Peterson Hall"));
        dets.add(new MarkerDetails(32.879909, -117.237151, "Price Center Theater"));
        dets.add(new MarkerDetails(32.882141, -117.240550, "Sequoyah Hall"));
        dets.add(new MarkerDetails(32.879839, -117.233157, "Structural and Materials Engineering Building"));
        dets.add(new MarkerDetails(32.880909, -117.239641, "Solis Hall"));
        dets.add(new MarkerDetails(32.883946, -117.240443, "Social Sciences Building"));
        dets.add(new MarkerDetails(32.880594, -117.234422, "Warren Lecture Hall"));
        dets.add(new MarkerDetails(32.874519, -117.240167, "York Hall"));

        for( MarkerDetails det : dets ) {
            Marker m = mMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.garyp))
                            .position(det.getPosition())
                            .title(det.getTitle())
                            .snippet(det.getSnippet())
            );
        }
    }

    class MarkerDetails {
        private LatLng position;
        private String title;
        private String snippet;

        public MarkerDetails(double lat, double lng, String title) {
            this.position = new LatLng(lat,lng);
            this.title = title;
            this.snippet = "This happens to be " + this.title;
        }

        public LatLng getPosition() { return this.position; }
        public String getTitle() { return this.title; }
        public String getSnippet() { return this.snippet; }
    }

    private OnInfoWindowClickListener list = new OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            Log.v("Listener", "Tapped the " + marker.getTitle() + " info window Gary");
        }
    };

}
