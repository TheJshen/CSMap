package com.example.kcco.csmap;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.kcco.csmap.DAO.BuildingDAO;
import com.example.kcco.csmap.DAO.Messenger;
import com.google.android.gms.maps.model.LatLng;


public class RouteActivity extends ActionBarActivity {

    //Constant transport Mode
    private static final int CAR_MODE = 1000;
    private static final int SKATE_MODE = 100;
    private static final int BIKE_MODE = 10;
    private static final int WALK_MODE = 1;

    private int transport = 0;
    private int destinationId = 0;
    private String destinationName = "";

    //latitude and longitude are for either current location or start location
    private LatLng startLocation;
    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        getIntentData();
        BuildingDAO destination = BuildingDAO.searchBuilding(destinationId, RouteActivity.this);

        //destination should not be null because it is from Marker, but just do it in case
        if( destination != null)
            destinationName = destination.getName();

        ((EditText)findViewById(R.id.txtToInput)).setText(destinationName);



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

        int mode = transport;
        //Car button is clicked
        if( mode / CAR_MODE == 1 && tag == CAR_MODE){
            transport -= CAR_MODE;
        }
        else if( tag == CAR_MODE ){
            transport += CAR_MODE;
        }

        mode %= CAR_MODE;
        //Skate button is clicked
        if( mode / SKATE_MODE == 1 && tag == SKATE_MODE){
            transport -= SKATE_MODE;
            //may change button effect
        }
        else if( tag == SKATE_MODE ){
            transport += SKATE_MODE;
            //may change button effect
        }

        mode %= SKATE_MODE;
        //BIKE button is clicked
        if( mode / BIKE_MODE == 1 && tag == BIKE_MODE){
            transport -= BIKE_MODE;
            //may change button effect
        }
        else if( tag == BIKE_MODE ){
            transport += BIKE_MODE;
            //may change button effect
        }

        mode %= BIKE_MODE;
        //Walk button is clicked
        if( mode / WALK_MODE == 1 && tag == WALK_MODE){
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
        String startPlaceName = ((EditText)findViewById(R.id.txtFromInput)).getText().toString();
        boolean validStartPlace = processStartPlace(startPlaceName, RouteActivity.this);

        if( !validStartPlace ) {
            Messenger.error("This is not an invalid place name", RouteActivity.this);
        }
        else{

            Intent intent = new Intent(RouteActivity.this, MapMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //add more information into intent before start different activity
            intent.putExtra("destinationId", destinationId);
            intent.putExtra("latitude", startLocation.latitude);
            intent.putExtra("longitude", startLocation.longitude);
            intent.putExtra("transport", transport);
            intent.putExtra("activity", "RouteActivity");
            RouteActivity.this.startActivity(intent);
        }

    }


/////////////////////////////Helper functions//////////////////////////////////////////////////
    public void getIntentData(){
        destinationId = getIntent().getIntExtra("destinationPlaceId", 0);
        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);
        currentLocation = new LatLng(latitude, longitude);
        startLocation = new LatLng(latitude, longitude);

        //This is debug testing here.
        Log.d("RouteActivity", "destinationId: " + Integer.toString(destinationId));
        Log.d("RouteActivity", "latitude: " + Double.toString(latitude));
        Log.d("RouteActivity", "longitude: " + Double.toString(longitude));
    }

    public boolean processStartPlace(String startPlaceName, Activity activity){

        //There are input for start place name, so it is not current location anymore.
        if( ! startPlaceName.equals("") ){
            Log.d("RoutesActivity", "processStartPlace(): startPlaceName is not empty: "+ startPlaceName+".");
            BuildingDAO startPlace = BuildingDAO.searchBuilding(startPlaceName, activity);

            //startPlace is null means no match result can be found from database.
            if( startPlace == null )
                return false;

            //The startLocation is changed to different location.
            startLocation = startPlace.getCenterPoint();
        }
        //There are no input for start place name, so it should be current location.
        else {
            startLocation = currentLocation;
        }
        return true;
    }


}
