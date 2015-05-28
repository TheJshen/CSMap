package com.example.kcco.csmap;


import android.app.Activity;
import android.location.Location;
import android.util.Pair;

import com.example.kcco.csmap.DAO.BuildingDAO;
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
    private static final double SEARCH_DISTANCE = 0.01; //in miles
    private static final double BUILDING_DISTANCE = 0.01; //in miles

    /**
     * This should calculate the distance travelled by the different routeID's and
     * return  the three shortest if there is more than 3 routes from and to destination
     *
     * @param currentLoc is the starting point of the route
     * @param destinationID is the place we want to go
     * @param transportID is the transportation the user is going to be using to get to their destination
     * @param activity is just the activity being passes in from the map
     * */
    public static ArrayList<Route> getBestRoutes( LatLng currentLoc, int destinationID, int transportID, final Activity activity)
    {
        ArrayList<LatLng> toBeRoute;
        ArrayList<Route> toShow = new ArrayList<>();

        ArrayList<RoutesDAO> toRoutes = RoutesDAO.searchCloseRoutesAsce(destinationID, currentLoc.latitude, currentLoc.longitude,0.01, transportID,  activity);
        ArrayList<RoutesDAO> fromRoutes = RoutesDAO.searchCloseRoutesDesc(destinationID, currentLoc.latitude, currentLoc.longitude, 0.01,transportID,  activity);


        if ( toRoutes.size() + fromRoutes.size() <= THREE)
        {
            for( int i = 0; i < toRoutes.size() ; i++)
            {
                toBeRoute = RoutesDAO.searchSubRoutes(toRoutes.get(i).getRouteId(), activity);
                toShow.add(new Route(toBeRoute));//might add more parameters of the path
            }
            for( int i = 0; i < fromRoutes.size(); i++)
            {
                toBeRoute = RoutesDAO.searchSubRoutes(fromRoutes.get(i).getRouteId(), activity);
                toShow.add(new Route(toBeRoute));
            }
        }
        else
        {
            double lengthOfPath;
            int index=0;
            int indexFor;
            TreeMap topValues = new TreeMap<Double, ArrayList<LatLng>>();
            while( index < toRoutes.size())
            {
                toBeRoute = RoutesDAO.searchSubRoutes(toRoutes.get(index).getRouteId(), toRoutes.get(index).getSubRouteIndex(), false, activity);

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

            while( index < fromRoutes.size()) {
                toBeRoute = RoutesDAO.searchSubRoutes(fromRoutes.get(index).getRouteId(), fromRoutes.get(index).getSubRouteIndex(), true, activity);

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

    /** TODO: Method that will take an inputted route and use DAO's to get it into the data base
     * Might need a helper method to identify similar routes and not duplicate
     *
     */
    public static void uploadRoute(Route thisRoute, Activity activity) {
        RoutesDAO routeInfo = new RoutesDAO(activity);
        RoutesDAO subRoute = new RoutesDAO(activity);

        //local variables for route information
        int startLocId, endLocId;
        int transport, timeSpent;
        int userId = UserDAO.getCurrentUserId();
        ArrayList<LatLng> latLngRoute = thisRoute.getLatLngArray();

        //search if the start location of thisRoute is existed
        // Makes sure that there is at least two points in the route
        if( latLngRoute.size() > 1 ) {

            // This section assumes it generate info from front end
            startLocId = verifyExistedPlace(latLngRoute.get(0), "Start Location", activity);
            endLocId = verifyExistedPlace(latLngRoute.get(latLngRoute.size()-1), "End Location", activity);
            transport = 8; // will need to call a getter here TODO
            timeSpent = 9382; // in second TODO the time fucntion

            int routeId = routeInfo.createRoute(startLocId, endLocId, userId, transport, timeSpent);
            routeInfo.sendRouteInfo();
            subRoute.createSubRoute(routeId, latLngRoute);
            subRoute.sendSubRouteInfo();
        }
    }



    public static int verifyExistedPlace(LatLng point, String promptName, Activity activity){
        String placeName;
        int placeId;
        int userId = UserDAO.getCurrentUserId();

        //search if the given location point is existed in database.
        BuildingDAO existedPlace = BuildingDAO.searchBuilding(point,
                SEARCH_DISTANCE, activity);

        //the given location point did not exist in database
        if (existedPlace == null) {
            //the placeName should be the string generate from the front end.
            placeName = "Somewhere"; //TODO getting place names
            existedPlace = new BuildingDAO(activity);
            existedPlace.createBuilding(placeName, userId, point, BUILDING_DISTANCE);
            placeId = existedPlace.getPlaceId();
            existedPlace.sendBuildingInfo();
        }
        //the given location point did exist in database
        else {
            placeName = existedPlace.getName();
            //Assume front end prompt current place name, and then user change it.
            placeName = "Somewhere2";
            placeId = existedPlace.getPlaceId();
            existedPlace.setName(placeName);
            existedPlace.sendBuildingInfo();
        }
        return placeId;
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

