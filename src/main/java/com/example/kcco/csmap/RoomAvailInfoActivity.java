package com.example.kcco.csmap;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kcco.csmap.ClassroomSchedule.ScheduleInfo;

import java.util.ArrayList;


public class RoomAvailInfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_avail_info);

        //TODO Dynamically create text views and populate main area/Remove room_avail_info_main_text
        String title = ((String) getIntent().getExtras().get("BuildingName")) + " " + ((String) getIntent().getExtras().get("ClassroomName"));
        ((TextView) findViewById(R.id.room_avail_info_title)).setText(title);

        ArrayList<ScheduleInfo> infoList = getIntent().getParcelableArrayListExtra("ClassroomInfo");
        for(int i = 0, id = 1; i < infoList.size(); ++i, ++id) {
            ScheduleInfo info = infoList.get(i);

            TextView newText = new TextView(this);

            newText.setId(id);
            newText.setText(info.getTimePeriod() + " " + info.getClassName());
            newText.setTextSize((int) (getResources().getDimension(R.dimen.abc_text_size_body_1_material) / getResources().getDisplayMetrics().density));
            newText.setTextColor(getResources().getColor(R.color.text_color));

            int hPadding = getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin);
            int vPadding = getResources().getDimensionPixelOffset(R.dimen.activity_vertical_margin);
            newText.setPadding(hPadding, vPadding, hPadding, 0);

            newText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_main_default, null));

            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            // i = 1 (0) is not a valid ID so it gets put in default location
            rlParams.addRule(RelativeLayout.BELOW, id - 1);

            // Add new TextView to RelativeLayout
            ((RelativeLayout) findViewById(R.id.room_avail_info_main)).addView(newText,rlParams);
        }
    }

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

    public String turnToDisplayString(String timeString) {
        String startTime = timeString.substring(0, timeString.indexOf('-'));
        if(startTime.length() == 5)
            startTime = '0' + startTime;

        String endTime = timeString.substring(timeString.indexOf('-') + 1);
        if(endTime.length() == 5)
            endTime = '0' + endTime;

        return (startTime + "-\n" + endTime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_room_avail_info, menu);
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
