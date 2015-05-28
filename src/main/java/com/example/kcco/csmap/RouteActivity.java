package com.example.kcco.csmap;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.kcco.csmap.DAO.BuildingDAO;


public class RouteActivity extends ActionBarActivity {

    //Constant transport Mode
    private static final int CAR_MODE = 1000;
    private static final int SKATE_MODE = 100;
    private static final int BIKE_MODE = 10;
    private static final int WALK_MODE = 1;

    private int transport = 0;
    private int destinationId = 0;

    //latitude and longitude are for either current location or start location
    private double latitude = 0.0;
    private double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        getIntentData(savedInstanceState);

        BuildingDAO destination = BuildingDAO.searchBuilding(destinationId, RouteActivity.this);
        ((EditText)findViewById(R.id.txtToInput)).setText(destination.getName());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_route, menu);
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



/////////////////////////////Component functions//////////////////////////////////////////////////
    public void selectTransport(View view){
        String tagString = (String)view.getTag();
        int tag = new Integer(tagString).intValue();

        int mode = transport / CAR_MODE;
        int restMode = transport % CAR_MODE;

        //Car button is clicked
        if( mode == 1 && tag == CAR_MODE){
            transport -= CAR_MODE;
            //may change button effect
        }
        else if( tag == CAR_MODE ){
            transport += CAR_MODE;
            //may change button effect
        }

        mode = restMode / SKATE_MODE;
        restMode = transport % SKATE_MODE;

        //Skate button is clicked
        if( mode == 1 && tag == SKATE_MODE){
            transport -= SKATE_MODE;
            //may change button effect
        }
        else if( tag == SKATE_MODE ){
            transport += SKATE_MODE;
            //may change button effect
        }

        mode = restMode / BIKE_MODE;
        restMode = transport % BIKE_MODE;

        //BIKE button is clicked
        if( mode == 1 && tag == BIKE_MODE){
            transport -= BIKE_MODE;
            //may change button effect
        }
        else if( tag == BIKE_MODE ){
            transport += BIKE_MODE;
            //may change button effect
        }

        mode = restMode / WALK_MODE;

        //Walk button is clicked
        if( mode == 1 && tag == WALK_MODE){
            transport -= WALK_MODE;
            //may change button effect
        }
        else if( tag == WALK_MODE ){
            transport += WALK_MODE;
            //may change button effect
        }

        Log.d("RouteActivity", "selectedTransport is called, now transport is " + Integer.toString(transport));
    }

    public void goToMainActivity(View view){
        //TODO: should have function to handle from location if any change
        //TODO: not sure what activity should go to, temporary TrackActivity
        Intent intent = new Intent(RouteActivity.this, TrackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //add more information into intent before start different activity
        intent.putExtra("destinationId", destinationId);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("transport", transport);
        RouteActivity.this.startActivity(intent);
    }


/////////////////////////////Helper functions//////////////////////////////////////////////////
    public void getIntentData(Bundle savedInstanceState){
        destinationId = getIntent().getExtras().getInt("destinationPlaceId");
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        Log.d("RouteActivity", "destinationId: " + Integer.toString(destinationId));
        Log.d("RouteActivity", "latitude: " + Double.toString(latitude));
        Log.d("RouteActivity", "longitude: " + Double.toString(longitude));
    }
}
