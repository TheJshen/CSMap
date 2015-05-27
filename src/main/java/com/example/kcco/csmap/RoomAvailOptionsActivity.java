package com.example.kcco.csmap;

import com.example.kcco.csmap.ClassroomSchedule.UCSDBuilding;
import com.example.kcco.csmap.DAO.ClassroomDAO;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;


public class RoomAvailOptionsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room_avail_options);

        //Get the name of the selected building
        String buildingName = (String) getIntent().getExtras().get("BuildingName");

        //Set title to the name of the building
        ((TextView) findViewById(R.id.room_avail_options_title)).setText(buildingName);

        //Get info from parse
        final UCSDBuilding thisBuilding = new UCSDBuilding(buildingName);
        ArrayList<ClassroomDAO> cls = ClassroomDAO.query(MapsConstants.buildingCodes.get(buildingName));
        for(ClassroomDAO cl : cls) {
            String courseTitle = cl.getCourseCode() + " - " + cl.getCourseName();
            String daysOfWeek = cl.getDaysOfWeek();
            String timePeriod = cl.getStartTime() + "-" + cl.getEndTime();
            String classroomName = cl.getClassroomNumber();

            thisBuilding.addClass(classroomName, courseTitle, daysOfWeek, timePeriod);
        }

        /* Add buttons from UCSDBuilding object */
        ArrayList<String> classroomNames = thisBuilding.listClassrooms();
        for(int i = 0, id = 1; i < classroomNames.size(); i++, id++) {
            Button newButton = new Button(this); // Instantiate New Button

            // Apply UI Design
            newButton.setId(id);
            newButton.setText(buildingName + " " + classroomNames.get(i));
            newButton.setTextSize((int) (getResources().getDimension(R.dimen.abc_text_size_body_1_material) / getResources().getDisplayMetrics().density));
            newButton.setTextColor(getResources().getColor(R.color.text_color));
            newButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_main, null));

            // Position the new Button
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            rlParams.addRule(RelativeLayout.BELOW, id - 1); // id = 1 (0) is not a valid ID so it gets put in default location

            // Add the Listener for the Button
            final String building = buildingName;
            final String classroomName = classroomNames.get(i);
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent nextScreen = new Intent(RoomAvailOptionsActivity.this, RoomAvailInfoActivity.class);
                    nextScreen.putExtra("BuildingName", building);
                    nextScreen.putExtra("ClassroomName", classroomName);
                    nextScreen.putParcelableArrayListExtra("ClassroomInfo", thisBuilding.getClassroomByNumber(classroomName).scheduleToParcelableList());
                    startActivity(nextScreen);
                }
            });

            // Add new Layout to RelativeLayout
            ((RelativeLayout) findViewById(R.id.room_avail_options_main)).addView(newButton,rlParams);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_room_avail_options, menu);
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