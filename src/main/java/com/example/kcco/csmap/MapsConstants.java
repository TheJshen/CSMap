package com.example.kcco.csmap;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by David on 5/17/2015.
 */
public class MapsConstants {
    static class MarkerDetails {
        private LatLng position;
        private String title;
        private String snippet;

        public MarkerDetails(double lat, double lng, String title) {
            this.position = new LatLng(lat,lng);
            this.title = title;
            this.snippet = "Click here to get classroom schedule information";
        }

        public LatLng getPosition() { return this.position; }
        public String getTitle() { return this.title; }
        public String getSnippet() { return this.snippet; }
    }

    private static final MarkerDetails APM = new MarkerDetails(32.879031, -117.241029, "Applied Physics and Math Building");
    private static final MarkerDetails ASANT = new MarkerDetails(32.883957, -117.241928, "Asante Hall");
    private static final MarkerDetails CENTR = new MarkerDetails(32.878113, -117.237273, "Center Hall");
    private static final MarkerDetails CICC = new MarkerDetails(32.885012, -117.241320, "Copley International Conference Center");
    private static final MarkerDetails CPMC = new MarkerDetails(32.877912, -117.234771, "Conrad Prebys Music Center");
    private static final MarkerDetails CSB = new MarkerDetails(32.880546, -117.239491, "Cognitive Science Building");
    private static final MarkerDetails EBU1 = new MarkerDetails(32.881470, -117.235483, "Engineering Bldg Unit 1");
    private static final MarkerDetails EBU2 = new MarkerDetails(32.881030, -117.233135, "Engineering Bldg Unit 2");
    private static final MarkerDetails ERCA = new MarkerDetails(32.886073, -117.242083, "Eleanor Roosevelt College Administration Building");
    private static final MarkerDetails GH = new MarkerDetails(32.873933, -117.241043, "Galbraith Hall");
    private static final MarkerDetails HSS = new MarkerDetails(32.878390, -117.241672, "Humanities and Social Sciences Building");
    private static final MarkerDetails LEDDN = new MarkerDetails(32.878857, -117.241675, "Ledden Auditorium");
    private static final MarkerDetails LIT = new MarkerDetails(32.880507, -117.233811, "Literature Building");
    private static final MarkerDetails MANDE = new MarkerDetails(32.877776, -117.240072, "Mandeville Center");
    private static final MarkerDetails MAYER = new MarkerDetails(32.875339, -117.240022, "Mayer Hall");
    private static final MarkerDetails MCC = new MarkerDetails(32.881457, -117.240210, "Media Center and Communications");
    private static final MarkerDetails MCGIL = new MarkerDetails(32.879023, -117.242038, "McGill Hall");
    private static final MarkerDetails MYR_A = new MarkerDetails(32.875339, -117.240022, "Mayer Hall - Addition");
    private static final MarkerDetails NSB = new MarkerDetails(32.875252, -117.242816, "Natural Sciences Building");
    private static final MarkerDetails OTRSN = new MarkerDetails(32.886627, -117.241274, "Otterson Hall");
    private static final MarkerDetails PCYNH = new MarkerDetails(32.878291, -117.233940, "Pepper Canyon Hall");
    private static final MarkerDetails PETER = new MarkerDetails(32.879960, -117.240279, "Peterson Hall");
    private static final MarkerDetails PRICE = new MarkerDetails(32.879909, -117.237151, "Price Center Theater");
    private static final MarkerDetails SEQUO = new MarkerDetails(32.882141, -117.240550, "Sequoyah Hall");
    private static final MarkerDetails SME = new MarkerDetails(32.879839, -117.233157, "Structural and Materials Engineering Building");
    private static final MarkerDetails SOLIS = new MarkerDetails(32.880909, -117.239641, "Solis Hall");
    private static final MarkerDetails SSB = new MarkerDetails(32.883946, -117.240443, "Social Sciences Building");
    private static final MarkerDetails WLH = new MarkerDetails(32.880594, -117.234422, "Warren Lecture Hall");
    private static final MarkerDetails YORK = new MarkerDetails(32.874519, -117.240167, "York Hall");

    public static ArrayList<MarkerDetails> allBuildings = new ArrayList<MarkerDetails>(){{
        add(APM); add(ASANT); add(CENTR); add(CICC); add(CPMC); add(CSB); add(EBU1); add(EBU2); add(ERCA); add(GH); add(HSS); add(LEDDN);
        add(LIT); add(MANDE); add(MAYER); add(MCC); add(MCGIL); add(MYR_A); add(NSB); add(OTRSN); add(PCYNH); add(PETER); add(PRICE);
        add(SEQUO); add(SME); add(SOLIS); add(SSB); add(WLH); add(YORK);
    }};

    public static HashMap<String,String> buildingCodes = new HashMap<String,String>(){{
        put("Applied Physics and Math Building","APM"); put("Asante Hall","ASANT"); put("Center Hall","CENTR");
        put("Copley International Conference Center","CICC"); put("Conrad Prebys Music Center","CPMC");
        put("Cognitive Science Building","CSB"); put("Engineering Bldg Unit 1","EBU1"); put("Engineering Bldg Unit 2","EBU2");
        put("Eleanor Roosevelt College Administration Building","ERCA"); put("Galbraith Hall","GH");
        put("Humanities and Social Sciences Building","HSS"); put("Ledden Auditorium","LEDDN");
        put("Literature Building","LIT"); put("Mandeville Center","MANDE"); put("Mayer Hall","MAYER");
        put("Media Center and Communications","MCC"); put("McGill Hall","MCGIL"); put("Mayer Hall - Addition","MYR-A");
        put("Natural Sciences Building","NSB"); put("Otterson Hall","OTRSN"); put("Pepper Canyon Hall","PCYNH");
        put("Peterson Hall","PETER"); put("Price Center Theater","PRICE"); put("Sequoyah Hall","SEQUO");
        put("Structural and Materials Engineering Building","SME"); put("Solis Hall","SOLIS");
        put("Social Sciences Building","SSB"); put("Warren Lecture Hall","WLH"); put("York Hall","YORK");
    }};
}
