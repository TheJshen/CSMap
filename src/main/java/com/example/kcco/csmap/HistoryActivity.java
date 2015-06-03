package com.example.kcco.csmap;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kcco.csmap.DAO.BuildingDAO;
import com.example.kcco.csmap.DAO.RoutesDAO;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jason on 5/30/15.
 */
public class HistoryActivity extends ActionBarActivity {

    private int userID;
    private int[] history;
    private ArrayList<Pair<String, String>> startEndLocation;
    private ArrayList<Integer> routeIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Get current user to find the appropriate history
        userID = UserDAO.getCurrentUserId();
        // Initialize the array
        startEndLocation = new ArrayList<>();
        routeIds = new ArrayList<>();
        // Retrieves all the routes from database and populates the startEndLocation list
        getStartEndLocation();
        // Create the view with the list of routes
        populateView();

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

    // Method used to retrieve the routes of user from database and add them to the list
    private void getStartEndLocation() {
        history = UserDAO.searchHistoryRoutes(userID, this);
        // loop through history
        // Only shows history if they are available
        if( history != null ) {
            for (int routeId : history) {
                String start, end;

                // Retrieve the route
                RoutesDAO route = RoutesDAO.searchARoute(routeId, HistoryActivity.this);
                // Get the name of the starting location
                BuildingDAO place = BuildingDAO.searchBuilding(route.getStartLoc(), HistoryActivity.this);
                start = place.getName();

                //Get the name of the ending location
                place = BuildingDAO.searchBuilding(route.getEndLoc(), HistoryActivity.this);
                end = place.getName();

                startEndLocation.add(new Pair<>(start, end));
                routeIds.add(routeId);
            }
        }
    }

    // Used to format the items in the list TODO: Change to make it look nicer??
    private String createHistoryLabel( Pair<String, String> startEndLocationStrings ) {
        return new String("From: " + startEndLocationStrings.first + "\n" +
                          " To: " + startEndLocationStrings.second);
    }

    // Used to populate the display
    private void populateView() {
        //Set the title
        ((TextView)findViewById(R.id.history_title)).setText("Route History");

        // Loop that populates the view
        for(int i = 0, id = 1; i < startEndLocation.size(); i++, id++) {
            Button newButton = new Button(this); // Instantiate New Button

            // Apply UI Design
            newButton.setId(id);
            newButton.setText( createHistoryLabel(startEndLocation.get(i)) );
            newButton.setTextSize((int) (getResources().getDimension(R.dimen.abc_text_size_body_1_material) / getResources().getDisplayMetrics().density));
            newButton.setTextColor(getResources().getColor(R.color.text_color_important));
            newButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_main, null));

            // Position the new Button
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            rlParams.addRule(RelativeLayout.BELOW, id - 1); // id = 1 (0) is not a valid ID so it gets put in default location

            // Add the Listener for the Button
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO: fill in the response to the button click
                    goToMapActivity(routeIds.get(v.getId() - 1));

                }
            });

            // Add new Layout to RelativeLayout
            ((RelativeLayout) findViewById(R.id.history_main)).addView(newButton,rlParams);
        }
    }

    public void goToMapActivity(int routeId){
        Intent intent = new Intent(HistoryActivity.this, MapMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //add more information into intent before start different activity
        intent.putExtra("routeId", routeId);
        intent.putExtra("activity", "HistoryActivity");
        HistoryActivity.this.startActivity(intent);

    }
}
