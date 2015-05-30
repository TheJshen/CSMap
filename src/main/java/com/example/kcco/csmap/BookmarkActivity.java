package com.example.kcco.csmap;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kcco.csmap.DAO.BuildingDAO;
import com.example.kcco.csmap.DAO.RoutesDAO;
import com.example.kcco.csmap.R;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Teresa on 5/22/15.
 */

public class BookmarkActivity extends ActionBarActivity {

    private int userID;
    private int[] bookmarks;
    ArrayList<Pair<String, String>> startEndLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        startEndLocation = new ArrayList<Pair<String, String>>();

        // Get current user to find the appropriate bookmarks for
        userID = UserDAO.getCurrentUserId();
        // Search for bookmarks
        bookmarks = UserDAO.searchBookmarkRoutes(userID, this);
        // loop through bookarmsk
        /*for(int routeId: bookmarks){
            String start;
            String end;

            // Retrieve the route
            RoutesDAO route = RoutesDAO.searchARoute(routeId, BookmarkActivity.this);
            // Get the name of the starting location
            BuildingDAO place = BuildingDAO.searchBuilding(route.getStartLoc(), BookmarkActivity.this);
            start = place.getName();

            // Get the name of the ending location
            place = BuildingDAO.searchBuilding(route.getEndLoc(), BookmarkActivity.this);
            end = place.getName();

            startEndLocation.add(new Pair<String, String>(start, end));
        }*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_bookmark, menu);
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





}
