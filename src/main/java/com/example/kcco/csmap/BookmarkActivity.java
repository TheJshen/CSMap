package com.example.kcco.csmap;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kcco.csmap.DAO.BuildingDAO;
import com.example.kcco.csmap.DAO.Messenger;
import com.example.kcco.csmap.DAO.RoutesDAO;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Teresa on 5/22/15.
 */

/**
 * This is the activity where all bookmark related methods wil be places
 * this class can be called from the MapMainActivity.
 */
public class BookmarkActivity extends ActionBarActivity {

    private int userID;
    private int[] bookmarks; // Array of bookmarks retrieved from database
    // Array that indexes the start and end location of all bookmarks from
    // the array above
    private ArrayList<Pair<String, String>> startEndLocation;
    // Array of the Ids to be used to retrieve from database
    private ArrayList<Integer> routeIds;
    private LatLng startingLocation= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        // Get current user to find the appropriate bookmarks for
        userID = UserDAO.getCurrentUserId();
        // Initialize the array
        startEndLocation = new ArrayList<>();
        routeIds = new ArrayList<>();
        // Retrieves all the routes from database and poulates the startEndLocation list
        getStartEndLocation();
        // Create the view with list of routes
        populateView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bookmark, menu);
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

    // Retrieves the data to be displayed in the bookmark list when users
    // touches the bookmarks tab on hte main screen
    public void getStartEndLocation() {
        bookmarks = UserDAO.searchBookmarkRoutes(userID, this);
        // loop through bookmarks
        // Only shows bookmarks if they are available
        if (bookmarks != null) {

            for (int routeId : bookmarks) {
                String start, end;

                // Retrieve the route
                RoutesDAO route = RoutesDAO.searchARoute(routeId, BookmarkActivity.this);
                // Get the name of the starting location
                BuildingDAO place = BuildingDAO.searchBuilding(route.getStartLoc(), BookmarkActivity.this);
                start = place.getName();


                // Get the name of the ending location
                place = BuildingDAO.searchBuilding(route.getEndLoc(), BookmarkActivity.this);
                end = place.getName();

                startEndLocation.add(new Pair<>(start, end));
                routeIds.add(routeId);
            }
        }
    }

    // Used to format the items in the list:
    private String createBookmarkLabel( Pair<String, String> startEndLocationStrings ) {
        // Format for each cell on the bookmark display
        return new String("From: " + startEndLocationStrings.first + "\n" +
                          " To: " + startEndLocationStrings.second);
    }

    // Used to populate the display
    private void populateView() {
        //Set the title
        ((TextView)findViewById(R.id.bookmark_title)).setText("Bookmarks");

        // Loop that populates the view
        for(int i = 0, id = 1; i < startEndLocation.size(); i++, id++) {
            Button newButton = new Button(this); // Instantiate New Button

            // Apply UI Design
            newButton.setId(id);
            newButton.setText( createBookmarkLabel(startEndLocation.get(i)) );
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

                    goToMapActivity(routeIds.get(v.getId() - 1));
                }
            });

            // Add new Layout to RelativeLayout
            ((RelativeLayout) findViewById(R.id.bookmark_main)).addView(newButton,rlParams);
        }
    }

    // Method used to transition from bookmark activity to the main map activity
    // Passing back the selected route to be displayed if needed.
    public void goToMapActivity(int routeId){
        RoutesDAO start = RoutesDAO.searchARoute(routeId, BookmarkActivity.this);
        BuildingDAO place = BuildingDAO.searchBuilding(start.getStartLoc(), BookmarkActivity.this);
        if ( place != null)
            startingLocation = place.getCenterPoint();
        else
            Messenger.toast("Corrupt Bookmark", BookmarkActivity.this);
        Intent intent = new Intent(BookmarkActivity.this, MapMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //add more information into intent before start different activity
        Bundle args = new Bundle();
        args.putParcelable("startingLocation", startingLocation);
        intent.putExtra("routeId", routeId);
        intent.putExtra("activity", "BookmarkActivity");
        intent.putExtra("bundle", args);
        BookmarkActivity.this.startActivity(intent);

    }
}
