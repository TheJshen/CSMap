package com.example.kcco.csmap;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kcco.csmap.DAO.BuildingDAO;
import com.example.kcco.csmap.DAO.RoutesDAO;

import java.util.ArrayList;


public class HistoryActivity extends ActionBarActivity {

    private int userID;
    private int[] history;
    private ArrayList<Pair<String, String>> startEndLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        startEndLocation = new ArrayList<Pair<String, String>>();

        // Get current user to find the appropriate history
        userID = UserDAO.getCurrentUserId();
        history = UserDAO.searchHistoryRoutes(userID, this);
        // loop through history
        // Only shows history if they are available
        if( history != null ) {
            for (int routeId: history) {
                String start, end;

                // Retrieve the route
                RoutesDAO route = RoutesDAO.searchARoute(routeId, HistoryActivity.this);
                // Get the name of the starting location
                BuildingDAO place = BuildingDAO.searchBuilding(route.getStartLoc(), HistoryActivity.this);
                start = place.getName();

                //Get the name of the ending location
                place = BuildingDAO.searchBuilding(route.getStartLoc(), HistoryActivity.this);
                end = place.getName();

                startEndLocation.add(new Pair<String, String>(start, end));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
