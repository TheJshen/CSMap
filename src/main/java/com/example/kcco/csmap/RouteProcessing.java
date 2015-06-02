package com.example.kcco.csmap;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.PowerManager;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kcco.csmap.DAO.BuildingDAO;
import com.example.kcco.csmap.DAO.Messenger;
import com.google.android.gms.maps.model.LatLng;
import com.example.kcco.csmap.DAO.RoutesDAO;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by lvsanche on 5/18/15.
 * Will hold some of the processing of the DAO data that will in turn then become routes.
 * Also will go and use the DAO class to add things.
 */
public class RouteProcessing {

    public final static int THREE= 3;
    //Constant for distance
    private static final double SEARCH_DISTANCE = 0.05; //in miles
    private static final double BUILDING_DISTANCE = 0.05; //in miles

    //Transport Constant
    private static final int CAR_MODE = 1000;
    private static final int SKATE_MODE = 100;
    private static final int BIKE_MODE = 10;
    private static final int WALK_MODE = 1;

    //Send Route parameter
    private static BuildingDAO startLoc;
    private static BuildingDAO endLoc;
    private static int startLocId;
    private static int endLocId;
    private static int timeSpent;
    private static int transport;

    /**
     * This should calculate the distance travelled by the different routeID's and
     * return  the three shortest if there is more than 3 routes from and to destination
     *
     * @param currentLoc is the starting point of the route
     * @param destinationID is the place we want to go
     * @param transportID is the transportation the user is going to be using to get to their destination
     * @param activity is just the activity being passes in from the map
     * */

    public static ArrayList<Route> getBestRoutes( LatLng startPoint, int destinationID, int transportID, final Activity activity)
    {
        ArrayList<LatLng> toBeRoute;
        ArrayList<Route> toShow = new ArrayList<>();
        ArrayList<RoutesDAO> toRouteDao = new ArrayList<>();
        ArrayList<RoutesDAO> fromRouteDao = new ArrayList<>();
        Log.d("getBest", "transportID " + Integer.toString(transportID)+ " destinationID " +Integer.toString(destinationID)
                + " startPoint " + startPoint.toString());
//        Log.d("getBest", "toRoutes startID " + Integer.toString(startID)+ " destinationID " +Integer.toString(destinationID) );
//        int[] toRoutes = RoutesDAO.searchAllRoutes(startID, destinationID, activity);

        int getNumOfRoute = 0;

        toRouteDao = RoutesDAO.searchCloseRoutesAsce(destinationID, startPoint.latitude, startPoint.longitude, SEARCH_DISTANCE, activity);
        fromRouteDao = RoutesDAO.searchCloseRoutesDesc(destinationID, startPoint.latitude, startPoint.longitude, SEARCH_DISTANCE, activity);

//        int[] toRoutes = RoutesDAO.searchAllRoutes(startID, destinationID, activity);
        if ( toRouteDao == null)
            Log.d( "getBestRoutes", "toRoutes is null");

        else {
            Log.d("getBset", "Before transport filter, toRouteDAO size = " + Integer.toString(toRouteDao.size()));
            toRouteDao = getTransportRoutes(toRouteDao, transportID, activity);
            Log.d("getBset", "After transport filter, toRouteDAO size = " + Integer.toString(toRouteDao.size()));
            getNumOfRoute += toRouteDao.size();
        }

//        int[] fromRoutes = RoutesDAO.searchAllRoutes(destinationID, startID, activity);
        if(fromRouteDao == null)
            Log.d( "getBestRoutes", "fromRotues is null");
        else {
            Log.d("getBset", "Before transport filter, fromRouteDAO size = " + Integer.toString(fromRouteDao.size()));
            fromRouteDao = getTransportRoutes(fromRouteDao, transportID, activity);
            Log.d("getBset", "After transport filter, fromRouteDAO size = " + Integer.toString(fromRouteDao.size()));
            getNumOfRoute += fromRouteDao.size();
        }

        //verify if fromRouteDao and toRouteDao are both either null or size 0
        if ( fromRouteDao == null || fromRouteDao.size() == 0) {
            if (toRouteDao == null || toRouteDao.size() == 0){
                Messenger.toast("No Routes found", activity);
                return null;
            }
        }

        Log.d("getBest", "getNumOfRoute is " + Integer.toString(getNumOfRoute));
        //From now, fromRouteDao or toRouteDao have something, so they are not null or size 0
        if ( getNumOfRoute <= THREE)
        {
            if ( toRouteDao != null ) {
                for (int i = 0; i < toRouteDao.size(); i++) {
                    Log.d("getBest", "Adding toRouteDAO, routeId = " + Integer.toString(toRouteDao.get(i).getRouteId())
                            + " and index = " + Integer.toString(toRouteDao.get(i).getSubRouteIndex()));

//                toBeRoute = RoutesDAO.searchSubRoutes(toRouteDao.get(i).getRouteId(), activity);
                    toBeRoute = RoutesDAO.searchSubRoutes(toRouteDao.get(i).getRouteId(), toRouteDao.get(i).getSubRouteIndex(), false, activity);
                    Log.d("getBest", "Adding toRouteDAO, size of subRoute = " + Integer.toString(toBeRoute.size())
                            + " and last latlng = ( " + Double.toString(toBeRoute.get(toBeRoute.size() - 1).latitude) + ", "
                            + Double.toString(toBeRoute.get(toBeRoute.size()-1).longitude) + " )");
                    toShow.add(new Route(toBeRoute));//might add more parameters of the path
                }
            }

            if ( fromRouteDao != null ) {
                for (int i = 0; i < fromRouteDao.size(); i++) {
                    Log.d("getBest", "Adding fromRouteDAO, routeId = " + Integer.toString(fromRouteDao.get(i).getRouteId())
                            + " and index = " + Integer.toString(fromRouteDao.get(i).getSubRouteIndex()));
                    toBeRoute = RoutesDAO.searchSubRoutes(fromRouteDao.get(i).getRouteId(), fromRouteDao.get(i).getSubRouteIndex(), true, activity);
                    Log.d("getBest", "Adding fromRouteDAO, size of subRoute = " + Integer.toString(toBeRoute.size())
                            + " and last latlng = ( " + Double.toString(toBeRoute.get(toBeRoute.size() - 1).latitude) + ", "
                            + Double.toString(toBeRoute.get(toBeRoute.size()-1).longitude) + " )");

                    toShow.add(new Route(toBeRoute));
                }
            }
        }
        else
        {
            double lengthOfPath;
            int index=0;
            int indexFor;
            TreeMap topValues = new TreeMap<Double, ArrayList<LatLng>>();
            if ( toRouteDao != null ) {
                while (index < toRouteDao.size()) {
                    toBeRoute = RoutesDAO.searchSubRoutes(toRouteDao.get(index).getRouteId(), toRouteDao.get(index).getSubRouteIndex(), false, activity);

                    for (lengthOfPath = 0, indexFor = 0; index < toBeRoute.size() - 1; index++) {
                        lengthOfPath += getDistance(toBeRoute.get(indexFor), toBeRoute.get(indexFor + 1));
                    }

                    if (topValues.size() < THREE)
                        topValues.put(lengthOfPath, toBeRoute);
                    else {
                    /*must find the largest lengthOfPath in tree and remove it*/
                        topValues.remove(topValues.lastEntry().getKey());
                        topValues.put(lengthOfPath, toBeRoute);
                    }

                    index++;
                }
            }

            /*Now to loop through all the fromRoutes in reverse and get some of their distances
            *
             */
            index = 0;

            if (fromRouteDao != null) {
                while (index < fromRouteDao.size()) {
                    toBeRoute = RoutesDAO.searchSubRoutes(fromRouteDao.get(index).getRouteId(), fromRouteDao.get(index).getSubRouteIndex(), true, activity);

                    for (lengthOfPath = 0, indexFor = 0; index < toBeRoute.size() - 1; index++) {
                        lengthOfPath += getDistance(toBeRoute.get(indexFor), toBeRoute.get(indexFor + 1));
                    }

                    if (topValues.size() < THREE)
                        topValues.put(lengthOfPath, toBeRoute);
                    else {
                    /*must find the largest lengthOfPath in tree and remove it*/
                        topValues.remove(topValues.lastEntry().getKey());
                        topValues.put(lengthOfPath, toBeRoute);
                    }
                    index++;
                }
            }

            /*now to return the array of three routes
             */
            toShow.add(new Route((ArrayList<LatLng>)topValues.firstEntry().getValue()));
            toShow.add(new Route((ArrayList<LatLng>)topValues.lastEntry().getValue()));
            topValues.remove(topValues.firstEntry().getKey());
            toShow.add(new Route((ArrayList<LatLng>) topValues.firstEntry().getValue()));
        }
        return toShow;
    }


    public static ArrayList<Route> getBestRoutes( int startID, int destinationID, int transportID, final Activity activity)
    {
        ArrayList<LatLng> toBeRoute;
        ArrayList<Route> toShow = new ArrayList<>();
        ArrayList<RoutesDAO> toRouteDao = new ArrayList<>();
        ArrayList<RoutesDAO> fromRouteDao = new ArrayList<>();
        Log.d("getBest", "toRoutes startID " + Integer.toString(startID)+ " destinationID " +Integer.toString(destinationID) );
        int[] toRoutes = RoutesDAO.searchAllRoutes(startID, destinationID, activity);
        if ( toRoutes == null)
        {
            Log.d( "getBestRoutes", "toRoutes is null");
        }
        else
            toRouteDao= getTransportRoutes(toRoutes, transportID, activity);

        int[] fromRoutes = RoutesDAO.searchAllRoutes(destinationID, startID, activity);
        if(fromRoutes == null)
            Log.d( "getBestRoutes", "fromRotues is null");
        else
            toRouteDao= getTransportRoutes(toRoutes, transportID, activity);



        if ( fromRouteDao.size() != 0 && toRouteDao.size() != 0)
        {
            Messenger.toast("No Routes found", activity);
            return null;
        }

        if ( toRouteDao.size() + fromRouteDao.size() <= THREE)
        {
            for( int i = 0; i < toRouteDao.size() ; i++)
            {
                toBeRoute = RoutesDAO.searchSubRoutes(toRouteDao.get(i).getRouteId(), activity);
                toShow.add(new Route(toBeRoute));//might add more parameters of the path
            }
            for( int i = 0; i < fromRouteDao.size(); i++)
            {
                toBeRoute = RoutesDAO.searchSubRoutes(fromRouteDao.get(i).getRouteId(), activity);
                toShow.add(new Route(toBeRoute));
            }
        }
        else
        {
            double lengthOfPath;
            int index=0;
            int indexFor;
            TreeMap topValues = new TreeMap<Double, ArrayList<LatLng>>();
            while( index < toRoutes.length)
            {
                toBeRoute = RoutesDAO.searchSubRoutes(toRouteDao.get(index).getRouteId(), toRouteDao.get(index).getSubRouteIndex(), false, activity);

                for(lengthOfPath=0, indexFor= 0; index < toBeRoute.size()-1; index++ )
                {
                    lengthOfPath += getDistance(toBeRoute.get(indexFor), toBeRoute.get(indexFor + 1));
                }

                if ( topValues.size() < THREE)
                    topValues.put(lengthOfPath, toBeRoute);
                else
                {
                    /*must find the largest lengthOfPath in tree and remove it*/
                    topValues.remove(topValues.lastEntry().getKey());
                    topValues.put(lengthOfPath, toBeRoute);
                }

                index++;
            }

            /*Now to loop through all the fromRoutes in reverse and get some of their distances
            *
             */
            index = 0;

            while( index < fromRouteDao.size()) {
                toBeRoute = RoutesDAO.searchSubRoutes(fromRouteDao.get(index).getRouteId(), fromRouteDao.get(index).getSubRouteIndex(), true, activity);

                for (lengthOfPath = 0, indexFor = 0; index < toBeRoute.size() - 1; index++) {
                    lengthOfPath += getDistance(toBeRoute.get(indexFor), toBeRoute.get(indexFor + 1));
                }

                if (topValues.size() < THREE)
                    topValues.put(lengthOfPath, toBeRoute);
                else {
                    /*must find the largest lengthOfPath in tree and remove it*/
                    topValues.remove(topValues.lastEntry().getKey());
                    topValues.put(lengthOfPath, toBeRoute);
                }

                index++;

            }

            /*now to return the array of three routes
             */
            toShow.add(new Route((ArrayList<LatLng>)topValues.firstEntry().getValue()));
            toShow.add(new Route((ArrayList<LatLng>)topValues.lastEntry().getValue()));
            topValues.remove(topValues.firstEntry().getKey());
            toShow.add(new Route((ArrayList<LatLng>) topValues.firstEntry().getValue()));


        }

        return toShow;

    }





    public static ArrayList<RoutesDAO> getTransportRoutes( ArrayList<RoutesDAO> allDaos, int transportID, Activity activity) {
        ArrayList<RoutesDAO> toReturn = new ArrayList<>();
//        ArrayList<RoutesDAO> allDaos = new ArrayList<>();

//        for( int j = 0; j < inputIDs.length; j ++)
//        {
//            allDaos.add(RoutesDAO.searchARoute(inputIDs[j], activity));
//        }

        boolean carMode = false, skateMode = false, bikeMode = false, walkMode = false;

        int mode = transportID;
        //check if selected for car
        if( mode / CAR_MODE == 1 )
            carMode = true;

        mode %= CAR_MODE;
        //check if selected for skate
        if( mode / SKATE_MODE == 1 )
            skateMode = true;

        mode %= SKATE_MODE;
        //check if selected for bike
        if( mode / BIKE_MODE == 1 )
            bikeMode = true;

        mode %= BIKE_MODE;
        //check if selected for walk
        if( mode / WALK_MODE == 1 )
            walkMode = true;

        Log.d("getTransportRoutes", "carMode = " + Boolean.toString(carMode) + " skateMode = " + Boolean.toString(skateMode)
                + " bikeMode = " + Boolean.toString(bikeMode) + " walkMode = " + Boolean.toString(walkMode));

        /*time to remove some of the routeDAO's that do not have the transportation that we need*/
        for ( int i = 0; i < allDaos.size(); ++i)
        {
            int routeMode = RoutesDAO.searchARoute(allDaos.get(i).getRouteId(),activity).getTransport();
            Log.d("getTransportRoutes", "routeMode = " + Integer.toString(routeMode));
            if ( carMode && routeMode / CAR_MODE == 1 ){
                toReturn.add(allDaos.get(i));
                continue;
            }

            routeMode %= CAR_MODE;
            if ( skateMode && routeMode / SKATE_MODE == 1 ){
                toReturn.add(allDaos.get(i));
                continue;
            }

            routeMode %= SKATE_MODE;
            if ( bikeMode && routeMode / BIKE_MODE == 1 ){
                toReturn.add(allDaos.get(i));
                continue;
            }

            routeMode %= BIKE_MODE;
            if ( walkMode && routeMode / WALK_MODE == 1 ){
                toReturn.add(allDaos.get(i));
                continue;
            }
            Log.d("getTransportRoutes", "no match transport for routeId " + Integer.toString(allDaos.get(i).getRouteId()));

        }

        return toReturn;
    }

    public static ArrayList<RoutesDAO> getTransportRoutes( int[] inputIDs, int transportID, Activity activity)
    {
        ArrayList<RoutesDAO> toReturn = new ArrayList<>();
        ArrayList<RoutesDAO> allDaos = new ArrayList<>();

        for( int j = 0; j < inputIDs.length; j ++)
        {
            allDaos.add(RoutesDAO.searchARoute(inputIDs[j], activity));
        }
        /*time to remove some of the routeDAO's that do not have the transportation that we need*/
        for ( int i = 0; i < allDaos.size(); ++i)
        {
            if ( allDaos.get(i).getTransport() - transportID > 0 )
                toReturn.add(allDaos.get(i)); // means that our ID is less restrictive than the routes availability
        }

        return toReturn;
    }

    public static double getDistance( LatLng start, LatLng end )
    {
        Location starting = new Location("a");
        starting.setLatitude(start.latitude);
        starting.setLongitude(start.longitude);
        Location ending = new Location("b");
        ending.setLatitude(end.latitude);
        ending.setLongitude(end.longitude);

        return starting.distanceTo(ending);

    }



    /** Method that will take a current location and give the best routes to the destination
     * Up to whoever wants to work on it.
     * Implementation should go something like this:
     *      find all the paths to the destination, with that find the closest coordinate to the
     *      current location, have a certain threshold. Looking through the individual coordinate
     *      arrays of the routes might be the difficult thing to figure out.
     *
     */
    public void findClosestRoute()
    {

    }

    /**
     * Might need a helper method to identify similar routes and not duplicate
     *
     */
    public static void uploadRoute(Route thisRoute, int transport, int timeSpent, int startLocId, int endLocId, Activity activity) {
        RoutesDAO routeInfo = new RoutesDAO(activity);
        RoutesDAO subRoute = new RoutesDAO(activity);

        //local variables for route information
        int userId = UserDAO.getCurrentUserId();
        ArrayList<LatLng> latLngRoute = thisRoute.getLatLngArray();

        //search if the start location of thisRoute is existed
        // Makes sure that there is at least two points in the route
        if( latLngRoute.size() > 1 ) {

            int routeId = routeInfo.createRoute(startLocId, endLocId, userId, transport, timeSpent);
            routeInfo.sendRouteInfo();
            subRoute.createSubRoute(routeId, latLngRoute);
            subRoute.sendSubRouteInfo();
        }
    }

    //This function either create new place into database
    //or extract place info from database to update with place name
    //No matter which way, corresponding placeId should be returned by this function
    public static int verifyExistedPlace(BuildingDAO existedPlace, LatLng point, String placeName, Activity activity){
        int placeId;
        int userId = UserDAO.getCurrentUserId();

        //the given location point did not exist in database
        if (existedPlace == null) {
            existedPlace = new BuildingDAO(activity);
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


    //Create a popup Prompt to ask user input name for start and end locations
    //Also, user can choose the transportation that he/she sed for the route.
    //At the end, the info for start and end location and route are saved into Parse.
    public static void saveRoutePrompt(final Route thisRoute, final Activity activity){
        final ArrayList<LatLng> latLngRoute = thisRoute.getLatLngArray();

        startLoc = BuildingDAO.searchBuilding(latLngRoute.get(0),
                SEARCH_DISTANCE, activity);
        endLoc = BuildingDAO.searchBuilding(latLngRoute.get(latLngRoute.size()-1),
                SEARCH_DISTANCE, activity);

        //Building for popupDialog
        AlertDialog.Builder prompt = new AlertDialog.Builder(activity);
        prompt.setTitle("User Input");

        // Set up the Layout, EditText, TextView, CheckBox
        LinearLayout layout = new LinearLayout(activity);
        final EditText txtFromInput = new EditText(activity);
        final EditText txtToInput = new EditText(activity);
        final TextView txtFromLoc = new TextView(activity);
        final TextView txtToLoc = new TextView(activity);
        final TextView txtTransport = new TextView(activity);
        final CheckBox boxWalk = new CheckBox(activity);
        final CheckBox boxCar = new CheckBox(activity);
        final CheckBox boxBike = new CheckBox(activity);
        final CheckBox boxSkate = new CheckBox(activity);

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
                startLocId = RouteProcessing.verifyExistedPlace(startLoc, latLngRoute.get(0), txtFromInput.getText().toString(), activity);
                endLocId = RouteProcessing.verifyExistedPlace(endLoc, latLngRoute.get(latLngRoute.size() - 1), txtToInput.getText().toString(), activity);
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

                RouteProcessing.uploadRoute(thisRoute, transport, timeSpent, startLocId, endLocId, activity);

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



    /**
     * TODO Method to use the DAO and get the different RouteIds associated with the end and
     * starting points
     */
    public ArrayList<Integer> findRoutes( String start, String destination, int transport)
    {
        return null;
    }


    /**
     * TODO must find the closest building around the given point of reference
     * Might need to move this method elsewhere tha is more appropriate
     */
    public void findClosestBuilding()
    {

    }

    public static ArrayList<Pair<LatLng,String>> findLocations( String searchTerm, Activity activity)
    {
        ArrayList<BuildingDAO> buildings = BuildingDAO.searchAllBuildings(searchTerm, activity);
        if(buildings != null) {
            ArrayList<Pair<LatLng, String>> toReturn = new ArrayList<>();
            for (int i = 0; i < buildings.size(); i++) {
                toReturn.add(new Pair(buildings.get(i).getCenterPoint(), buildings.get(i).getName()));
            }

            return toReturn;
        }

        return null;
    }


    public static int findBuildingID( String searchBuilding, Activity activity)
    {
        /*first make the building DAO based on a name we know exists and is correct*/
        BuildingDAO bDAO = BuildingDAO.searchBuilding(searchBuilding, activity);
        /*check to see that is it not a null dao*/
        if ( bDAO!= null)
        {
            /* need to return the ID of the building*/
            return bDAO.getPlaceId();
        }
        else
            return 0;
    }

} // END OF FILE

