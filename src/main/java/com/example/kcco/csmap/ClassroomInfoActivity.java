package com.example.kcco.csmap;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;


public class ClassroomInfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_classroom_info);

        //Get the name of the selected building
        String buildingName = (String) getIntent().getExtras().get("BuildingName");

        //Set title to the name of the building
        ((TextView) findViewById(R.id.classroom_info_title)).setText(buildingName);

        //TODO: Get info from parse

        /* TEST ADD TEXT */
        /*
        for(int i = 1; i <= 100; i++) {
            TextView newText = new TextView(this);

            newText.setId(i);
            newText.setText("This is Working! " + i);
            newText.setTextSize((int) (getResources().getDimension(R.dimen.abc_text_size_body_1_material) / getResources().getDisplayMetrics().density));
            newText.setTextColor(getResources().getColor(R.color.text_color));

            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            // i = 1 (0) is not a valid ID so it gets put in default location
            rlParams.addRule(RelativeLayout.BELOW, i - 1);

            // Add new TextView to RelativeLayout
            ((RelativeLayout) findViewById(R.id.classroom_info_main)).addView(newText,rlParams);
        }
        */
        /* END TEST ADD TEXT */

        /* TEST ADD BUTTONS */
        for(int i = 1; i <= 100; i++) {
            Button newButton = new Button(this);

            newButton.setId(i);
            newButton.setText("This is Working! " + i);
            newButton.setTextSize((int) (getResources().getDimension(R.dimen.abc_text_size_body_1_material) / getResources().getDisplayMetrics().density));
            newButton.setTextColor(getResources().getColor(R.color.text_color));
            newButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_main, null));

            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            // i = 1 (0) is not a valid ID so it gets put in default location
            rlParams.addRule(RelativeLayout.BELOW, i - 1);

            // Add the Listener for the Button
            final int iF = i;
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("Listener", "Button Works " + iF);
                }
            });

            // Add new TextView to RelativeLayout
            ((RelativeLayout) findViewById(R.id.classroom_info_main)).addView(newButton,rlParams);
        }
        /* END TEST ADD BUTTONS*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_classroom_info, menu);
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