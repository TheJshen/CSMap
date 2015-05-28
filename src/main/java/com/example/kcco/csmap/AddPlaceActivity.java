package com.example.kcco.csmap;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.kcco.csmap.DAO.BuildingDAO;
import com.google.android.gms.maps.model.LatLng;

//TODO: should be delete after it is done
//This is temporary Activity, make it easy to add Place into Parse database
public class AddPlaceActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_place, menu);
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

    public void savePlace(View view){
        EditText nameInput = (EditText)findViewById(R.id.name);
        EditText latitudeInput = (EditText)findViewById(R.id.latitude);
        EditText longitudeInput = (EditText)findViewById(R.id.longitude);

        String name = nameInput.getText().toString();
        double latitude = Double.parseDouble(latitudeInput.getText().toString());
        double longitude = Double.parseDouble(longitudeInput.getText().toString());
        int userId = UserDAO.getCurrentUserId();

        LatLng thisLocation = new LatLng(latitude, longitude);

        BuildingDAO thisPlace = new BuildingDAO(AddPlaceActivity.this);
        thisPlace.createBuilding(name, userId, thisLocation);
        thisPlace.sendBuildingInfo();

    }
}
