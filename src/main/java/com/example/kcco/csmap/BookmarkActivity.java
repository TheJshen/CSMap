package com.example.kcco.csmap;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kcco.csmap.DAO.BuildingDAO;
import com.example.kcco.csmap.DAO.RoutesDAO;
import com.example.kcco.csmap.R;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Teresa on 5/22/15.
 */

// Not tested yet
public class BookmarkActivity extends ActionBarActivity {

    private int userID;
    private int[] bookmarks;
    private ArrayList<Pair<String, String>> startEndLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        // Get current user to find the appropriate bookmarks for
        userID = UserDAO.getCurrentUserId();
        // Initialize the array
        startEndLocation = new ArrayList<>();
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
    //asumme that we have a routeID, need to fig.out how to get rounteID to pass in first
    public void addBookmark(int routeID)
    {
        UserDAO bookmark=new UserDAO(BookmarkActivity.this);
        int userID = UserDAO.getCurrentUserId();
        bookmark.createBookmark(userID,routeID);
        bookmark.sendBookmarkInfo();

    }

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
            }
        }
    }

    // Used to format the items in the list: TODO: Change to make it look nicer??
    private String createBookmarkLabel( Pair<String, String> startEndLocationStrings ) {
        return new String("From: " + startEndLocationStrings.first + "\n" +
                          " To: " + startEndLocationStrings.second);
    }

    // Used to populate the display
    private void populateView() {
        //Set the title
        ((TextView)findViewById(R.id.bookmark_title)).setText("FILLER" /*TODO: fill this in*/);

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

                    //TODO: fill in the response to the button click

                }
            });

            // Add new Layout to RelativeLayout
            ((RelativeLayout) findViewById(R.id.bookmark_main)).addView(newButton,rlParams);
        }
    }
}
