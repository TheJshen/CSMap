package com.example.kcco.csmap.DAO;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;

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
    public void setStartLoc( String startLoc ) { current.put(ParseConstant.ROUTES_STRLOC, startLoc); }
    public void setEndLoc( String endLoc ) { current.put(ParseConstant.ROUTES_ENDLOC, endLoc); }
    public void setCreatedBy( int userId ) { current.put(ParseConstant.ROUTES_CREATEDBY, userId); }
    public void setTimeSpent( int timeSpent ) { current.put(ParseConstant.ROUTES_TIMESPENT, timeSpent); }
    public void setTransport( int transport ) { current.put(ParseConstant.ROUTES_TRANSPORT, transport); }
    //following are SubRoute table
    public void setSubRouteId( int routeId ) { current.put(ParseConstant.SUBROUTE_ROUTE_ID, routeId); }
    public void setSubRouteIndex( int index ) { current.put(ParseConstant.SUBROUTE_NUM_INDEX, index); }
    public void setSubRouteX( double x ) { current.put(ParseConstant.SUBROUTE_NUM_X, x); }
    public void setSubRouteY( double y ) { current.put(ParseConstant.SUBROUTE_NUM_Y, y); }
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
    public String getStartLoc() { return current.getString(ParseConstant.ROUTES_STRLOC); }
    public String getEndLoc() { return current.getString(ParseConstant.ROUTES_ENDLOC); }
    public int getCreatedBy() { return current.getInt(ParseConstant.ROUTES_CREATEDBY); }
    public int getTimeSpent() { return current.getInt(ParseConstant.ROUTES_TIMESPENT); }
    public int getTransport() { return current.getInt(ParseConstant.ROUTES_TRANSPORT); }
    //following are SubRoute table
    public int getSubRouteId() { return current.getInt(ParseConstant.SUBROUTE_ROUTE_ID); }
    public int getSubRouteIndex() { return current.getInt(ParseConstant.SUBROUTE_NUM_INDEX); }
    public double getSubRouteX() { return current.getDouble(ParseConstant.SUBROUTE_NUM_X); }
    public double getSubRouteY() { return current.getDouble(ParseConstant.SUBROUTE_NUM_Y); }
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
     *      String startLoc: data required for column startLoc
     *      String endLoc: data required for column endLoc
     *      int createdBy: data required for column createdBy
     *      int transport: data required for column transport
     *      int timeSpent: data required for column timeSpent
     * Return:
     *      int routeId: it is the next empty routeId
     */
    public int createRoute(String startLoc, String endLoc, int createdBy, int transport, int timeSpent){
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
     *      String startLoc: data required for column startLoc
     *      String endLoc: data required for column endLoc
     *      int createdBy: data required for column createdBy
     *      int transport: data required for column transport
     * Return:
     *      int routeId: it is the next empty routeId
     */
    public int createRoute(String startLoc, String endLoc, int createdBy, int transport){
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
    public void createSubRoute(int routeId, ArrayList<Double> x, ArrayList<Double> y){
        subRoutes = new ArrayList<ParseObject>();
        for( int i = 0; i < x.size(); i++ ){
            current = new ParseObject(ParseConstant.SUBROUTE);
            setSubRouteId(routeId);
            setSubRouteIndex(i);
            setSubRouteX(x.get(i));
            setSubRouteY(y.get(i));

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
            setPossibleEdgeX2(y.get(i + 1));
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
                    Messager.toast(success, activity);
                } else {
                    //route did not saved.
                    String errorMessage = "Parse Error: sendRouteInfo(): " + e.getMessage();
                    Messager.error(errorMessage, activity);
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
                    Messager.toast(success, activity);
                } else {
                    //subRoute did not saved.
                    String errorMessage = "Parse Error: sendSubRouteInfo(): " + e.getMessage();
                    Messager.error(errorMessage, activity);
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
                    Messager.toast(success, activity);
                } else {
                    //subRoute did not saved.
                    String errorMessage = "Parse Error: sendSubRouteInfo(): " + e.getMessage();
                    Messager.error(errorMessage, activity);
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
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
     *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      ArrayList<RoutesDAO> list if any match; else null.
    */
    public static ArrayList<LatLng> searchSubRoutes(int routeId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        ArrayList<LatLng> list = null;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.SUBROUTE);
        query.whereEqualTo(ParseConstant.SUBROUTE_ROUTE_ID, routeId);
        query.addAscendingOrder(ParseConstant.SUBROUTE_NUM_INDEX);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //There has match cases in User table.
        if( results != null && results.size() != 0){
            list = new ArrayList<LatLng>(results.size());
            for( ParseObject obj: results ) {
                RoutesDAO tempRoute = new RoutesDAO(obj, activity);
                list.add(new LatLng(tempRoute.getSubRouteX(), tempRoute.getSubRouteY()));
            }


            //This part is debug purpose to show all results.
            Log.d("RoutesDAO", "searchSubRoutes(routeID) return ArrayList<RoutesDAO>, # of location " + list.size());

            //Return result for the calling function.
            return list;
        }

        return null;
    }

    /* Name: searchAllRoutes (not tested)
     * Describe:
     *      it will search all routes with given start and end location
     * Parameter:
     *      String strLoc: the search parameter
     *      String endLoc: the search parameter
     *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      ArrayList<RoutesDAO> list if any match; else null.
    */
    public static int[] searchAllRoutes(String strLoc, String endLoc, final Activity activity) {
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
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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









}
