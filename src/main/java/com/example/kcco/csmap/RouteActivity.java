package com.example.kcco.csmap;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class RouteActivity extends ActionBarActivity {

    //Constant transport Mode
    private static final int CAR_MODE = 1000;
    private static final int SKATE_MODE = 100;
    private static final int BIKE_MODE = 10;
    private static final int WALK_MODE = 1;

    private int transport = 0;
    private String destination = "";
    private double latitude = 0.0;
    private double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
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
        //TODO: not sure what activity should go to, temporary MapsActivity
        Intent intent = new Intent(RouteActivity.this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //add more information into intent before start different activity
        intent.putExtra("destination", destination);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("transport", transport);
        RouteActivity.this.startActivity(intent);
    }
}
