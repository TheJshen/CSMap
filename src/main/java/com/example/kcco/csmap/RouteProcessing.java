package com.example.kcco.csmap;


import android.app.Activity;
import android.location.Location;

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
    //public List<Route> bestRoutes;

    /**
     * This should calculate the distance travelled by the different routeID's and
     * return  the three shortest if there is more than 3 routes from and to destination
     *
     * @param routeIDs This is the list of the routeIDs that are candidates for the route
     */
    public static ArrayList<Route> getBestRoutes( ArrayList<Integer> routeIDs, final Activity activity)
    {
        ArrayList<LatLng> toBeRoute;
        ArrayList<Route> toShow = new ArrayList<>();

        if ( routeIDs.size() <= THREE)
        {
            for( int i = 0; i < routeIDs.size() ; i++)
            {
                toBeRoute = RoutesDAO.searchSubRoutes(routeIDs.get(i), activity);
                toShow.add(new Route(toBeRoute));//might add more parameters of the path
            }
        }
        else
        {
            double lengthOfPath;
            int index=0;
            int indexFor;
            TreeMap topValues = new TreeMap<Double, ArrayList<LatLng>>();
            while( index < routeIDs.size())
            {
                toBeRoute = RoutesDAO.searchSubRoutes(routeIDs.get(index), activity);

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

}

