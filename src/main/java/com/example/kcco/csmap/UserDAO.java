package com.example.kcco.csmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.kcco.csmap.DAO.Messenger;
import com.example.kcco.csmap.DAO.ParseConstant;
import com.parse.DeleteCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by KaChan on 4/29/2015
 * Describe: This class is designed to handle any usage of Parse user objects,
 *           so this project has easy and limited access for the database. It
 *           is easy because it can simply call function and fill out the args.
 *           It is limited because some data are required to fill. This set up
 *           can ensure any other programmer won't create some extra unexpected
 *           column in Parse data objects. The main usage functions would be
 *           create entry, read/search entry, update entry, or delete entry.
 *           Each entry represent a row in the Parse data objects
 * Updated:
 *          -04/29/15: Ka: created file
 *          -04/30/15: Ka: modify comment
 *          -04/30/15: Ka: updated function signUp
 *          -05/06/15: Ka: modify class design
 *          -05/06/15: Ka: created set and get functions
 *          -05/09/15: Ka: created searchUserName and searchNextEmptyUserId functions
 *          -05/10/15: Ka: created searchEmail, searchUser, resetPassword, saveUserInfo, sendUserInfo functions.
 */

/*
 *      This is the amount of code I will expected any other people need to code for any other place
 *      for the project that connect and make use of database
 *
 *              UserDAO user = new UserDAO(RegisterActivity.this);    //initial and connect to database
 *              user.createUser(username, email, password);             //make sure all arguments
 *              ParseException e = user.signUp(RegisterActivity.this);  //save data through internet
 *              ParseErrorHandler.handle( e );                          //separate module to handle error
 */
public class UserDAO{

    Activity activity;
    ParseUser thisUser;
    ParseObject current;
    ParseObject thisBookmark;
    ParseObject thisHistory;
    boolean result = false;

    /* Name: UserDAO
    * Describe:
    *      This is constructor for this class. It will initial the ParseUser objects
    * Parameter:
    */
    public UserDAO(Activity a){
        thisUser = new ParseUser();
        //cannot initial current because we don't know what parse table is talking about here
//        thisBookmark = new ParseObject(ParseConstant.BOOKMARK);
//        thisHistory = new ParseObject(ParseConstant.HISTORY);
        activity = a;
    }

    /* Name: UserDAO
    * Describe:
    *      This is constructor for this class. It will hold the given ParseUser objects
    * Parameter:
    *      ParseUser user: this UserDAO hold it for further purpose.
    */
    public UserDAO(ParseUser user, Activity a){
        thisUser = user;
        activity = a;
    }

    /* Name: UserDAO
    * Describe:
    *      This is constructor for this class. It will hold the given ParseObject.
    * Parameter:
    *      ParseObject p: this UserDAO hold it for further purpose.
    */
    public UserDAO(ParseObject p, Activity a){
        current = p;
        activity = a;
    }

    /* Name: setFunctions
     * Describe:
     *      This section is all the set functions for all columns in User table.
     * Parameter:
     *      Data input for appropriate column
     * Return:
     */
    public void setUsername( String username ) { thisUser.setUsername(username); }
    public void setEmail( String email ) { thisUser.setEmail(email); }
    public void setPassword( String password ) { thisUser.setPassword(password); }
    public void setName( String name ) { thisUser.put(ParseConstant.USER_NAME, name); }
    public void setUserId( int userId ) { thisUser.put(ParseConstant.USER_USER_ID, userId); }
    public void setPhone(String phone) { thisUser.put(ParseConstant.USER_PHONE, phone); }
    public void setRouteId( int routeId ) { current.put(ParseConstant.BOOKMARK_ROUTE_ID, routeId);}
    public void setBookmarkUserId( int userId ) { current.put(ParseConstant.BOOKMARK_USER_ID, userId);}
    public void setHistoryUserId( int userId ) {current.put(ParseConstant.HISTORY_USER_ID, userId);}

    /* Name: getFunctions
     * Describe:
     *      This section is all the get functions for all columns in User table.
     * Parameter:
     * Return:
     *      Data input for appropriate column
     */
    public String getUsername() { return thisUser.getUsername(); }
    public String getEmail() { return thisUser.getEmail(); }
    //no search get password because that is not allow by Parse
    public String getName() { return thisUser.getString(ParseConstant.USER_NAME); }
    public int getUserId() { return thisUser.getInt(ParseConstant.USER_USER_ID); }
    public String getPhone() { return thisUser.getString(ParseConstant.USER_PHONE); }
    public int getRouteId() { return current.getInt(ParseConstant.BOOKMARK_ROUTE_ID); }
    public int getBookmarkUserId() { return current.getInt(ParseConstant.BOOKMARK_USER_ID); }
    public int getHistoryUserId() { return current.getInt(ParseConstant.HISTORY_USER_ID); }

    ///////////////////////////////////////////////writing data ////////////////////////////////////////
    /* Name: createUser
     * Describe:
     *      This function is gathering all required data locally before it is
     *      sent to Parse server. This can ensure all required data is filled
     *      properly. All data should be VALID before pass to this function.
     * Parameter:
     *      String username: data required for column username
     *      String email: data required for column email
     *      String password: data required for column password
     *      String name: data required for column fullname
     * Return:
     */
    public void createUser(String username, String email, String password, String name ){
        int count = searchNextEmptyUserId();

        setUsername(username);
        setPassword(password);
        setEmail(email);
        setName(name);
        setUserId(count);
    }

    /* Name: createUser
     * Describe:
     *      This function is overloading createUser function to default column name
     *      with no input..
     * Parameter:
     *      String username: data required for column username
     *      String email: data required for column email
     *      String password: data required for column password
     * Return:
     */
    public void createUser(String username, String email, String password){
        createUser(username, email, password, "");
    }

    /* Name: updateUserInfo
     * Describe:
     *      This function search User based userId, then save info in it.
     * Parameter:
     *      int userId: used to search user from Parse
     *      String username: updated data
     *      String email: updated data
     *      String password: updated data
     *      String fullname: updated data
     *      String phone: updated data
     * Return:
     */
    public void updateUserInfo( int userId, String username, String email, String password, String fullname, String phone){
        thisUser = searchUser( userId, activity).thisUser;
        setUsername(username);
        setEmail(email);
        if ( password != null )
            setPassword(password);
        setName(fullname);
        setPhone(phone);
    }

    /* Name: createBookmark
     * Describe:
     *      This function save bookmark information before send to it..
     * Parameter:
     *      int userId: data for who bookmark this route
     *      int routeId: data for which route is bookmarked
     * Return:
     */
    public void createBookmark( int userId, int routeId){
        current = new ParseObject(ParseConstant.BOOKMARK);
        setBookmarkUserId(userId);
        setRouteId(routeId);
    }

    /* Name: createHistory
     * Describe:
     *      This function save history information before send to it..
     * Parameter:
     *      int userId: data for who bookmark this route
     *      int routeId: data for which route is bookmarked
     * Return:
     */
    public void createHistory( int userId, int routeId ){
        current = new ParseObject(ParseConstant.HISTORY);
        setBookmarkUserId(userId);
        setRouteId(routeId);
    }

    /////////////////////////////////////////sending data /////////////////////////////////////////////
    /* Name: userSignUp
     * Describe:
     *      activity call this function to sign up. After sign up, it can display
     *      message for success or fail.
     * Parameter:
     * Return:
     */
    public void userSignUp() {
        thisUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                final ProgressDialog dlg = new ProgressDialog(activity);
                dlg.setTitle("Please wait.");
                dlg.setMessage("Signing up, please wait.");
                dlg.show();
                if (e == null) {
                    //sign up successfully
                    String success = "Success, Welcome!";
                    dlg.dismiss();
                    Messenger.toast(success, activity);
                    ((SignUpActivity) activity).switchActivity(1);
                } else {
                    // Sign up didn't succeed. pass exception to Messenger
                    String errorMessage = e.getMessage();
                    dlg.dismiss();
                    Messenger.toast(errorMessage, activity);
                    Messenger.logException(e, "UserDAO", "userSignUp");
                    //((SignUpActivity) activity).switchActivity(0);
                }
            }
        });
    }

    /* Name: sendUserInfo
     * Describe:
     *      it will send user info to Parse, and show message for success or error.
     * Parameter:
     * Return:
     */
    public void sendUserInfo() {
        thisUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //data is saved successfully
                } else {
                    //data did not saved, check ParseException message
                }
            }
        });
    }

    /* Name: resetPassword (not tested)
     * Describe:
     *      it search user email by userId and then send user email url to reset password.
     * Parameter:
     *      int userId: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     */
    public static void resetPassword(int userId, final Activity activity) {
        String email = searchEmail(userId, activity);
        resetPassword(email, activity);
    }

    /* Name: resetPassword (not tested)
     * Describe:
     *      it uses Parse default method to send user email url to reset password.
     * Parameter:
     *      String email: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     */
    public static void resetPassword(String email, final Activity activity) {
        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // an email was successfully sent with reset instructions
                    Messenger.toast("An email has been sent to your email account.", activity);
                } else {
                    // something went wrong. Look at the ParseException message
                    Messenger.toast(e.getMessage(), activity);
                    Messenger.logException(e, "UserDAO", "resetPassword");
                }
            }
        });
    }

    /* Name: logIn (not tested)
     * Describe:
     *      it will search password in User Table by email. Activity is needed
     *      for any error occur.
     * Parameter:
     *      String username: the search parameter.
     *      String password: the search parameter
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      String password if any match; else null.
     */
    public static void logIn(String username, String password, final Activity activity) {
        // Login with the original test database
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                final ProgressDialog dlg = new ProgressDialog(activity);
                dlg.setTitle("Please wait.");
                dlg.setMessage("Logging in, please wait.");
                dlg.show();
                if (e != null) {
                    dlg.dismiss();
                    //show the error message
                    Messenger.toast("Invalid username/password.", activity);
                    Messenger.logException(e, "UserDAO", "logIn");
                } else {
                    dlg.dismiss();
                    ((LoginActivity) activity).switchActivity(1);
                }
            }
        });
    }

    /* Name: logOut (not tested)
     * Describe:
     *      it will log the user out
     * Parameter:
     *      String username: the search parameter.
     *      String password: the search parameter
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      String password if any match; else null.
     */

    public static void logOut (final Activity activity) {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Messenger.error("ParseError: " + e.getMessage(), activity);
                    Messenger.logException(e, "UserDAO", "logOut");
                } else {
                    ((MapMainActivity) activity).goToLoginActivity();
                }
            }
        });
    }


    /* Name: sendBookmarkInfo
     * Describe:
     *      it will send bookmark info to Parse, and show message for success or error
     * Parameter:
     * Return:
     */
    public void sendBookmarkInfo() {
        current.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //bookmark is saved successfully
                    String success = "Bookmark is saved!";
                    Messenger.toast(success, activity);
                } else {
                    //bookmark did not saved.
                    String errorMessage = "Parse Error: saveBookmarkInfo(): " + e.getMessage();
                    Messenger.error(errorMessage, activity);
                    Messenger.logException(e, "UserDAO", "sendBookmarkInfo");
                }
            }
        });
    }

    /* Name: sendHistoryInfo
     * Describe:
     *      it will send history info to Parse, and show message for success or error
     * Parameter:
     * Return:
     */
    public void sendHistoryInfo() {
        current.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //history is saved successfully
                    String success = "History is saved!";
                    Messenger.toast(success, activity);
                } else {
                    //history did not saved.
                    String errorMessage = "Parse Error: saveHistoryInfo(): " + e.getMessage();
                    Messenger.error(errorMessage, activity);
                    Messenger.logException(e, "UserDAO", "sendHistoryInfo");
                }
            }
        });
    }

/////////////////////////////////searching data ///////////////////////////////////////////////////

    /* Name: getCurrentUserId
     * Describe:
     *      it will get the currently Active User Id
     * Parameter:
     * Return:
     *      int userId if any match; else 0.
     */
    public static int getCurrentUserId() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null)
            return 0;
        else
            return currentUser.getInt(com.example.kcco.csmap.DAO.ParseConstant.USER_USER_ID);
    }

    /* Name: isUserActive
     * Describe:
     *      it will get the currently Active User
     * Parameter:
     * Return:
     *      boolean false if no active user; else true.
     */
    public static boolean isUserActive() {
        if (ParseUser.getCurrentUser() == null)
            return false;
        else
            return true;
    }

    /* Name: searchUser
     * Describe:
     *      it will search user by the given userId.
     * Parameter:
     *      int userId: the search parameter.
     *      Activity activity: the activity calls this funtion, needed for exception
     * Return:
     *      String username if any match; else null.
     */
    public static UserDAO searchUser(int userId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseUser> results = null;
        UserDAO user = null;

        //query to fill out all the search requirement
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseConstant.USER);
        query.whereEqualTo(ParseConstant.USER_USER_ID, userId);

        try {
            results = (ArrayList<ParseUser>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchUser");
        }

        //There has match cases in User table.
        if( results.size() != 0){
            user = new UserDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchUser(userID) return UserDAO, username " + user.getUsername());

            //Return result for the calling function.
            return user;
        }

        return null;
    }

    /* Name: searchUser
     * Describe:
     *      it will search user by the given username.
     * Parameter:
     *      String username: the search parameter
     *      Activity activity: the activity calls this funtion, needed for exception
     * Return:
     *      String username if any match; else null.
     */
    public static UserDAO searchUser(String username, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseUser> results = null;
        UserDAO user = null;

        //query to fill out all the search requirement
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseConstant.USER);
        query.whereEqualTo(ParseConstant.USER_USERNAME, username);

        try {
            results = (ArrayList<ParseUser>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchUser");
        }

        //There has match cases in User table.
        if( results.size() != 0){
            user = new UserDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchUser(userID) return UserDAO, username " + user.getUsername());

            //Return result for the calling function.
            return user;
        }

        return null;
    }

    /* Name: searchUsername
     * Describe:
     *      it will search username in UserTable by email. Activity is needed
     *      for any error occur.
     * Parameter:
     *      String email: the search parameter.
     *      Activity activity: the activity calls this funtion, needed for exception
     * Return:
     *      String username if any match; else null.
     */
    public static String searchUsername(String email, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseUser> results = null;
        UserDAO user = null;

        //query to fill out all the search requirement
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseConstant.USER);
        query.selectKeys(Arrays.asList(ParseConstant.USER_USERNAME));
        query.whereEqualTo(ParseConstant.USER_EMAIL, email);

        try {
            results = (ArrayList<ParseUser>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchUsername");
        }

        //There has match cases in User table.
        if( results.size() != 0){
            user = new UserDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchUsername(email) return Username " + user.getUsername());

            //Return result for the calling function.
            return user.getUsername();
        }

        return null;
    }

    /* Name: searchUsername
    * Describe:
    *      it will search username in UserTable by userId. Activity is needed
    *      for any error occur.
    * Parameter:
    *      int userId: the search parameter.
    *      Activity activity: the activity calls this funtion, needed for exception
    * Return:
    *      String username if any match; else null.
    */
    public static String searchUsername(int userId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseUser> results = null;
        UserDAO user = null;

        //query to fill out all the search requirement
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseConstant.USER);
        query.selectKeys(Arrays.asList(ParseConstant.USER_USERNAME));
        query.whereEqualTo(ParseConstant.USER_USER_ID, userId);

        try {
            results = (ArrayList<ParseUser>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchUsername");
        }

        //There has match cases in User table.
        if( results.size() != 0){
            user = new UserDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchUsername(userId) return Username " + user.getUsername());

            //Return result for the calling function.
            return user.getUsername();
        }

        return null;
    }

    /* Name: searchEmail
    * Describe:
    *      it will search email in UserTable by userId. Activity is needed
    *      for any error occur.
    * Parameter:
    *      int userId: the search parameter.
    *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      String username if any match; else null.
    */
    public static String searchEmail(int userId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseUser> results = null;
        UserDAO user = null;

        //query to fill out all the search requirement
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseConstant.USER);
        query.selectKeys(Arrays.asList(ParseConstant.USER_EMAIL));
        query.whereEqualTo(ParseConstant.USER_USER_ID, userId);

        try {
            results = (ArrayList<ParseUser>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchEmail");
        }

        //There has match cases in User table.
        if( results.size() != 0){
            user = new UserDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchEmail(userId) return Email " + user.getEmail());

            //Return result for the calling function.
            return user.getEmail();
        }

        return null;
    }

    /* Name: searchEmail
    * Describe:
    *      it will search email in UserTable by username. Activity is needed
    *      for any error occur.
    * Parameter:
    *      String username: the search parameter.
    *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      String username if any match; else null.
    */
    public static String searchEmail(String username, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseUser> results = null;
        UserDAO user = null;

        //query to fill out all the search requirement
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseConstant.USER);
        query.selectKeys(Arrays.asList(ParseConstant.USER_EMAIL));
        query.whereEqualTo(ParseConstant.USER_USERNAME, username);

        try {
            results = (ArrayList<ParseUser>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchEmail");
        }

        //There has match cases in User table.
        if( results.size() != 0){
            user = new UserDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchEmail(userId) return Email " + user.getEmail());

            //Return result for the calling function.
            return user.getEmail();
        }

        return null;
    }

    /* Name: searchNextEmptyUserId()
     * Describe:
     *      search for the next empty userId, just new user has an empty userId
     * Parameter:
     * Return:
     *      String username if any match; else null.
     */
    private int searchNextEmptyUserId() {
        //define local variable(s) here
        int count = 0;

        //query to fill out all the search requirement
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseConstant.USER);

        try {
            count = query.count();
        }
        catch(ParseException e) {
            Toast.makeText(activity, "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //This part is debug purpose to show all results.
        Log.d("UserDAO", "getNextEmptyUserId() return " + Integer.toString(count));

        return count+1;
    }

    /* Name: searchABookmark
     * Describe:
     *      it will search a bookmark record by userId and routeId.
     * Parameter:
     *      int userId: the search parameter.
     *      int routeId: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      UserDAO if any match; else null.
     */
    public static UserDAO searchABookmark(int userId, int routeId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        UserDAO user = null;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.BOOKMARK);
        query.whereEqualTo(ParseConstant.BOOKMARK_USER_ID, userId);
        query.whereEqualTo(ParseConstant.BOOKMARK_ROUTE_ID, routeId);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchABookmark");
        }

        //There has match cases in User table.
        if( results.size() != 0){
            user = new UserDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchABookmark(userID, routeId) return UserDAO, objectId " + results.get(0).getObjectId());

            //Return result for the calling function.
            return user;
        }

        return null;
    }

    /* Name: searchAllBookmark (not tested)
     * Describe:
     *      it will search all bookmark record by userId. This function mostly likely is
     *      not used by other places besides some other DAO.
     * Parameter:
     *      int userId: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      ArrayList<ParseObject> results if any match; else null.
     */
    public static ArrayList<ParseObject> searchAllBookmark(int userId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.BOOKMARK);
        query.whereEqualTo(ParseConstant.BOOKMARK_USER_ID, userId);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchAllBookmark");
        }

        //There has match cases in User table.
        if( results.size() != 0){

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchAllBookmark(userID) return ArrayList<UserDAO>, # of results " + results.size());

            //Return result for the calling function.
            return results;
        }

        return null;
    }

    /* Name: searchBookmarkRoutes
    * Describe:
    *      it will search routesId by userId. Activity is needed
    *      for any error occur.
    * Parameter:
    *      int userId: the search parameter.
    *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      int[] routes if any match; else null.
    */
    public static int[] searchBookmarkRoutes(int userId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        UserDAO user = null;
        int routes[] = null;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.BOOKMARK);
        query.selectKeys(Arrays.asList(ParseConstant.BOOKMARK_ROUTE_ID));
        query.whereEqualTo(ParseConstant.BOOKMARK_USER_ID, userId);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchBookmarkRoutes");
        }

        //There has match cases in User table.
        if( results.size() != 0){
            routes = new int[results.size()];
            for( int i = 0; i < results.size(); i++ ){
                user = new UserDAO(results.get(i), activity);
                routes[i] = user.getRouteId();
            }

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchBookmarkRoute(userId) return # of routes " + routes.length);

            //Return result for the calling function.
            return routes;
        }

        return null;
    }

    /* Name: searchAHistory (not tested)
     * Describe:
     *      it will search a history record by userId and routeId.
     * Parameter:
     *      int userId: the search parameter.
     *      int routeId: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      UserDAO if any match; else null.
     */
    public static UserDAO searchAHistory(int userId, int routeId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        UserDAO user = null;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.HISTORY);
        query.whereEqualTo(ParseConstant.HISTORY_USER_ID, userId);
        query.whereEqualTo(ParseConstant.HISTORY_ROUTE_ID, routeId);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchAHistory");
        }

        //There has match cases in User table.
        if( results.size() != 0){
            user = new UserDAO(results.get(0), activity);

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchBookmark(userID, routeId) return UserDAO, objectId " + results.get(0).getObjectId());

            //Return result for the calling function.
            return user;
        }

        return null;
    }

    /* Name: searchAllHistory (not tested)
     * Describe:
     *      it will search all history record by userId.This function mostly likely is
     *      not used by other places besides some other DAO.
     * Parameter:
     *      int userId: the search parameter.
     *      Activity activity: the activity calls this function, needed for exception
     * Return:
     *      UserDAO if any match; else null.
     */
    public static ArrayList<ParseObject> searchAllHistory(int userId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.HISTORY);
        query.whereEqualTo(ParseConstant.HISTORY_USER_ID, userId);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchAllHistory");
        }

        //There has match cases in User table.
        if( results.size() != 0){

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchAllBookmark(userID) return ArrayList<UserDAO>, # of results " + results.size());

            //Return result for the calling function.
            return results;
        }

        return null;
    }

    /* Name: searchHistoryRoutes (not tested)
    * Describe:
    *      it will search routesId by userId. Activity is needed
    *      for any error occur.
    * Parameter:
    *      int userId: the search parameter.
    *      Activity activity: the activity calls this function, needed for exception
    * Return:
    *      int[] routes if any match; else null.
    */
    public static int[] searchHistoryRoutes(int userId, final Activity activity) {
        //define local variable(s) here
        ArrayList<ParseObject> results = null;
        UserDAO user = null;
        int[] routes = null;

        //query to fill out all the search requirement
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstant.HISTORY);
        query.selectKeys(Arrays.asList(ParseConstant.HISTORY_ROUTE_ID));
        query.whereEqualTo(ParseConstant.HISTORY_USER_ID, userId);

        try {
            results = (ArrayList<ParseObject>) query.find();
        }
        catch(ParseException e) {
            Messenger.error("Parse Error: " + e.getMessage(), activity);
            Messenger.logException(e, "UserDAO", "searchHistoryRoutes");
        }

        //There has match cases in User table.
        if( results.size() != 0){
            routes = new int[results.size()];
            for( int i = 0; i < results.size(); i++ ){
                user = new UserDAO(results.get(i), activity);
                routes[i] = user.getRouteId();
            }

            //This part is debug purpose to show all results.
            Log.d("UserDAO", "searchBookmarkRoute(userId) return # of routes " + routes.length);

            //Return result for the calling function.
            return routes;
        }

        return null;
    }




///////////////////////////////////delete data/////////////////////////////////////////////////////

    /* Name: deleteABookmark
    * Describe:
    *      it will delete a bookmark record
    * Parameter:
    * Return:
    */
    public void deleteABookmark(){
        final String objectId = current.getObjectId();
        current.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //bookmark is removed
                    String success = "Bookmark " + objectId + " is deleted!";
                    Messenger.toast(success, activity);
                } else {
                    // bookmark did not removed
                    String errorMessage = "Parse Error: deleteABookmark: " + e.getMessage();
                    Messenger.error(errorMessage, activity);
                    Messenger.logException(e, "UserDAO", "deleteABookmark");
                }
            }
        });
    }

    /* Name: deleteAllBookmark
    * Describe:
    *      it will delete all bookmarks with the given userId
    * Parameter:
    *       int userId: the delete range
    *       Activity activity: the activity calls this function, needed for exception
    * Return:
    */
    public static void deleteAllBookmarks(int userId, final Activity activity) {
        ArrayList<ParseObject> bookmarks = searchAllBookmark(userId, activity);
        ParseObject.deleteAllInBackground(bookmarks, new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //bookmark is removed
                    String success = "all Bookmarks are deleted!";
                    Messenger.toast(success, activity);
                } else {
                    // bookmark did not removed
                    String errorMessage = "Parse Error: deleteAllBookmark: " + e.getMessage();
                    Messenger.error(errorMessage, activity);
                    Messenger.logException(e, "UserDAO", "deleteAllBookmark");
                }
            }
        });

    }

    /* Name: deleteAHistory
    * Describe:
    *      it will delete a history record
    * Parameter:
    * Return:
    */
    public void deleteAHistory(){
        final String objectId = current.getObjectId();
        current.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //history is removed
                    String success = "History " + objectId + " is deleted!";
                    Messenger.toast(success, activity);
                } else {
                    // bookmark did not removed
                    String errorMessage = "Parse Error: deleteAHistory: " + e.getMessage();
                    Messenger.error(errorMessage, activity);
                    Messenger.logException(e, "UserDAO", "deleteAHistory");
                }
            }
        });
    }

    /* Name: deleteAllHistories
    * Describe:
    *      it will delete all histories with the given userId
    * Parameter:
    *       int userId: the delete range
    *       Activity activity: the activity calls this function, needed for exception
    * Return:
    */
    public static void deleteAllHistories(int userId, final Activity activity) {
        ArrayList<ParseObject> histories = searchAllHistory(userId, activity);
        ParseObject.deleteAllInBackground(histories, new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //bookmark is removed
                    String success = "all Histories are deleted!";
                    Messenger.toast(success, activity);
                } else {
                    // bookmark did not removed
                    String errorMessage = "Parse Error: deleteAllHistories: " + e.getMessage();
                    Messenger.error(errorMessage, activity);
                    Messenger.logException(e, "UserDAO", "deleteAllHistories");
                }
            }
        });
    }
}

