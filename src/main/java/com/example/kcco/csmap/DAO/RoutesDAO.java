package com.example.kcco.csmap.DAO;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by KaChan on 5/16/2015.
 */
public class RoutesDAO {
    Activity activity;
    ParseObject current;
    ArrayList<ParseObject> subRoutes;

    public RoutesDAO(Activity a){
        activity = a;
    }
    public RoutesDAO(ParseObject obj, Activity a){
        activity = a;
        current = obj;
    }

    /* Name: setFunctions
     * Describe:
     *      This section is all the set functions for all columns in Route
     * Parameter:
     *      Data input for appropriate column
     * Return:
     */
    //following are Routes table
    public void setRouteId( int routeId ) { current.put(ParseConstant.ROUTES_ROUTE_ID, routeId); }
    public void setStartLoc( int startLoc ) { current.put(ParseConstant.ROUTES_STRLOC, startLoc); }
    public void setEndLoc( int endLoc ) { current.put(ParseConstant.ROUTES_ENDLOC, endLoc); }
    public void setCreatedBy( int userId ) { current.put(ParseConstant.ROUTES_CREATEDBY, userId); }
    public void setTimeSpent( int timeSpent ) { current.put(ParseConstant.ROUTES_TIMESPENT, timeSpent); }
    public void setTransport( int transport ) { current.put(ParseConstant.ROUTES_TRANSPORT, transport); }
    //following are SubRoute table
    public void setSubRouteId( int routeId ) { current.put(ParseConstant.SUBROUTE_ROUTE_ID, routeId); }
    public void setSubRouteIndex( int index ) { current.put(ParseConstant.SUBROUTE_NUM_INDEX, index); }
    public void setSubRoutePoint( LatLng point ) {
        ParseGeoPoint newPoint = new ParseGeoPoint(point.latitude, point.longitude);
        current.put(ParseConstant.SUBROUTE_POINT, newPoint);
    }
    //following are PossibleEdges table
    public void setPossibleEdgeX1( double x1 ) { current.put(ParseConstant.POSSIBLE_X1, x1);}
    public void setPossibleEdgeY1( double y1 ) { current.put(ParseConstant.POSSIBLE_X1, y1);}
    public void setPossibleEdgeX2( double x2 ) { current.put(ParseConstant.POSSIBLE_X1, x2);}
    public void setPossibleEdgeY2( double y2 ) { current.put(ParseConstant.POSSIBLE_X1, y2);}
    public void setPossibleEdgeTransport( int mode ) { current.put(ParseConstant.POSSIBLE_TRANSPORT, mode); }
    public void setPossibleEdgeBlocked( boolean blocked ) { current.put(ParseConstant.POSSIBLE_BLOCKED, blocked); }
    public void setPossibleEdgeReason( String reason ) { current.put(ParseConstant.POSSIBLE_REASON, reason); }

    /* Name: getFunctions
    * Describe:
    *      This section is all the get functions for all columns in User table.
    * Parameter:
    * Return:
    *      Data input for appropriate column
    */
    //Following are Routes table
    public int getRouteId() { return current.getInt(ParseConstant.ROUTES_ROUTE_ID); }
    public int getStartLoc() { return current.getInt(ParseConstant.ROUTES_STRLOC); }
    public int getEndLoc() { return current.getInt(ParseConstant.ROUTES_ENDLOC); }
    public int getCreatedBy() { return current.getInt(ParseConstant.ROUTES_CREATEDBY); }
    public int getTimeSpent() { return current.getInt(ParseConstant.ROUTES_TIMESPENT); }
    public int getTransport() { return current.getInt(ParseConstant.ROUTES_TRANSPORT); }
    //following are SubRoute table
    public int getSubRouteId() { return current.getInt(ParseConstant.SUBROUTE_ROUTE_ID); }
    public int getSubRouteIndex() { return current.getInt(ParseConstant.SUBROUTE_NUM_INDEX); }
    public LatLng getSubRoutePoint() {
        ParseGeoPoint thisPoint = current.getParseGeoPoint(ParseConstant.SUBROUTE_POINT);
        return new LatLng(thisPoint.getLatitude(), thisPoint.getLongitude());
    }
    //following are PossibleEdges table
    public double getPossibleEdgeX1() { return current.getDouble(ParseConstant.POSSIBLE_X1); }
    public double getPossibleEdgeY1() { return current.getDouble(ParseConstant.POSSIBLE_Y1); }
    public double getPossibleEdgeX2() { return current.getDouble(ParseConstant.POSSIBLE_X2); }
    public double getPossibleEdgeY2() { return current.getDouble(ParseConstant.POSSIBLE_Y2); }
    public int getPossibleEdgeTransport() { return current.getInt(ParseConstant.POSSIBLE_BLOCKED); }
    public boolean getPossibleEdgeBlocked() { return current.getBoolean(ParseConstant.POSSIBLE_BLOCKED); }
    public String getPossibleEdgeReason() { return current.getString(ParseConstant.POSSIBLE_REASON); }


    ///////////////////////////////////////////////writing data ////////////////////////////////////////
     /* Name: createRoute (not tested)
     * Describe:
     *      This function is gathering all required data locally before it is
     *      sent to Parse server. This can ensure all required data is filled
     *      properly. All data should be VALID before pass to this function.
     * Parameter:
     *      int startLoc: data required for column startLoc
     *      int endLoc: data required for column endLoc
     *      int createdBy: data required for column createdBy
     *      int transport: data required for column transport
     *      int timeSpent: data required for column timeSpent
     * Return:
     *      int routeId: it is the next empty routeId
     */
    public int createRoute(int startLoc, int endLoc, int createdBy, int transport, int timeSpent){
        current = new ParseObject(ParseConstant.ROUTES);
        int routeId = searchNextEmptyRouteId();

        setRouteId(routeId);
        setTransport(transport);
        setStartLoc(startLoc);
        setEndLoc(endLoc);
        setCreatedBy(createdBy);
        setTimeSpent(timeSpent);

        return routeId;
    }

    /* Name: createRoute (not tested)
     * Describe:
     *      This function is overriding with less argument
     * Parameter:
     *      int startLoc: data required for column startLoc
     *      int endLoc: data required for column endLoc
     *      int createdBy: data required for column createdBy
     *      int transport: data required for column transport
     * Return:
     *      int routeId: it is the next empty routeId
     */
    public int createRoute(int startLoc, int endLoc, int createdBy, int transport){
        return createRoute(startLoc, endLoc, createdBy, transport, 0);
    }

    /* Name: createSubRoute (not tested)
     * Describe:
     *      This function is gathering all required data locally before it is
     *      sent to Parse server. This can ensure all required data is filled
     *      properly. All data should be VALID before pass to this function.
     * Parameter:
     *      int routeId: data required for column routeId
     *      ArrayList<double> x: data required for column x
     *      ArrayList<double> y: data required for column y
     * Return:
     *      int routeId: it is the next empty routeId
     */
    public void createSubRoute(int routeId, ArrayList<LatLng> location){
        subRoutes = new ArrayList<ParseObject>();
        for( int i = 0; i < location.size(); i++ ){
            current = new ParseObject(ParseConstant.SUBROUTE);
            setSubRouteId(routeId);
            setSubRouteIndex(i);
//            setSubRouteX(location.get(i).latitude);
//            setSubRouteY(location.get(i).longitude);
            setSubRoutePoint(location.get(i));

            subRoutes.add(current);
        }
    }

    /* Name: createPossibleEdge (not tested)
     * Describe:
     *      This function is gathering all required data locally before it is
     *      sent to Parse server. This can ensure all required data is filled
     *      properly. All data should be VALID before pass to this function.
     * Parameter:
     *      ArrayList<double> x: data required for column x
     *      ArrayList<double> y: data required for column y
     *      int transportation: data required for column transportation
     * Return:
     *      int routeId: it is the next empty routeId
     */
    public void createPossibleEdge(int transportation, ArrayList<Double> x, ArrayList<Double> y){
        subRoutes = new ArrayList<ParseObject>();
        for( int i = 0; i < x.size()-1; i++ ){
            current = new ParseObject(ParseConstant.POSSIBLE);
            setPossibleEdgeTransport(transportation);
            setPossibleEdgeX1(x.get(i));
            setPossibleEdgeY1(y.get(i));
            setPossibleEdgeX2(x.get(i + 1));
            setPossibleEdgeY2(y.get(i + 1));
            setPossibleEdgeReason("");
            setPossibleEdgeBlocked(false);

            subRoutes.add(current);
        }
    }


/////////////////////////////////////////sending data /////////////////////////////////////////////

    /* Name: sendRouteInfo (not tested)
     * Describe:
     *      it will send route info to Parse, and show message for success or error
     * Parameter:
     * Return:
     */
    public void sendRouteInfo(){
        current.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //route is saved successfully
                    String success = "Route is saved!";
                    Messenger.toast(success, activity);
                } else {
                    //route did not saved.
                    String errorMessage = "Parse Error: sendRouteInfo(): " + e.getMessage();
                    Messenger.error(errorMessage, activity);
                    Messenger.logException(e, "RoutesDAO", "sendRouteInfo");
                }
            }
        });
    }

    /* Name: sendSubRouteInfo (not tested)
     * Describe:
     *      it will send SubRoute info to Parse, and show message for success or error
     * Parameter:
     * Return:
     */
    public void sendSubRouteInfo(){
        ParseObject.saveAllInBackground(subRoutes, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //subRoute is saved successfully
                    String success = "SubRoute is saved!";
                    Messenger.toast(success, activity);
                } else {
                    //subRoute did not saved.
                    String errorMessage = "Parse Error: sendSubRouteInfo(): " + e.getMessage();
                    Messenger.error(errorMessage, activity);
                    Messenger.logException(e, "RoutesDAO", "sendSubRouteInfo");
                }
            }
        });
    }

    /* Name: sendPossibleEdgeInfo (not tested)
     * Describe:
     *      it will send PossibleEdge info to Parse, and show message for success or error
     * Parameter:
     * Return:
     */
    public void sendPossibleEdgeInfo(){
        ParseObject.saveAllInBackground(subRoutes, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //subRoute is saved successfully
                    String success = "SubRoute is saved!";
                    Messenger.toast(success, activity);
                } else {
                    //subRoute did not saved.
                    String errorMessage = "Parse Error: sendSubRouteInfo(): " + e.getMessage();
                    Messenger.error(errorMessage, activity);
                }
            }
        });
    }





/////////////////////////////////searching data ///////////////////////////////////////////////////

    /* Name: searchNextEmptyRouteId() (not tested)
     * Describe:
     *      search for the next empty routeId, just new route has an empty routeId
     * Parameter:
     * Return:
     *      int count is the next unused routeId
     */
    private int searchNextEmptyRouteId() {
        //define local variable(s) here
        int count = 0;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.ROUTES);

        try {
            count = query.count();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "RoutesDAO", "searchNextEmptyRouteId");
        }

        //This part is debug purpose to show all results.
        Log.d("RoutesDAO", "getNextEmptyRouteId() return " + Integer.toString(count));

        return count+1;
    }

    /* Name: searchSubRoutes (not tested)
     * Describe:
     *      it will search all location in a given routeId
     * Parameter:
     *      int routeId: the search parameter.
     *      int index: the search parameter.
     *      boolean reversed: determine the order of SubRoute sequence.
     *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      ArrayList<RoutesDAO> list if any match; else null.
    */
    public static ArrayList<LatLng> searchSubRoutes(int routeId, int index, boolean reversed, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        ArrayList<LatLng> list = null;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.SUBROUTE);
        query.whereEqualTo(ParseConstant.SUBROUTE_ROUTE_ID, routeId);
        if(reversed) {
            query.whereLessThanOrEqualTo(ParseConstant.SUBROUTE_NUM_INDEX, index);
            query.orderByDescending(ParseConstant.SUBROUTE_NUM_INDEX);
        }
        else{
            query.whereGreaterThanOrEqualTo(ParseConstant.SUBROUTE_NUM_INDEX, index);
            query.orderByAscending(ParseConstant.SUBROUTE_NUM_INDEX);
        }

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "RoutesDAO", "searchSubRoutes");
        }

        //There has match cases in User table.
        if( results != null && results.size() != 0){
            list = new ArrayList<LatLng>(results.size());
            Log.d("RoutesDAO", "searchSubRoute(routeId = " + Integer.toString(routeId) +
                    ", index = " + Integer.toString(index) + ") return results size " + results.size());
            for( ParseObject obj: results ) {
                RoutesDAO tempRoute = new RoutesDAO(obj, activity);
                list.add(tempRoute.getSubRoutePoint());
//                Log.d("RouteDAO", "Adding list, routeId = " + Integer.toString(tempRoute.getRouteId())
//                        + " and index = " + Integer.toString(tempRoute.getSubRouteIndex()));

            }


            //This part is debug purpose to show all results.
            Log.d("RoutesDAO", "searchSubRoutes(routeID) return ArrayList<RoutesDAO>, # of location " + list.size());

            //Return result for the calling function.
            return list;
        }

        return null;
    }

    /* Name: searchSubRoutes (not tested)
     * Describe:
     *      This is override function that default with 0 index and increasing order
     * Parameter:
     *      int routeId: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      ArrayList<RoutesDAO> list if any match; else null.
    */
    public static ArrayList<LatLng> searchSubRoutes(int routeId, final Activity activity) {
        return searchSubRoutes(routeId, 0, false, activity);
    }

    /* Name: searchSubRoutes (not tested)
     * Describe:
     *      This is override function with RouteDAO that holds the information
     * Parameter:
     *      RoutesDAO thisRoute: contain data for the search parameter
     *      boolean reversed: determine the order of SubRoute sequence.
     *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      ArrayList<RoutesDAO> list if any match; else null.
    */
    public static ArrayList<LatLng> searchSubRoutes(RoutesDAO thisRoute, boolean reversed, final Activity activity) {
        return searchSubRoutes(thisRoute.getSubRouteId(), thisRoute.getSubRouteIndex(), reversed, activity);
    }

    /* Name: searchARoute (not tested)
     * Describe:
     *      it will search a route with given routeId
     * Parameter:
     *      int routeId: the search parameter
     *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      RouteDAO route if any match; else null.
    */
    public static RoutesDAO searchARoute(int routeId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        RoutesDAO route;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.ROUTES);
        query.whereEqualTo(ParseConstant.ROUTES_ROUTE_ID, routeId);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "RoutesDAO", "searchARoute");
        }

        //There has match cases in User table.
        if( results != null && results.size() != 0){
            route = new RoutesDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("RoutesDAO", "searchARoute(strLoc, endLoc) return RouteDAO " + route.getRouteId());

            //Return result for the calling function.
            return route;
        }

        return null;
    }

    /* Name: searchAllRoutes (not tested)
     * Describe:
     *      it will search all routes (id) with given start and end location
     * Parameter:
     *      int strLoc: the search parameter
     *      int endLoc: the search parameter
     *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      int[] routeId if any match; else null.
    */
    public static int[] searchAllRoutes(int strLoc, int endLoc, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        int routeId[];

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.ROUTES);
        query.whereEqualTo(ParseConstant.ROUTES_STRLOC, strLoc);
        query.whereEqualTo(ParseConstant.ROUTES_ENDLOC, endLoc);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "RoutesDAO", "searchAllRoutes");
        }

        //There has match cases in User table.
        if( results != null && results.size() != 0){
            routeId = new int[results.size()];
            for( int i = 0; i < results.size(); i++ ) {
                RoutesDAO tempRoute = new RoutesDAO(results.get(i), activity);
                routeId[i] = tempRoute.getRouteId();
            }

            //This part is debug purpose to show all results.
            Log.d("RoutesDAO", "searchAllRoutes(routeID) return int[], # of routeId " + results.size());

            //Return result for the calling function.
            return routeId;
        }

        return null;
    }

    /* Name: searchCloseRoutesAsce (not tested)
     * Describe:
     *      it will search the route has END location is same as the destination, and
     *      such routes have any vertices that are close to current location in range. It
     *      will return a list of RoutesDAO to hold information for all result routes.
     *      "Close" is close the current location, not the closest distance.
     * Parameter:
     *      ArrayList<Integer> routeIds: the search parameter
     *      double x: the search parameter
     *      double y: the search parameter.
     *      double distance: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      ArrayList<RoutesDAO> routes if any match; else null.
     */
    public static ArrayList<RoutesDAO> searchCloseRoutesAsce(int destination, double x, double y, double distance, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        ArrayList<RoutesDAO> routes;
        RoutesDAO closestPoint;
        ParseGeoPoint currentLocation = new ParseGeoPoint(x, y);
        double smallestDistance = distance * 2.0;
        int closestIndex = 0;

        int currentRouteId;
        double currentDistance;

        //query to fill out all the search requirement
        //Querry for the start location same as destination in the Routes table
        ParseQuery<ParseObject> endLoc =  ParseQuery.getQuery(ParseConstant.ROUTES);
        endLoc.whereEqualTo(ParseConstant.ROUTES_ENDLOC, destination);

        //Querry for the same routeId in the SubRoute within the given location range.
        ParseQuery<ParseObject> endSubRoute = ParseQuery.getQuery(ParseConstant.SUBROUTE);
        endSubRoute.whereMatchesKeyInQuery(ParseConstant.SUBROUTE_ROUTE_ID, ParseConstant.ROUTES_ROUTE_ID, endLoc);
        endSubRoute.whereWithinMiles(ParseConstant.SUBROUTE_POINT, currentLocation, distance);
        endSubRoute.orderByAscending(ParseConstant.SUBROUTE_NUM_INDEX);
        endSubRoute.setLimit(1000);

        try {
            results = (ArrayList<ParseObject>) endSubRoute.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "RoutesDAO", "searchCloseRouteAsce");
        }

        //There has match cases in Routes and SubRoute Tables
        if( results != null && results.size() != 0){
            routes = new ArrayList<RoutesDAO>();
            currentRouteId = results.get(0).getInt(ParseConstant.SUBROUTE_ROUTE_ID);
            Set<String> keySet = results.get(0).keySet();
            Log.d("RoutesDAO", "searchCloseRouteAsce() ParseObject keySet = " + keySet.toString());


            for( ParseObject obj: results ){
                RoutesDAO temp = new RoutesDAO(obj, activity);
                if( currentRouteId != temp.getRouteId() ){
                    //add the previous RoutesDAO info before beginning the next new route.
                    closestPoint = new RoutesDAO(obj, activity);
                    closestPoint.setSubRouteIndex(closestIndex);
                    closestPoint.setSubRouteId(currentRouteId);
                    routes.add(closestPoint);

                    //reset to current info before next new route
                    currentRouteId = temp.getRouteId();
                    closestIndex = temp.getSubRouteIndex();
                    smallestDistance = distance * 2.0;
                }
                currentDistance = getDistance(currentLocation, temp.getSubRoutePoint());
                if( currentDistance < smallestDistance){
                    closestIndex = temp.getSubRouteIndex();
                    smallestDistance = currentDistance;
                }
            }

            //add the last RoutesDAO with its information.
            RoutesDAO lastRoute = new RoutesDAO(results.get(results.size()-1), activity);
            lastRoute.setSubRouteIndex(closestIndex);
            lastRoute.setRouteId(currentRouteId);
            routes.add(lastRoute);

            //This part is debug purpose to show all results.
            Log.d("RoutesDAO", "searchCloseRouteAsce(destination, x, y, distance) return # of RoutesDAO " + routes.size());

            //Return result for the calling function.
            return routes;
        }
        return null;
    }



    /* Name: searchCloseRoutesDesc (not tested)
     * Describe:
     *      it will search the route has START location is same as the destination, and
     *      such routes have any vertices that are close to current location in range. It
     *      will return a list of RoutesDAO to hold information for all result routes.
     *      "Close" is close the current location, not the closest distance.
     * Parameter:
     *      ArrayList<Integer> routeIds: the search parameter
     *      double x: the search parameter
     *      double y: the search parameter.
     *      double distance: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      ArrayList<RoutesDAO> routes if any match; else null.
     */
    public static ArrayList<RoutesDAO> searchCloseRoutesDesc(int destination, double x, double y, double distance, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        ArrayList<RoutesDAO> routes;
        RoutesDAO closestPoint;
        ParseGeoPoint currentLocation = new ParseGeoPoint(x, y);

        double smallestDistance = distance * 2.0;
        int closestIndex = 0;

        int currentRouteId;
        double currentDistance;

        //query to fill out all the search requirement
        //Querry for the start location same as destination in the Routes table
        ParseQuery<ParseObject> startLoc =  ParseQuery.getQuery(ParseConstant.ROUTES);
        startLoc.whereEqualTo(ParseConstant.ROUTES_STRLOC, destination);

        //Querry for the same routeId in the SubRoute within the given location range.
        ParseQuery<ParseObject> startSubRoute = ParseQuery.getQuery(ParseConstant.SUBROUTE);
        startSubRoute.whereMatchesKeyInQuery(ParseConstant.SUBROUTE_ROUTE_ID, ParseConstant.ROUTES_ROUTE_ID, startLoc);
        startSubRoute.whereWithinMiles(ParseConstant.SUBROUTE_POINT, currentLocation, distance);
        startSubRoute.orderByDescending(ParseConstant.SUBROUTE_NUM_INDEX);
        startSubRoute.setLimit(1000);

        try {
            results = (ArrayList<ParseObject>) startSubRoute.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "RoutesDAO", "searchCloseRouteDesc");
        }

        //There has match cases in Routes and SubRoute Tables
        if( results != null && results.size() != 0){
            routes = new ArrayList<RoutesDAO>();
            currentRouteId = results.get(0).getInt(ParseConstant.SUBROUTE_ROUTE_ID);
            Set<String> keySet = results.get(0).keySet();
            Log.d("RoutesDAO", "searchCloseRouteAsce() ParseObject keySet = " + keySet.toString());

            for( ParseObject obj: results ){
                RoutesDAO temp = new RoutesDAO(obj, activity);
                if( currentRouteId != temp.getRouteId() ){
                    //add the previous RoutesDAO info before beginning the next new route.
                    closestPoint = new RoutesDAO(obj, activity);
                    closestPoint.setSubRouteIndex(closestIndex);
                    closestPoint.setSubRouteId(currentRouteId);
                    routes.add(closestPoint);

                    //reset to current info before next new route
                    currentRouteId = temp.getRouteId();
                    closestIndex = temp.getSubRouteIndex();
                    smallestDistance = distance * 2.0;
                }
                currentDistance = getDistance(currentLocation, temp.getSubRoutePoint());
                if( currentDistance < smallestDistance){
                    closestIndex = temp.getSubRouteIndex();
                    smallestDistance = currentDistance;
                }
            }

            //add the last RoutesDAO with its information.
            RoutesDAO lastRoute = new RoutesDAO(results.get(results.size()-1), activity);
            lastRoute.setSubRouteIndex(closestIndex);
            lastRoute.setRouteId(currentRouteId);
            routes.add(lastRoute);

            //This part is debug purpose to show all results.
            Log.d("RoutesDAO", "searchCloseRouteDesc(destination, x, y, distance) return # of RoutesDAO " + routes.size());

            //Return result for the calling function.
            return routes;
        }
        return null;
    }

    /* Name: searchDestinations (not tested)
     * Describe:
     *      it will tell if the given place exists or not
     * Parameter:
     *      String endLoc: the search parameter
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      true if any match; else false.
     */
    public static ArrayList<Integer> searchDestinations(String endLoc, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        ArrayList<Integer> destinations = null;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.PLACES);
        query.whereEqualTo(ParseConstant.PLACES_NAME, endLoc.toLowerCase());

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "RoutesDAO", "searchDestinations");
        }

        //There has match cases in User table.
        if( results != null && results.size() != 0){
            for( ParseObject obj: results){
                BuildingDAO temp = new BuildingDAO(obj, activity);
                destinations.add(new Integer(temp.getPlaceId()));
            }

            //This part is debug purpose to show all results.
            Log.d("RoutesDAO", "searchDestination(endLoc) return boolean, yes ");

            //Return result for the calling function.
            return destinations;
        }

        return null;
    }


    /* Name: searchClosestPlace (not tested)
    * Describe:
    *      it will search the closest places with given location and distance
    * Parameter:
    *      double x: the search parameter
    *      double y: the search parameter
    *      double distance: the search parameter
    *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      ArrayList<RoutesDAO> list if any match; else null.
    */
    public static String searchClosestPlace(double x, double y, double distance, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        ParseGeoPoint currentLocation = new ParseGeoPoint(x, y);
        String closestPlace = "";
        double smallestDistance = distance * 2;
        double currentDistance;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.PLACES);
        query.whereWithinMiles(ParseConstant.PLACES_CENTER_POINT, currentLocation, distance);
        query.setLimit(10);
//        query.whereLessThanOrEqualTo(ParseConstant.PLACES_X1, x + distance);
//        query.whereGreaterThanOrEqualTo(ParseConstant.PLACES_X1, x - distance);
//        query.whereLessThanOrEqualTo(ParseConstant.PLACES_Y1, y + distance);
//        query.whereGreaterThanOrEqualTo(ParseConstant.PLACES_Y1, y - distance);
//        query.whereLessThanOrEqualTo(ParseConstant.PLACES_X2, x + distance);
//        query.whereGreaterThanOrEqualTo(ParseConstant.PLACES_X2, x - distance);
//        query.whereLessThanOrEqualTo(ParseConstant.PLACES_Y2, y + distance);
//        query.whereGreaterThanOrEqualTo(ParseConstant.PLACES_Y2, y - distance);
//        query.whereLessThanOrEqualTo(ParseConstant.PLACES_X3, x + distance);
//        query.whereGreaterThanOrEqualTo(ParseConstant.PLACES_X3, x - distance);
//        query.whereLessThanOrEqualTo(ParseConstant.PLACES_Y3, y + distance);
//        query.whereGreaterThanOrEqualTo(ParseConstant.PLACES_Y3, y - distance);
//        query.whereLessThanOrEqualTo(ParseConstant.PLACES_X4, x + distance);
//        query.whereGreaterThanOrEqualTo(ParseConstant.PLACES_X4, x - distance);
//        query.whereLessThanOrEqualTo(ParseConstant.PLACES_Y4, y + distance);
//        query.whereGreaterThanOrEqualTo(ParseConstant.PLACES_Y4, y - distance);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "RoutesDAO", "searchClosestPlace");
        }

        //There has match cases in User table.
        if( results != null && results.size() != 0){

            for( ParseObject obj: results){
                BuildingDAO temp = new BuildingDAO(obj, activity);
//                //That is the location has four points different
//                if( temp.getX1() != temp.getX2() || temp.getX1() != temp.getX3() || temp.getX1() != temp.getX4() ||
//                        temp.getY1() != temp.getY2() || temp.getY1() != temp.getY3() || temp.getY1() != temp.getY4()){
//
//                    double tempDistance = 0.0;
//                    currentDistance = getDistance(x, y, temp.getX1(), temp.getY1());
//
//                    tempDistance = getDistance(x, y, temp.getX1(), temp.getY1());
//                    if (tempDistance < currentDistance)
//                        currentDistance = tempDistance;
//
//                    tempDistance = getDistance(x, y, temp.getX2(), temp.getY2());
//                    if (tempDistance < currentDistance)
//                        currentDistance = tempDistance;
//
//                    tempDistance =  getDistance(x, y, temp.getX3(), temp.getY3());
//                    if (tempDistance < currentDistance)
//                        currentDistance = tempDistance;
//
//                    tempDistance =  getDistance(x, y, temp.getX4(), temp.getY4());
//                    if (tempDistance < currentDistance)
//                        currentDistance = tempDistance;
//                }
//                //This is the location has all four points are the same
//                else{
//                    currentDistance = getDistance(x, y, temp.getX1(), temp.getY1());
//                }
                currentDistance = getDistance(currentLocation, temp.getCenterPoint());

                if( currentDistance < smallestDistance ) {
                    smallestDistance = currentDistance;
                    closestPlace = temp.getName();
                }
            }

            //This part is debug purpose to show all results.
            Log.d("RoutesDAO", "searchClosestPlace(x,y,distance) return place " + closestPlace);

            //Return result for the calling function.
            return closestPlace;
        }

        return null;
    }


//    private static double getDistance(double x1, double y1, double x2, double y2){
//        double dx = x1 - x2;
//        double dy = y1 - y2;
//        return Math.sqrt( (dx*dx) + (dy*dy) );
//    }

    private static double getDistance(ParseGeoPoint currentLocation, LatLng subRoutePoint) {
        ParseGeoPoint toLocation = new ParseGeoPoint(subRoutePoint.latitude, subRoutePoint.longitude);
        return currentLocation.distanceInMilesTo(toLocation);
    }







}
