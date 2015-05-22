package com.example.kcco.csmap;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kcco.csmap.R;

/**
 * Created by Teresa on 5/22/15.
 */

public class BookmarkActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
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





}
