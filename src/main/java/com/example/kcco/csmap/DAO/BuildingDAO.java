package com.example.kcco.csmap.DAO;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by KaChan on 5/11/2015.
 */
public class BuildingDAO {
    Activity activity;
    ParseObject current;

    public BuildingDAO(Activity a){
        activity = a;
    }
    public BuildingDAO(ParseObject obj, Activity a){
        activity = a;
        current = obj;
    }

    /* Name: setFunctions
     * Describe:
     *      This section is all the set functions for all columns in Building.
     * Parameter:
     *      Data input for appropriate column
     * Return:
     */
    //following are places table
    public void setName( String name ) { current.put(ParseConstant.PLACES_NAME, name); }
    public void setPlaceId( int placeId ) { current.put(ParseConstant.PLACES_PLACE_ID, placeId); }
    public void setCratedBy( int createdBy ) { current.put(ParseConstant.PLACES_PLACE_ID, createdBy); }
    public void setX1( double x1) { current.put(ParseConstant.PLACES_X1, x1); }
    public void setY1( double y1) { current.put(ParseConstant.PLACES_X1, y1); }
    public void setX2( double x2) { current.put(ParseConstant.PLACES_X1, x2); }
    public void setY2( double y2) { current.put(ParseConstant.PLACES_X1, y2); }
    public void setX3( double x3) { current.put(ParseConstant.PLACES_X1, x3); }
    public void setY3( double y3) { current.put(ParseConstant.PLACES_X1, y3); }
    public void setX4( double x4) { current.put(ParseConstant.PLACES_X1, x4); }
    public void setY4( double y4) { current.put(ParseConstant.PLACES_X1, y4); }
    //following are classroom table
    public void setClassroomName( String name ) { current.put(ParseConstant.CLASS_NAME, name);}
    public void setClassroomPlaceId( int placeId ) { current.put(ParseConstant.CLASS_PLACE_ID, placeId);}
    //following are Events table
    public void setEventsPlace( String placeName ) { current.put(ParseConstant.EVENTS_LOCATION, placeName); }
    public void setEventsName( String name ) { current.put(ParseConstant.EVENTS_NAME, name); }
    public void setEventsStrTime( Date strTime ) { current.put(ParseConstant.EVENTS_START_TIME, strTime); }
    public void setEventsEndTime( Date endTime ) { current.put(ParseConstant.EVENTS_END_TIME, endTime); }
    public void setEventsId ( int eventsId ) { current.put(ParseConstant.EVENTS_EVENT_ID, eventsId); }

    /* Name: getFunctions
     * Describe:
     *      This section is all the get functions for all columns in User table.
     * Parameter:
     * Return:
     *      Data input for appropriate column
     */
    //Following are places table
    public String getName() { return current.getString(ParseConstant.PLACES_NAME); }
    public int getPlaceId() { return current.getInt(ParseConstant.PLACES_PLACE_ID); }
    public int getCratedBy() { return current.getInt(ParseConstant.PLACES_CREATED_BY); }
    public double getX1() { return current.getDouble(ParseConstant.PLACES_X1); }
    public double getY1() { return current.getDouble(ParseConstant.PLACES_Y1); }
    public double getX2() { return current.getDouble(ParseConstant.PLACES_X2); }
    public double getY2() { return current.getDouble(ParseConstant.PLACES_Y2); }
    public double getX3() { return current.getDouble(ParseConstant.PLACES_X3); }
    public double getY3() { return current.getDouble(ParseConstant.PLACES_Y3); }
    public double getX4() { return current.getDouble(ParseConstant.PLACES_X4); }
    public double getY4() { return current.getDouble(ParseConstant.PLACES_Y4); }
    //following are classroom table
    public String getClassName() { return current.getString(ParseConstant.CLASS_NAME); }
    public int getClassPlaceId() { return current.getInt(ParseConstant.CLASS_PLACE_ID); }
    //following are Events table
    public String getEventsName() { return current.getString(ParseConstant.EVENTS_NAME); }
    public String getEventsPlaceName() { return current.getString(ParseConstant.EVENTS_LOCATION); }
    public Date getEventsStrTime() { return  current.getDate(ParseConstant.EVENTS_START_TIME); }
    public Date getEventsEndTime() { return current.getDate(ParseConstant.EVENTS_END_TIME); }
    public int getEventsId() { return current.getInt(ParseConstant.EVENTS_EVENT_ID); }

///////////////////////////////////////////////writing data ////////////////////////////////////////
     /* Name: createBuilding (not tested)
     * Describe:
     *      This function is gathering all required data locally before it is
     *      sent to Parse server. This can ensure all required data is filled
     *      properly. All data should be VALID before pass to this function.
     * Parameter:
     *      String name: data required for column username
     *      in createdBy: data required for column createdBy, which is same as userId
     *      double x1: data required for column x1, x-coordinate for top-left corner
     *      double y1: data required for column y1, y-coordinate for top-left corner
     *      double x2: data required for column x1, x-coordinate for top-right corner
     *      double y2: data required for column y1, y-coordinate for top-right corner
     *      double x3: data required for column x1, x-coordinate for bottom-right corner
     *      double y3: data required for column y1, y-coordinate for bottom-right corner
     *      double x4: data required for column x1, x-coordinate for bottom-left corner
     *      double y4: data required for column y1, y-coordinate for bottom-left corner
     * Return:
     */
    public void createBuilding(String name, int createdBy, double x1, double y1, double x2,
                               double y2, double x3, double y3, double x4, double y4){
        current = new ParseObject(ParseConstant.PLACES);
        int count = searchNextEmptyPlaceId();

        setName(name);
        setPlaceId(count);
        setCratedBy(createdBy);
        setX1(x1);
        setY1(y1);
        setX1(x2);
        setY1(y2);
        setX1(x3);
        setY1(y3);
        setX1(x4);
        setY1(y4);
    }

    /* Name: createBuilding (not tested)
     * Describe:
     *      This function is overloading createBuilding, passing info if a place has one point only
     * Parameter:
     *      String name: data required for column username
     *      in createdBy: data required for column createdBy, which is same as userId
     *      double x1: data required for column x1, x-coordinate for top-left corner
     *      double y1: data required for column y1, y-coordinate for top-left corner
     *      double x2: data required for column x1, x-coordinate for top-right corner
     *      double y2: data required for column y1, y-coordinate for top-right corner
     *      double x3: data required for column x1, x-coordinate for bottom-right corner
     *      double y3: data required for column y1, y-coordinate for bottom-right corner
     *      double x4: data required for column x1, x-coordinate for bottom-left corner
     *      double y4: data required for column y1, y-coordinate for bottom-left corner
     * Return:
     */
    public void createBuilding(String name, int createdBy, double x1, double y1){
        createBuilding(name, createdBy, x1, y1, x1, y1, x1, y1, x1, y1);
    }

    /* Name: updateBuildingInfo (not tested)
    * Describe:
    *      This function search Places based placeId, then save info in it.
    * Parameter:
    *      int placeId: used to search a place from Parse
    *      String name: updated data
    *      in createdBy: updated data
    *      double x1: updated data, x-coordinate for top-left corner
    *      double y1: updated data, y-coordinate for top-left corner
    *      double x2: updated data, x-coordinate for top-right corner
    *      double y2: updated data, y-coordinate for top-right corner
    *      double x3: updated data, x-coordinate for bottom-right corner
    *      double y3: updated data, y-coordinate for bottom-right corner
    *      double x4: updated data, x-coordinate for bottom-left corner
    *      double y4: updated data, y-coordinate for bottom-left corner
    * Return:
            */
    public void updateBuildingInfo(int placeId, String name, int createdBy, double x1, double y1, double x2,
                                   double y2, double x3, double y3, double x4, double y4){
        current = searchBuilding( placeId, activity).current;
        setName(name);
        setCratedBy(createdBy);
        setX1(x1);
        setY1(y1);
        setX1(x2);
        setY1(y2);
        setX1(x3);
        setY1(y3);
        setX1(x4);
        setY1(y4);
    }

    /* Name: createClassroom (not tested)
     * Describe:
     *      This function is gathering all required data locally before it is
     *      sent to Parse server. This can ensure all required data is filled
     *      properly. All data should be VALID before pass to this function.
     * Parameter:
     *      String name: data required for column username
     *      int placeId: data required for column placeId.
     * Return:
     */
    public void createClassroom(String name, int placeId){
        current = new ParseObject(ParseConstant.CLASS);
        int count = searchNextEmptyPlaceId();

        setName(name);
        setPlaceId(count);
    }

    /* Name: createEvents (not tested)
     * Describe:
     *      This function is gathering all required data locally before it is
     *      sent to Parse server. This can ensure all required data is filled
     *      properly. All data should be VALID before pass to this function.
     * Parameter:
     *      String name: data required for column name
     *      String location: data required for column Place
     *      Date strTime: data required for column startTime
     *      Data endTime: data required for column endTime
     * Return:
     */
    public void createEvent(String name, String location, Date strTime, Date endTime){
        current = new ParseObject(ParseConstant.EVENTS);
        int eventId = searchNextEmptyEventId();

        setEventsName(name);
        setEventsPlace(location);
        setEventsStrTime(strTime);
        setEventsEndTime(endTime);
        setEventsId(eventId);
    }

    /* Name: updateEventInfo (not tested)
    * Describe:
    *      This function search Places based placeId, then save info in it.
    * Parameter:
    *      int eventId: used to search a event from Parse
    *      String name: updated data
    *      String location: updated data
    *      Date strTime: updated data
    * Return:
            */
    public void updateEventInfo(int eventId, String name, String location, Date strTime, Date endTime){
        current = searchEvent(eventId, activity).current;

        setEventsName(name);
        setEventsPlace(location);
        setEventsStrTime(strTime);
        setEventsEndTime(endTime);
    }

/////////////////////////////////////////sending data /////////////////////////////////////////////

    /* Name: sendBuildingInfo (not tested)
     * Describe:
     *      it will send building info to Parse, and show message for success or error
     * Parameter:
     * Return:
     */
    public void sendBuildingInfo(){
        current.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //places is saved successfully
                    String success = "Places is saved!";
                    Messager.toast(success, activity);
                } else {
                    //bookmark did not saved.
                    String errorMessage = "Parse Error: sendBuildingInfo(): " + e.getMessage();
                    Messager.error(errorMessage, activity);
                }
            }
        });
    }

    /* Name: sendClassroomInfo (not tested)
     * Describe:
     *      it will send classroom info to Parse, and show message for success or error
     * Parameter:
     * Return:
     */
    public void sendClassroomInfo(){
        current.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //places is saved successfully
                    String success = "Classroom is saved!";
                    Messager.toast(success, activity);
                } else {
                    //bookmark did not saved.
                    String errorMessage = "Parse Error: sendClassroomInfo(): " + e.getMessage();
                    Messager.error(errorMessage, activity);
                }
            }
        });
    }

    /* Name: sendEventInfo (not tested)
     * Describe:
     *      it will send event info to Parse, and show message for success or error
     * Parameter:
     * Return:
     */
    public void sendEventInfo(){
        current.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //places is saved successfully
                    String success = "Event is saved!";
                    Messager.toast(success, activity);
                } else {
                    //bookmark did not saved.
                    String errorMessage = "Parse Error: sendEventInfo(): " + e.getMessage();
                    Messager.error(errorMessage, activity);
                }
            }
        });
    }

/////////////////////////////////searching data ///////////////////////////////////////////////////

    /* Name: searchNextEmptyPlacesId() (not tested)
     * Describe:
     *      search for the next empty placeId, just new place has an empty placeId
     * Parameter:
     * Return:
     *      int placeId if any match; else null.
     */
    private int searchNextEmptyPlaceId() {
        //define local variable(s) here
        int count = 0;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.PLACES);

        try {
            count = query.count();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //This part is debug purpose to show all results.
        Log.d("UserDAO", "getNextEmptyPlaceId() return " + new Integer(count).toString());

        return count+1;
    }

    /* Name: searchBuilding (not tested)
     * Describe:
     *      it will search a place by the given placeId
     * Parameter:
     *      int placeId: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      BuildingDAO building if any match; else null.
     */
    public static BuildingDAO searchBuilding(int placeId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        BuildingDAO building;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.PLACES);
        query.whereEqualTo(ParseConstant.PLACES_PLACE_ID, placeId);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //There has match cases in User table.
        if( results.size() != 0){
            building = new BuildingDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("BuildingDAO", "searchBuilding(placeID) return BuildingDAO, placeName " + building.getName());

            //Return result for the calling function.
            return building;
        }

        return null;
    }

    /* Name: searchBuilding (not tested)
     * Describe:
     *      it will search a place by the given placeName
     * Parameter:
     *      String placeName: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      BuildingDAO building if any match; else null.
     */
    public static BuildingDAO searchBuilding(String placeName, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        BuildingDAO building;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.PLACES);
        query.whereEqualTo(ParseConstant.PLACES_NAME, placeName);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //There has match cases in User table.
        if( results.size() != 0){
            building = new BuildingDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("BuildingDAO", "searchBuilding(placeName) return BuildingDAO, placeName " + building.getName());

            //Return result for the calling function.
            return building;
        }

        return null;
    }

    /* Name: searchBuilding (not tested)
     * Describe:
     *      it will search a place by the given location
     * Parameter:
     *      double x: the search parameter.
     *      double y: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      BuildingDAO building if any match; else null.
     */
    public static BuildingDAO searchBuilding(double x, double y, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        BuildingDAO building;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.PLACES);
        query.whereGreaterThanOrEqualTo(ParseConstant.PLACES_X1, x);
        query.whereLessThanOrEqualTo(ParseConstant.PLACES_X2, x);
        query.whereLessThanOrEqualTo(ParseConstant.PLACES_Y1, y);
        query.whereGreaterThanOrEqualTo(ParseConstant.PLACES_Y4, y);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //There has match cases in User table.
        if( results.size() != 0){
            building = new BuildingDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("BuildingDAO", "searchBuilding(x, y) return BuildingDAO, placeName " + building.getName());

            //Return result for the calling function.
            return building;
        }

        return null;
    }

    /* Name: searchClassrooms (not tested)
     * Describe:
     *      it will search a list classroom name by the given placeId
     * Parameter:
     *      int placeId: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      String[] classes if any match; else null.
     */
    public static String[] searchClassrooms(int placeId, final Activity activity){
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        BuildingDAO building = null;
        String[] classes;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.CLASS);
        query.selectKeys(Arrays.asList(ParseConstant.CLASS_NAME));
        query.whereEqualTo(ParseConstant.CLASS_PLACE_ID, placeId);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //There has match cases in User table.
        if( results.size() != 0){
            classes = new String[results.size()];
            for( int i = 0; i < results.size(); i++ ){
                building = new BuildingDAO(results.get(i), activity);
                classes[i] = building.getClassName();
            }

            //This part is debug purpose to show all results.
            Log.d("BuildingDAO", "searchBuilding(x, y) return BuildingDAO, # of classes " + classes.length);

            //Return result for the calling function.
            return classes;
        }

        return null;
    }

    /* Name: searchNextEmptyEventsId() (not tested)
     * Describe:
     *      search for the next empty eventId, just new event has an empty eventId
     * Parameter:
     * Return:
     *      int eventId if any match; else null.
     */
    private int searchNextEmptyEventId() {
        //define local variable(s) here
        int count = 0;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.PLACES);

        try {
            count = query.count();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //This part is debug purpose to show all results.
        Log.d("UserDAO", "getNextEmptyEventId() return " + new Integer(count).toString());

        return count+1;
    }

    /* Name: searchEvent (not tested)
     * Describe:
     *      it will search a event by the given eventId
     * Parameter:
     *      int eventId: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      BuildingDAO building if any match; else null.
     */
    public BuildingDAO searchEvent(int eventId, Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        BuildingDAO building;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.EVENTS);
        query.whereEqualTo(ParseConstant.EVENTS_EVENT_ID, eventId);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //There has match cases in User table.
        if( results.size() != 0){
            building = new BuildingDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("BuildingDAO", "searchEvent(eventId) return BuildingDAO, eventName " + building.getEventsName());

            //Return result for the calling function.
            return building;
        }

        return null;
    }

    /* Name: searchAvailable (not tested)
     * Describe:
     *      it will search if the given location is available
     * Parameter:
     *      String location: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      Boolean false for available if any match; else true.
     */
    public boolean searchAvailable(String location, Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        BuildingDAO building;
        Date now = new Date();

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.EVENTS);
        query.whereEqualTo(ParseConstant.EVENTS_LOCATION, location );
        query.whereLessThanOrEqualTo(ParseConstant.EVENTS_START_TIME, now);
        query.whereGreaterThanOrEqualTo(ParseConstant.EVENTS_END_TIME, now);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //There has match cases in User table.
        if( results.size() != 0){
            building = new BuildingDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("BuildingDAO", "searchAvailable(location) return boolean, eventName " + building.getEventsName());

            //Return result for the calling function.
            return false;
        }

        return true;
    }

    /* Name: searchCurrentEvent (not tested)
     * Describe:
     *      it will search current event in the given location
     * Parameter:
     *      String location: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      BuildingDAO building if any match; else true.
     */
    public BuildingDAO searchCurrentEvent(String location, Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        BuildingDAO building;
        Date now = new Date();

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.EVENTS);
        query.whereEqualTo(ParseConstant.EVENTS_LOCATION, location );
        query.whereLessThanOrEqualTo(ParseConstant.EVENTS_START_TIME, now);
        query.whereGreaterThanOrEqualTo(ParseConstant.EVENTS_END_TIME, now);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //There has match cases in User table.
        if( results.size() != 0){
            building = new BuildingDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("BuildingDAO", "searchCurrentEvent(location) return boolean, eventName " + building.getEventsName());

            //Return result for the calling function.
            return building;
        }

        return null;
    }

    /* Name: searchTodayEvent (not tested)
     * Describe:
     *      it will search today schedule in today event
     * Parameter:
     *      String location: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      ArrayList<BuildingDAO> buildings if any match; else true.
     */
    public ArrayList<BuildingDAO> searchTodayEvent(String location, Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        ArrayList<BuildingDAO> buildings = null;
        GregorianCalendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        Date begin = date.getTime();
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        Date end = date.getTime();

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.EVENTS);
        query.whereEqualTo(ParseConstant.EVENTS_LOCATION, location);
        query.whereGreaterThanOrEqualTo(ParseConstant.EVENTS_END_TIME, begin); //catch the event not end the begin of day
        query.whereLessThanOrEqualTo(ParseConstant.EVENTS_START_TIME, end);   //catch the event begin the end of day

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //There has match cases in User table.
        if( results != null && results.size() != 0){
            buildings = new ArrayList<BuildingDAO>(results.size());
            for( ParseObject obj: results )
                    buildings.add(new BuildingDAO(obj, activity));

            //This part is debug purpose to show all results.
            Log.d("BuildingDAO", "searchTodayEvent(location) return ArrayList<BuildingDAO>, # of events " + buildings.size());

            //Return result for the calling function.
            return buildings;
        }

        return null;
    }

}
